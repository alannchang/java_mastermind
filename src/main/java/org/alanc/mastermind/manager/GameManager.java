package org.alanc.mastermind.manager;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.logic.GameLogic;
import org.alanc.mastermind.logic.GameState;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.random.RandomOrgService;
import org.alanc.mastermind.random.QuotaChecker;
import org.alanc.mastermind.ui.GameUI;
import org.alanc.mastermind.util.ErrorHandler;
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

    public GameManager () {
        logger.info("Initializing GameManager");

        this.randomNumberService = new RandomOrgService();
        this.gameLogic = new GameLogic(randomNumberService);
        this.scanner = new Scanner(System.in);
        this.currentConfig = new GameConfig();

        logger.debug("GameManager initialized");
        logger.debug("Random Service set to {}", randomNumberService.getClass().getSimpleName());
        logger.debug("Config set to default: {} attempts, {} code length, max number {},",
                currentConfig.getMaxAttempts(), currentConfig.getCodeLength(), currentConfig.getMaxNumber());
    }

    public void launch() {
        GameUI.showMainMenu(scanner, this);
    }

    public GameConfig getCurrentConfig() {
        return currentConfig;
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

            initialState = gameLogic.createNewGame(currentConfig);
        }
    }

    private GameState playOneRound(GameState gameState) {
        showWelcomeMessage(gameState.getMaxAttempts(), gameState.getCodeLength(), gameState.getMaxNumber());

        while(!gameState.isGameEnded()) {
            String playerGuess = Utils.readLine(scanner, "What is the secret code? ");

            try {
                gameState = gameLogic.processGuess(gameState, playerGuess);

                // Show feedback for the most recent guess
                if (!gameState.getGuessHistory().isEmpty()) {
                    GameState.GuessResult latestGuess =
                            gameState.getGuessHistory().get(gameState.getGuessHistory().size() - 1);
                    if (!latestGuess.allCorrect()) {
                        System.out.println(latestGuess.provideFeedback());
                        System.out.printf("Try Again. Attempts Remaining: %d\n", gameState.getAttemptsRemaining());
                    }
                }
            } catch (IllegalArgumentException e) {
                ErrorHandler.handleInputValidationError(logger, playerGuess, e.getMessage());
            }
        }
        return gameState;
    }

    private void showWelcomeMessage(int maxAttempts, int codeLength, int maxNumber) {
        System.out.println("WELCOME TO MASTERMIND!");
        System.out.printf("You have %d chances to guess the secret code.", maxAttempts);
        System.out.printf("The secret code consists of %d integers from %d to %d.\n",
                codeLength, 0, maxNumber);
        System.out.println("When entering your guess, please separate each integer with a single space.");
    }

    private void showEndGameMessage(GameState gameState) {
        printUI(Messages.SEPARATOR);

        if (gameState.hasPlayerWon()) {
            printUI(Messages.YOU_WIN_BANNER);
            System.out.printf("You guessed it! The code is %s.\n", gameState.getSecretCode());
        } else {
            printUI(Messages.YOU_LOSE_BANNER);
            System.out.printf("Sorry, the secret code was: %s.\n", gameState.getSecretCode());
        }
    }

    private boolean askToPlayAgain() {
        return GameUI.showEndGameMenu(scanner);
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
