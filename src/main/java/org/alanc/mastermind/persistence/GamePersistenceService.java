package org.alanc.mastermind.persistence;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.game.GameState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Service for persisting and loading game states.
 * Bridges between game logic and database storage.
 */
public class GamePersistenceService implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(GamePersistenceService.class);
    private final GameDAO gameDAO;

    public GamePersistenceService(GameDAO gameDAO) {
        this.gameDAO = gameDAO;
    }

    /**
     * Saves a new game state and returns the saved record with ID.
     */
    public GameRecord saveNewGame(GameState gameState, LocalDateTime startedAt) {
        GameRecord record = GameConverter.toRecord(gameState, null, startedAt);
        GameRecord savedRecord = gameDAO.saveGame(record);
        logger.debug("Saved new game with ID: {}", savedRecord.getId());
        return savedRecord;
    }

    /**
     * Updates an existing game state.
     */
    public void updateGame(GameState gameState, Long gameId, LocalDateTime startedAt) {
        GameRecord record = GameConverter.toRecord(gameState, gameId, startedAt);
        gameDAO.updateGame(record);
        logger.debug("Updated game with ID: {}", gameId);
    }

    /**
     * Checks if the most recent game is incomplete.
     */
    public boolean isLastGameIncomplete() {
        return gameDAO.isLastGameIncomplete();
    }

    /**
     * Gets the last incomplete game for resume functionality.
     */
    public Optional<GameConverter.GameStateResult> getLastIncompleteGame() {
        if (gameDAO.isLastGameIncomplete()) {
            Optional<GameRecord> record = gameDAO.getLastGame();
            if (record.isPresent()) {
                GameConverter.GameStateResult result = GameConverter.fromRecord(record.get());
                logger.debug("Found incomplete game with ID: {}", record.get().getId());
                return Optional.of(result);
            }
        }
        return Optional.empty();
    }

    /**
     * Marks the last game as abandoned.
     */
    public void markLastGameAsAbandoned() {
        Optional<GameRecord> existing = gameDAO.getLastGame();
        if (existing.isPresent()) {
            GameRecord record = existing.get();
            GameRecord updatedRecord = new GameRecord(
                record.getId(),
                record.getSecretCode(),
                record.getMaxAttempts(),
                record.getCodeLength(),
                record.getMaxNumber(),
                GameStatus.ABANDONED.name(),
                record.getStartedAt(),
                LocalDateTime.now(),
                record.getGuessesJson()
            );
            gameDAO.updateGame(updatedRecord);
            logger.debug("Marked game {} as abandoned", record.getId());
        }
    }

    /**
     * Gets all games for history display.
     */
    public List<GameRecord> getAllGames() {
        return gameDAO.getAllGames();
    }

    /**
     * Clears all game history.
     */
    public void clearAllGames() {
        gameDAO.deleteAllGames();
        logger.info("Cleared all game history");
    }

    @Override
    public void close() throws Exception {
        gameDAO.close();
    }
}
