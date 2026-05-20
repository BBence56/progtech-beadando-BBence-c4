#!/bin/bash
# Database migration test script

# Clean up test database
TEST_HOME=$(mktemp -d)
export HOME=$TEST_HOME

echo "Testing database migration..."
echo "Test directory: $TEST_HOME"

# Run the JAR with a headless mode for testing
java -Djava.awt.headless=true \
     -cp "target/c4-baranbence-beadando-1.0-SNAPSHOT-jar-with-dependencies.jar" \
     nye.bence.database.Database \
     2>&1 | grep -E "(Connection|migration|Database|Error)" || echo "Note: GUI test would require display"

# Check if database was created
if [ -f "$TEST_HOME/.connect4/connect4.db" ]; then
    echo "✓ Database file created successfully at: $TEST_HOME/.connect4/connect4.db"
    
    # Verify database schema
    echo "Database tables:"
    sqlite3 "$TEST_HOME/.connect4/connect4.db" ".tables"
    
    echo ""
    echo "Schema version table content:"
    sqlite3 "$TEST_HOME/.connect4/connect4.db" "SELECT * FROM schema_version;"
    
    echo ""
    echo "✓ Database migration test PASSED"
else
    echo "✗ Database file not created at expected location"
    echo "Checking for .connect4 directory..."
    ls -la "$TEST_HOME/" 2>/dev/null || echo "Test directory empty"
    exit 1
fi
