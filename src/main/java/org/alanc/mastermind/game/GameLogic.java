package org.alanc.mastermind.game;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.random.MathRandomService;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Core game logic for Mastermind, handling game creation and guess processing.
 * 
 * This class encapsulates the fundamental rules of Mastermind:
 * - Generating secret codes using configurable random number services
 * - Processing player guesses and determining their accuracy
 * - Validating inputs according to game configuration
 * - Providing fallback mechanisms for random number generation
 */
public final class GameLogic {
    private static final Logger logger = LoggerFactory.getLogger(GameLogic.class);

    private final RandomNumberService randomNumberService;

    /**
     * Constructs a new GameLogic instance with the specified random number service.
     * 
     * @param randomNumberService the service to use for generating secret codes
     */
    public GameLogic(RandomNumberService randomNumberService) {
        this.randomNumberService = randomNumberService;
    }

    /**
     * Creates a new game with a randomly generated secret code.
     * Uses fallback MathRandomService if primary service fails.
     * 
     * @param config the game configuration specifying code length, number range, etc.
     * @return a new GameState ready for player guesses
     */
    public GameState createNewGame(GameConfig config) {
        String secretCode = generateSecretCode(config);
        return GameState.createNew(secretCode, config);
    }

    /**
     * Processes a player's guess and returns the updated game state.
     * Validates input and evaluates guess against the secret code.
     * 
     * @param currentState the current game state
     * @param playerGuess the player's guess as a string (e.g., "1 2 3 4")
     * @return a new GameState reflecting the result of this guess
     * @throws IllegalArgumentException if the guess format is invalid
     */
    public GameState processGuess(GameState currentState, String playerGuess) {
        if (currentState.isGameEnded()) {
            String gameStatus = currentState.hasPlayerWon() ? "won" : "lost";
            throw ErrorHandler.invalidGameState(
                    "process guess",
                    "game " + gameStatus,
                    "active game"
            );
        }

        ValidationResult validationResult = GameInputValidator.validateGuess(
                playerGuess,
                currentState.getCodeLength(),
                currentState.getMaxNumber()
        );

        if (!validationResult.isValid()) {
            logger.debug("Invalid guess input by user: {}", validationResult.getErrorMessage());
            throw new IllegalArgumentException(validationResult.getErrorMessage());
        }

        return currentState.withGuess(validationResult.getNumbers());
    }

    private String generateSecretCode(GameConfig config) {
        // Try the injected service first
        String code = randomNumberService.generate(
                config.getCodeLength(),
                0,
                config.getMaxNumber()
        );

        // Fallback to Math.random if injected service fails
        if (code == null) {
            RandomNumberService fallbackService = new MathRandomService();
            code = fallbackService.generate(
                    config.getCodeLength(),
                    0,
                    config.getMaxNumber()
            );
        }

        return code;
    }

    /**
     * Validates a player's guess format without processing it.
     * Primarily used for testing.
     * 
     * @param playerGuess the player's guess as a string
     * @param gameState the current game state for validation context
     * @return true if the guess format is valid, false otherwise
     */
    public boolean isValidGuess(String playerGuess, GameState gameState) {
        ValidationResult result = GameInputValidator.validateGuess(playerGuess, gameState.getCodeLength(), gameState.getMaxNumber());
        return result.isValid();
    }
}
