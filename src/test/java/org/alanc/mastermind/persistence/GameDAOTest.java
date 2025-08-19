package org.alanc.mastermind.persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic tests for GameDAO database operations.
 */
class GameDAOTest {
    private static final String TEST_DB = "test_games.db";
    private GameDAO gameDAO;

    @BeforeEach
    void setUp() {
        // Clean up any existing test database
        deleteTestDb();
        gameDAO = new GameDAO(TEST_DB);
    }

    @AfterEach
    void tearDown() throws Exception {
        gameDAO.close();
        deleteTestDb();
    }

    @Test
    void testSaveAndRetrieveGame() {
        GameRecord record = createTestGameRecord();
        
        GameRecord saved = gameDAO.saveGame(record);
        
        assertNotNull(saved.getId());
        assertEquals(record.getSecretCode(), saved.getSecretCode());
        assertEquals(record.getStatus(), saved.getStatus());
    }

    @Test
    void testGetLastGame() {
        GameRecord record = createTestGameRecord();
        gameDAO.saveGame(record);
        
        Optional<GameRecord> last = gameDAO.getLastGame();
        
        assertTrue(last.isPresent());
        assertEquals(record.getSecretCode(), last.get().getSecretCode());
    }

    @Test
    void testIsLastGameIncomplete() {
        assertFalse(gameDAO.isLastGameIncomplete());
        
        GameRecord incompleteGame = createTestGameRecord();
        gameDAO.saveGame(incompleteGame);
        
        assertTrue(gameDAO.isLastGameIncomplete());
    }

    @Test
    void testDeleteAllGames() {
        gameDAO.saveGame(createTestGameRecord());
        gameDAO.saveGame(createTestGameRecord());
        
        List<GameRecord> before = gameDAO.getAllGames();
        assertEquals(2, before.size());
        
        gameDAO.deleteAllGames();
        
        List<GameRecord> after = gameDAO.getAllGames();
        assertTrue(after.isEmpty());
    }

    private GameRecord createTestGameRecord() {
        return new GameRecord(
            null,
            "1 2 3 4",
            10,
            4,
            9,
            "IN_PROGRESS",
            LocalDateTime.now(),
            null,
            "[]"
        );
    }

    private void deleteTestDb() {
        File testDbFile = new File(TEST_DB);
        if (testDbFile.exists()) {
            testDbFile.delete();
        }
    }
}
