package org.alanc.mastermind.persistence;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.game.GameState;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Converts between GameState (domain) and GameRecord (persistence).
 * Handles JSON serialization of guess history.
 */
public class GameConverter {
    private static final Logger logger = LoggerFactory.getLogger(GameConverter.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Converts GameState to GameRecord for database storage.
     */
    public static GameRecord toRecord(GameState gameState, Long id, LocalDateTime startedAt) {
        String status = determineStatus(gameState);
        LocalDateTime completedAt = gameState.isGameEnded() ? LocalDateTime.now() : null;
        String guessesJson = serializeGuesses(gameState.getGuessHistory());

        return new GameRecord(
            id,
            gameState.getSecretCode(),
            gameState.getMaxAttempts(),
            gameState.getCodeLength(),
            gameState.getMaxNumber(),
            status,
            startedAt,
            completedAt,
            guessesJson
        );
    }

    /**
     * Converts GameRecord back to GameState and GameConfig for gameplay.
     */
    public static GameStateResult fromRecord(GameRecord record) {
        GameConfig config = new GameConfig.Builder()
            .maxAttempts(record.getMaxAttempts())
            .codeLength(record.getCodeLength())
            .maxNumber(record.getMaxNumber())
            .build();

        // For resumed games, we need to reconstruct the game state
        GameState baseState = GameState.createNew(record.getSecretCode(), config);
        
        // Apply each guess from the history to rebuild the state
        List<GuessResult> guesses = deserializeGuesses(record.getGuessesJson());
        GameState currentState = baseState;
        
        for (GuessResult guess : guesses) {
            if (!currentState.isGameEnded()) {
                // Convert guess string back to numbers and apply
                String[] parts = guess.guess().split("\\s+");
                int[] numbers = new int[parts.length];
                for (int i = 0; i < parts.length; i++) {
                    numbers[i] = Integer.parseInt(parts[i]);
                }
                currentState = currentState.withGuess(numbers);
            }
        }

        return new GameStateResult(currentState, config, record.getId(), record.getStartedAt());
    }

    private static String determineStatus(GameState gameState) {
        if (!gameState.isGameEnded()) {
            return GameStatus.IN_PROGRESS.name();
        }
        return gameState.hasPlayerWon() ? GameStatus.WON.name() : GameStatus.LOST.name();
    }

    private static String serializeGuesses(List<GameState.GuessResult> guesses) {
        try {
            return mapper.writeValueAsString(guesses);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize guesses", e);
            return "[]";
        }
    }

    private static List<GuessResult> deserializeGuesses(String guessesJson) {
        try {
            if (guessesJson == null || guessesJson.trim().isEmpty()) {
                return List.of();
            }
            return mapper.readValue(guessesJson, 
                mapper.getTypeFactory().constructCollectionType(List.class, GuessResult.class));
        } catch (JsonProcessingException e) {
            logger.error("Failed to deserialize guesses: {}", guessesJson, e);
            return List.of();
        }
    }

    /**
     * Result of converting a GameRecord back to game objects.
     * 
     * @param gameState the reconstructed game state
     * @param config the game configuration
     * @param gameId the unique game identifier
     * @param startedAt when the game was originally started
     */
    public record GameStateResult(GameState gameState, GameConfig config, Long gameId, LocalDateTime startedAt) {}

    /**
     * Record for JSON serialization of guess results.
     * 
     * @param guess the player's guess as a string
     * @param correctNumbers number of correct digits in wrong positions
     * @param correctLocations number of correct digits in correct positions
     * @param allCorrect whether this guess was completely correct
     */
    public record GuessResult(String guess, int correctNumbers, int correctLocations, boolean allCorrect) {}
}
