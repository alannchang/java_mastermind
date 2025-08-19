package org.alanc.mastermind.ui;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.manager.GameManager;
import org.alanc.mastermind.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import static org.alanc.mastermind.ui.GameText.*;
import static org.alanc.mastermind.util.Utils.*;

public class OptionsMenuUI {
    private static final Logger logger = LoggerFactory.getLogger(OptionsMenuUI.class);

    public static void show(Scanner scanner, GameManager gameManager) {
        while (true) {
            printUI(Messages.OPTIONS_BANNER);
            printUI(Messages.OPTIONS_MENU);

            try {
                int selection = readNumberInRange(scanner, "Please enter a number: ", 1, 8);
                logger.info("User selected options menu option: {}", selection);

                switch (selection) {
                    case 1 -> changeMaxAttempts(scanner, gameManager);
                    case 2 -> changeCodeLength(scanner, gameManager);
                    case 3 -> changeMaxNumber(scanner, gameManager);
                    case 4 -> {
                        logger.info("User requested Random.org quota check");
                        QuotaUI.show(scanner);
                    }
                    case 5 -> resetToDefaults(scanner, gameManager);
                    case 6 -> {
                        logger.info("User requested game history view");
                        GameHistoryUI.show(scanner, gameManager);
                    }
                    case 7 -> {
                        logger.info("User requested to clear game history");
                        clearGameHistory(scanner, gameManager);
                    }
                    case 8 -> {
                        logger.info("User exiting options menu");
                        return;
                    }
                }
            } catch (RuntimeException e) {
                ErrorHandler.handleUIError(
                        logger,
                        "options menu",
                        e,
                        "Returning to main menu"
                );
                return; // Exit options menu
            }
            printUI(Messages.SEPARATOR);
        }
    }

    private static void changeMaxAttempts(Scanner scanner, GameManager gameManager) {
        GameConfig config = gameManager.getCurrentConfig();
        System.out.printf("Current attempts: %d\n", config.getMaxAttempts());
        int newAttempts = readNumberInRange(scanner, "Please enter number of attempts: ", 1, 100);
        
        gameManager.updateMaxAttempts(newAttempts);
        logger.info("User changed max attempts to: {}", newAttempts);
        System.out.printf("Max attempts updated to: %d\n", newAttempts);
    }

    private static void changeCodeLength(Scanner scanner, GameManager gameManager) {
        GameConfig config = gameManager.getCurrentConfig();
        System.out.printf("Current secret code length: %d\n", config.getCodeLength());
        int newLength = readNumberInRange(scanner, "Please enter a number for code length: ", 1, 100);
        
        gameManager.updateCodeLength(newLength);
        logger.info("User changed code length to: {}", newLength);
        System.out.printf("Code length updated to: %d\n", newLength);
    }

    private static void changeMaxNumber(Scanner scanner, GameManager gameManager) {
        GameConfig config = gameManager.getCurrentConfig();
        System.out.printf("Current secret code range: 0 to %d\n", config.getMaxNumber());
        int newMaxNumber = readNumberInRange(scanner, "Please enter max number for secret code: ", 1, 100);
        
        gameManager.updateMaxNumber(newMaxNumber);
        logger.info("User changed max number to: {}", newMaxNumber);
        System.out.printf("Secret code range updated to: 0 to %d\n", newMaxNumber);
    }

    private static void resetToDefaults(Scanner scanner, GameManager gameManager) {
        gameManager.resetConfigToDefaults();
        
        logger.info("User reset settings to defaults");
        System.out.println("Default settings restored!");

        readLine(scanner, "Press Enter to return to Options menu\n");
    }

    private static void clearGameHistory(Scanner scanner, GameManager gameManager) {
        System.out.println("Are you sure you want to clear all game history? This cannot be undone.");
        String confirmation = readLine(scanner, "Type 'yes' to confirm: ");
        
        if ("yes".equalsIgnoreCase(confirmation.trim())) {
            gameManager.clearGameHistory();
            logger.info("User cleared all game history");
            System.out.println("Game history cleared successfully!");
        } else {
            logger.info("User cancelled game history clearing");
            System.out.println("Game history clearing cancelled.");
        }
        
        readLine(scanner, "Press Enter to return to Options menu\n");
    }

    private OptionsMenuUI() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
