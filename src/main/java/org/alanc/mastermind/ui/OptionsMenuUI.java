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
                int selection = readNumberInRange(scanner, "Please enter a number: ", 1, 7);
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
        GameConfig config = gameManager.getConfig();
        System.out.printf("Current attempts: %d\n", config.getMaxAttempts());
        int newAttempts = readNumberInRange(scanner, "Please enter number of attempts: ", 1, 100);
        
        if (newAttempts < 1 || newAttempts > 100) {
            throw ErrorHandler.invalidParameter(
                    "max attempts",
                    newAttempts,
                    "must be between 1 and 100"
            );
        }
        
        config.setMaxAttempts(newAttempts);
        logger.info("User changed max attempts to: {}", newAttempts);
        System.out.printf("Max attempts updated to: %d\n", newAttempts);
    }

    private static void changeCodeLength(Scanner scanner, GameManager gameManager) {
        GameConfig config = gameManager.getConfig();
        System.out.printf("Current secret code length: %d\n", config.getCodeLength());
        int newLength = readNumberInRange(scanner, "Please enter a number for code length: ", 1, 100);
        
        if (newLength < 1 || newLength > 100) {
            throw ErrorHandler.invalidParameter(
                    "code length",
                    newLength,
                    "must be between 1 and 100"
            );
        }
        
        config.setCodeLength(newLength);
        logger.info("User changed code length to: {}", newLength);
        System.out.printf("Code length updated to: %d\n", newLength);
    }

    private static void changeMaxNumber(Scanner scanner, GameManager gameManager) {
        GameConfig config = gameManager.getConfig();
        System.out.printf("Current secret code range: 0 to %d\n", config.getMaxNumber());
        int newMaxNumber = readNumberInRange(scanner, "Please enter max number for secret code: ", 1, 100);
        
        if (newMaxNumber < 0 || newMaxNumber > 100) {
            throw ErrorHandler.invalidParameter(
                    "max number",
                    newMaxNumber,
                    "must be between 0 and 100"
            );
        }
        if (newMaxNumber < 1) {
            throw ErrorHandler.invalidParameter(
                    "max number",
                    newMaxNumber,
                    "must be at least 1 to provide a valid range (0 to max)"
            );
        }
        
        config.setMaxNumber(newMaxNumber);
        logger.info("User changed max number to: {}", newMaxNumber);
        System.out.printf("Secret code range updated to: 0 to %d\n", newMaxNumber);
    }

    private static void resetToDefaults(Scanner scanner, GameManager gameManager) {
        GameConfig config = gameManager.getConfig();
        config.resetToDefaults();
        
        logger.info("User reset settings to defaults");
        System.out.println("Default settings restored!");

        readLine(scanner, "Press Enter to return to Main Menu");
    }

    private OptionsMenuUI() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
