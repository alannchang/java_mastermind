package org.alanc.mastermind.ui;

import org.alanc.mastermind.logic.GameState;
import org.alanc.mastermind.manager.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import static org.alanc.mastermind.ui.GameText.*;

public class GameUI {
    private static final Logger logger = LoggerFactory.getLogger(GameUI.class);

    // Menu operations
    public static void showMainMenu(Scanner scanner, GameManager gameManager) {
        MainMenuUI.show(scanner, gameManager);
    }

    public static boolean showEndGameMenu(Scanner scanner) {
        return EndgameUI.show(scanner);
    }

    public static void showWelcomeMessage(int maxAttempts, int codeLength, int maxNumber) {
        System.out.println("WELCOME TO MASTERMIND!");
        System.out.printf("You have %d chances to guess the secret code.", maxAttempts);
        System.out.printf("The secret code consists of %d integers from %d to %d.\n",
                codeLength, 0, maxNumber);
        System.out.println("When entering your guess, please separate each integer with a single space.");
    }

    public static void showGuessResult(GameState.GuessResult result, int attemptsRemaining) {
        if (!result.allCorrect()) {
            System.out.println(result.provideFeedback());
            System.out.printf("Try Again. Attempts Remaining: %d\n", attemptsRemaining);
        }
    }

    public static void showEndGameMessage(GameState gameState) {
        printUI(Messages.SEPARATOR);

        if (gameState.hasPlayerWon()) {
            printUI(Messages.YOU_WIN_BANNER);
            System.out.printf("You guessed it! The code is %s.\n", gameState.getSecretCode());
        } else {
            printUI(Messages.YOU_LOSE_BANNER);
            System.out.printf("Sorry, the secret code was: %s.\n", gameState.getSecretCode());
        }
    }

    public static void quit() {
        printUI(Messages.QUIT_INFO);
        System.exit(0);
    }

    private GameUI() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
