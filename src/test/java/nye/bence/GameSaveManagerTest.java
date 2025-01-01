package nye.bence;

import nye.bence.game.Board;
import nye.bence.user.Player;
import nye.bence.util.GameSaveManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class GameSaveManagerTest {

    private static final String SAVE_DIR = "saves";
    private static final String PLAYER_NAME = "TestPlayer";
    private Player player;
    private Board board;

    @BeforeEach
    public void setUp() {
        player = new Player(PLAYER_NAME, 10);
        board = new Board();
        new File(SAVE_DIR).mkdirs();
    }

    @AfterEach
    public void tearDown() {
        File saveFile = new File(SAVE_DIR + "/" + PLAYER_NAME + "_save.xml");
        if (saveFile.exists()) {
            saveFile.delete();
        }
    }

    @Test
    public void testSaveGame() {
        GameSaveManager.saveGame(board, player);
        File saveFile = new File(SAVE_DIR + "/" + PLAYER_NAME + "_save.xml");
        assertTrue(saveFile.exists(), "Save file should exist after saving the game.");
    }

    @Test
    public void testLoadGame() {
        GameSaveManager.saveGame(board, player);
        Board loadedBoard = GameSaveManager.loadGame(player);
        assertNotNull(loadedBoard, "Loaded board should not be null.");
        assertArrayEquals(board.getBoard(), loadedBoard.getBoard(), "Loaded board should match the saved board.");
    }

    @Test
    public void testLoadGameFileNotFound() {
        Board loadedBoard = GameSaveManager.loadGame(player);
        assertNull(loadedBoard, "Loaded board should be null if the save file does not exist.");
    }
}
