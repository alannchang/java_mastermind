package org.alanc.mastermind.ui;

import org.alanc.mastermind.manager.GameManager;
import org.alanc.mastermind.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import static org.alanc.mastermind.ui.GameText.*;
import static org.alanc.mastermind.util.Utils.*;

public class MainMenuUI {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuUI.class);

    public static void show(Scanner scanner, GameManager gameManager) {
        while (true) {
            printUI(Messages.STARTUP_BANNER);
            printUI(Messages.MAIN_MENU);

            try {
                int userSelection = readNumberInRange(scanner, "Please enter a number: ", 1, 5);
                logger.info("User selected main menu option: {}", userSelection);

                printUI(GameText.Messages.SEPARATOR);

                switch (userSelection) {
                    case 1 -> {
                        logger.info("Starting new game from main menu");
                        gameManager.startNewGame();
                    }
                    case 2 -> {
                        logger.info("Entering options menu");
                        OptionsMenuUI.show(scanner, gameManager);
                    }
                    case 3 -> {
                        logger.info("Showing about screen");
                        AboutUI.show(scanner);
                    }
                    case 4 -> {
                        logger.info("User chose to quit from main menu");
                        GameUI.quit();
                    }
                }
            } catch (RuntimeException e) {
                ErrorHandler.handleUIError(
                        logger,
                        "main menu",
                        e,
                        "Returning to main menu"
                );
                return; // Exit main menu
            }
        }
    }

    // Private constructor to prevent instantiation
    private MainMenuUI() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
