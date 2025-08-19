package org.alanc.mastermind.persistence;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.game.GameState;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic tests for GameConverter functionality.
 */
class GameConverterTest {

    @Test
    void testToRecordAndFromRecord() {
        // Create a test game state
        GameConfig config = GameConfig.defaults();
        GameState gameState = GameState.createNew("1 2 3 4", config);
        
        // Make a guess to add some history
        gameState = gameState.withGuess(new int[]{1, 2, 3, 5});
        
        LocalDateTime startTime = LocalDateTime.now();
        Long gameId = 123L;
        
        // Convert to record
        GameRecord record = GameConverter.toRecord(gameState, gameId, startTime);
        
        // Verify record fields
        assertEquals(gameId, record.getId());
        assertEquals("1 2 3 4", record.getSecretCode());
        assertEquals(config.getMaxAttempts(), record.getMaxAttempts());
        assertEquals("IN_PROGRESS", record.getStatus());
        assertEquals(startTime, record.getStartedAt());
        assertNotNull(record.getGuessesJson());
        
        // Convert back to game state
        GameConverter.GameStateResult result = GameConverter.fromRecord(record);
        
        // Verify round-trip conversion
        assertEquals(gameState.getSecretCode(), result.gameState().getSecretCode());
        assertEquals(gameState.getAttemptsRemaining(), result.gameState().getAttemptsRemaining());
        assertEquals(gameState.getGuessHistory().size(), result.gameState().getGuessHistory().size());
        assertEquals(gameId, result.gameId());
        assertEquals(startTime, result.startedAt());
    }

    @Test
    void testCompletedGameStatus() {
        GameConfig config = GameConfig.defaults();
        GameState gameState = GameState.createNew("1 2 3 4", config);
        
        // Make winning guess
        gameState = gameState.withGuess(new int[]{1, 2, 3, 4});
        
        GameRecord record = GameConverter.toRecord(gameState, 1L, LocalDateTime.now());
        
        assertEquals("WON", record.getStatus());
        assertNotNull(record.getCompletedAt());
    }
}
