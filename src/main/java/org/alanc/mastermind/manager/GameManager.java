package org.alanc.mastermind.manager;

import org.alanc.mastermind.config.GameConfigDTO;
import org.alanc.mastermind.logic.GameLogic;
import org.alanc.mastermind.logic.GameState;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.random.RandomOrgService;
import org.alanc.mastermind.random.QuotaChecker;
import org.alanc.mastermind.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import org.alanc.mastermind.util.Utils;


public class GameManager implements AutoCloseable{
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);

    private final GameLogic gameLogic;
    private final Scanner scanner;
    private RandomNumberService randomNumberService;
    private GameConfigDTO currentConfig;


    public GameManager () {
        this.randomNumberService = new RandomOrgService();
        this.gameLogic = new GameLogic(randomNumberService);
        this.scanner = new Scanner(System.in);
        this.currentConfig = new GameConfigDTO();
    }

    public void startApplication() {
        logger.info("Application started...");
        GameState gameState = gameLogic.createNewGame(currentConfig);
        playOneRound(gameState);
    }

    private void playOneRound(GameState gameState) {
        showWelcomeMessage(gameState.getMaxAttempts(), gameState.getCodeLength(), gameState.getMaxNumber());

        while(!gameState.isGameEnded()) {
            String playerGuess = Utils.readLine(scanner, "What is the secret code? ");

            try {
                gameState = gameLogic.processGuess(gameState, playerGuess);

                // Show feedback for the most recent guess
                if (!gameState.getGuessHistory().isEmpty()) {
                    GameState.GuessResult latestGuess =
                            gameState.getGuessHistory().get(gameState.getGuessHistory().size() - 1);
                    if (!latestGuess.isAllCorrect()) {
                        System.out.println(latestGuess.provideFeedback());
                        System.out.printf("Attempts Remaining: %d\n", gameState.getAttemptsRemaining());
                    }
                }
            } catch (IllegalArgumentException e) {
                ErrorHandler.handleInputValidationError(logger, playerGuess, e.getMessage());
            }
        }
    }

    private void showWelcomeMessage(int maxAttempts, int codeLength, int maxNumber) {
        System.out.println("WELCOME TO MASTERMIND!");
        System.out.printf("You have %d chances to guess the secret code.", maxAttempts);
        System.out.printf("The secret code consists of %d integers from %d to %d.\n",
                codeLength, 0, maxNumber);
        System.out.println("When entering your guess, please separate each integer with a single space.");
    }

    @Override
    public void close() {
        logger.debug("Closing GameManager resources");
        
        closeScanner();
        closeRandomOrgService();
        closeQuotaChecker();
    }

    private void closeScanner() {
        try {
            if (scanner != null) {
                scanner.close();
                logger.debug("input scanner closed successfully");
            }
        } catch (Exception e) {
            ErrorHandler.handleResourceError(logger, "input scanner", e, false);
        }
    }

    private void closeRandomOrgService() {
        try {
            RandomOrgService.shutdown();
            logger.debug("RandomOrgService HTTP client closed successfully");
        } catch (Exception e) {
            ErrorHandler.handleResourceError(logger, "RandomOrgService HTTP client", e, false);
        }
    }

    private void closeQuotaChecker() {
        try {
            QuotaChecker.shutdown();
            logger.debug("QuotaChecker HTTP client closed successfully");
        } catch (Exception e) {
            ErrorHandler.handleResourceError(logger, "QuotaChecker HTTP client", e, false);
        }
    }

}
