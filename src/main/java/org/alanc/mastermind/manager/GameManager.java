package org.alanc.mastermind.manager;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.logic.GameLogic;
import org.alanc.mastermind.logic.GameState;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.random.RandomOrgService;

import org.alanc.mastermind.ui.GameUI;
import org.alanc.mastermind.util.ErrorHandler;
import org.alanc.mastermind.util.GameTerminatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import org.alanc.mastermind.util.Utils;
import static org.alanc.mastermind.ui.GameText.*;

public class GameManager implements AutoCloseable{
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);

    private final GameLogic gameLogic;
    private final Scanner scanner;
    private RandomNumberService randomNumberService;
    private GameConfig currentConfig;

    public GameManager(RandomNumberService randomNumberService) {
        logger.info("Initializing GameManager");

        this.randomNumberService = randomNumberService;
        this.gameLogic = new GameLogic(randomNumberService);
        this.scanner = new Scanner(System.in);
        this.currentConfig = GameConfig.defaults();

        logger.debug("GameManager initialized");
        logger.debug("Random Service set to {}", randomNumberService.getClass().getSimpleName());
        logger.debug("Config set to: {} attempts, {} code length, max number {}",
                currentConfig.getMaxAttempts(), currentConfig.getCodeLength(), currentConfig.getMaxNumber());
    }

    public void launch() {
        GameUI.showMainMenu(scanner, this);
    }

    public GameConfig getCurrentConfig() {
        return currentConfig;
    }

    public void updateMaxAttempts(int maxAttempts) {
        this.currentConfig = GameConfig.Builder.from(currentConfig)
            .maxAttempts(maxAttempts)
            .build();
        logger.info("Updated max attempts to: {}", maxAttempts);
    }

    public void updateCodeLength(int codeLength) {
        this.currentConfig = GameConfig.Builder.from(currentConfig)
            .codeLength(codeLength)
            .build();
        logger.info("Updated code length to: {}", codeLength);
    }

    public void updateMaxNumber(int maxNumber) {
        this.currentConfig = GameConfig.Builder.from(currentConfig)
            .maxNumber(maxNumber)
            .build();
        logger.info("Updated max number to: {}", maxNumber);
    }

    public void resetConfigToDefaults() {
        this.currentConfig = GameConfig.defaults();
        logger.info("Reset configuration to defaults");
    }

    public void startNewGame() {
        logger.info("Starting new game with configuration: {} attempts, {} code length, {} max number",
                currentConfig.getMaxAttempts(), currentConfig.getCodeLength(), currentConfig.getMaxNumber());

        GameState gameState = gameLogic.createNewGame(currentConfig);
        playGame(gameState);
    }

    private void playGame(GameState initialState) {
        while (true) {
            GameState endState = playOneRound(initialState);

            showEndGameMessage(endState);

            if (!askToPlayAgain()) {
                return;  // returns to main menu
            }

            // Use the current configuration for replay
            initialState = gameLogic.createNewGame(currentConfig);
        }
    }

    private GameState playOneRound(GameState gameState) {
        GameUI.showWelcomeMessage(gameState.getMaxAttempts(), gameState.getCodeLength(), gameState.getMaxNumber());

        while(!gameState.isGameEnded()) {
            String playerGuess = null;
            try {
                playerGuess = Utils.readLine(scanner, "What is the secret code? ");

                gameState = gameLogic.processGuess(gameState, playerGuess);

                // Show feedback for the most recent guess
                if (!gameState.getGuessHistory().isEmpty()) {
                    GameState.GuessResult latestGuess =
                            gameState.getGuessHistory().get(gameState.getGuessHistory().size() - 1);
                    GameUI.showGuessResult(latestGuess, gameState.getAttemptsRemaining());
                }
            } catch (IllegalArgumentException e) {
                ErrorHandler.handleInputValidationError(logger, playerGuess, e.getMessage());
            } catch (GameTerminatedException e) {
                logger.info("Game terminated by user input: {}", e.getMessage());
                throw e; // Re-throw to be handled at higher level
            }
        }
        return gameState;
    }

    private void showEndGameMessage(GameState gameState) {
        GameUI.showEndGameMessage(gameState);
    }

    private boolean askToPlayAgain() {
        return GameUI.showEndGameMenu(scanner);
    }

    @Override
    public void close() {
        logger.debug("Closing GameManager resources");
        
        closeResource("scanner", scanner);
        closeResource("random number service", randomNumberService);
    }

    private void closeResource(String resourceName, AutoCloseable resource) {
        try {
            if (resource != null) {
                resource.close();
                logger.debug("{} closed successfully", resourceName);
            }
        } catch (Exception e) {
            ErrorHandler.handleResourceError(logger, resourceName, e, false);
        }
    }


}

