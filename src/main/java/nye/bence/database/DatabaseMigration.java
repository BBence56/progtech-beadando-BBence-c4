package nye.bence.database;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handles database schema migrations.
 * Migrations are SQL files in src/main/resources/migrations/ with naming convention:
 * V###__Description.sql (e.g., V001__Create_player_table.sql)
 */
public class DatabaseMigration {

    private static final Logger LOGGER = Logger.getLogger(DatabaseMigration.class.getName());
    private static final String MIGRATIONS_PATH = "/migrations/";
    private static final String SCHEMA_VERSION_TABLE = "schema_version";
    private static final String PLAYER_TABLE = "player";

    private final Connection connection;

    /**
     * Constructs a new DatabaseMigration with the specified connection.
     *
     * @param connection the database connection
     */
    public DatabaseMigration(Connection connection) {
        this.connection = connection;
    }

    /**
     * Runs all pending migrations.
     *
     * @throws SQLException if a database error occurs
     */
    public void migrate() throws SQLException {
        try {
            // Ensure schema_version table exists
            ensureSchemaVersionTable();

            // Get list of migration files
            List<String> migrationFiles = getMigrationFiles();

            // Apply each migration that hasn't been applied yet
            for (String migrationFile : migrationFiles) {
                String version = extractVersionFromFilename(migrationFile);
                if (!isMigrationApplied(version)) {
                    applyMigration(version, migrationFile);
                }
            }

            LOGGER.info("Database migrations completed successfully.");
        } catch (SQLException e) {
            LOGGER.severe("Database migration failed: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Ensures the schema_version table exists for tracking migrations.
     */
    private void ensureSchemaVersionTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + SCHEMA_VERSION_TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "version TEXT NOT NULL UNIQUE,"
                + "description TEXT NOT NULL,"
                + "installed_on TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + "execution_time_ms INTEGER DEFAULT 0"
                + ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    /**
     * Gets list of migration files from the migrations directory.
     *
     * @return list of migration filenames
     */
    private List<String> getMigrationFiles() {
        List<String> migrations = new ArrayList<>();

        // Manual list of migrations (since classpath scanning is complex)
        migrations.add("V001__Create_player_table.sql");
        migrations.add("V002__Create_schema_version_table.sql");

        return migrations;
    }

    /**
     * Checks if a migration has already been applied.
     *
     * @param version the migration version
     * @return true if applied, false otherwise
     */
    private boolean isMigrationApplied(String version) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + SCHEMA_VERSION_TABLE + " WHERE version = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, version);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    /**
     * Applies a single migration.
     *
     * @param version the migration version
     * @param filename the migration file name
     * @throws SQLException if migration fails
     */
    private void applyMigration(String version, String filename) throws SQLException {
        long startTime = System.currentTimeMillis();

        try {
            // Read migration SQL
            String sql = readMigrationFile(filename);

            // Execute migration
            try (Statement stmt = connection.createStatement()) {
                stmt.executeUpdate(sql);
            }

            // Record migration in schema_version
            long executionTime = System.currentTimeMillis() - startTime;
            String description = extractDescriptionFromFilename(filename);
            recordMigration(version, description, executionTime);

            LOGGER.info("Migration " + version + " applied successfully ("
                    + executionTime + "ms)");
        } catch (SQLException e) {
            LOGGER.severe("Failed to apply migration " + version + ": " + e.getMessage());
            throw e;
        }
    }

    /**
     * Reads the content of a migration file.
     *
     * @param filename the migration filename
     * @return the SQL content
     * @throws SQLException if file cannot be read
     */
    private String readMigrationFile(String filename) throws SQLException {
        try {
            URL resource = getClass().getResource(MIGRATIONS_PATH + filename);
            if (resource == null) {
                throw new SQLException("Migration file not found: " + filename);
            }

            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    // Skip comments and empty lines
                    if (!line.trim().startsWith("--") && !line.trim().isEmpty()) {
                        content.append(line).append("\n");
                    }
                }
            }

            return content.toString();
        } catch (IOException e) {
            throw new SQLException("Failed to read migration file: " + filename, e);
        }
    }

    /**
     * Records a migration in the schema_version table.
     *
     * @param version the version
     * @param description the description
     * @param executionTime the execution time in milliseconds
     */
    private void recordMigration(String version, String description, long executionTime)
            throws SQLException {
        String sql = "INSERT INTO " + SCHEMA_VERSION_TABLE
                + " (version, description, execution_time_ms) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, version);
            pstmt.setString(2, description);
            pstmt.setLong(3, executionTime);
            pstmt.executeUpdate();
        }
    }

    /**
     * Extracts version from migration filename.
     * Example: V001__Create_player_table.sql -> V001
     *
     * @param filename the filename
     * @return the version string
     */
    private String extractVersionFromFilename(String filename) {
        return filename.substring(0, filename.indexOf("__"));
    }

    /**
     * Extracts description from migration filename.
     * Example: V001__Create_player_table.sql -> Create player table
     *
     * @param filename the filename
     * @return the description
     */
    private String extractDescriptionFromFilename(String filename) {
        return filename
                .substring(filename.indexOf("__") + 2)
                .replace(".sql", "")
                .replace("_", " ");
    }

    /**
     * Gets the list of applied migrations.
     *
     * @return list of applied migration versions
     */
    public List<String> getAppliedMigrations() throws SQLException {
        List<String> applied = new ArrayList<>();
        String sql = "SELECT version FROM " + SCHEMA_VERSION_TABLE + " ORDER BY version";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                applied.add(rs.getString("version"));
            }
        }

        return applied;
    }

    /**
     * Validates that all required tables exist.
     *
     * @throws SQLException if validation fails
     */
    public void validateSchema() throws SQLException {
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, PLAYER_TABLE);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                throw new SQLException("Required table '" + PLAYER_TABLE + "' does not exist. "
                        + "Migrations may not have been applied correctly.");
            }
        }
    }
}
