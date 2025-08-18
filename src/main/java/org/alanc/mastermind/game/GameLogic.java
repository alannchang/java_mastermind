package org.alanc.mastermind.game;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.random.MathRandomService;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameLogic {
    private static final Logger logger = LoggerFactory.getLogger(GameLogic.class);

    private final RandomNumberService randomNumberService;

    public GameLogic(RandomNumberService randomNumberService) {
        this.randomNumberService = randomNumberService;
    }

    public GameState createNewGame(GameConfig config) {
        String secretCode = generateSecretCode(config);
        return GameState.createNew(secretCode, config);
    }

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

    public boolean isValidGuess(String playerGuess, GameState gameState) {
        ValidationResult result = GameInputValidator.validateGuess(playerGuess, gameState.getCodeLength(), gameState.getMaxNumber());
        return result.isValid();
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
}
