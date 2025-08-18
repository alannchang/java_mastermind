package org.alanc.mastermind.ui;

import org.alanc.mastermind.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

import static org.alanc.mastermind.ui.GameText.*;
import static org.alanc.mastermind.util.Utils.readNumberInRange;

/**
 * UI for handling resume game prompts when an incomplete game is found.
 */
public class ResumeGameUI {
    private static final Logger logger = LoggerFactory.getLogger(ResumeGameUI.class);

    /**
     * Shows the resume game menu and returns user's choice.
     * 
     * @param scanner the input scanner
     * @return true if user wants to resume, false if they want to start fresh
     */
    public static boolean show(Scanner scanner) {
        while (true) {
            printUI(Messages.RESUME_GAME_MENU);

            try {
                int selection = readNumberInRange(scanner, "Please enter a number: ", 1, 2);
                logger.info("User selected resume game option: {}", selection);

                switch(selection) {
                    case 1 -> {
                        logger.info("User chose to resume incomplete game");
                        return true;
                    }
                    case 2 -> {
                        logger.info("User chose to start a new game");
                        return false;
                    }
                    default -> {
                        System.out.println("Invalid entry, please try again.");
                        logger.debug("Invalid selection in resume game menu: {}", selection);
                    }
                }
            } catch (RuntimeException e) {
                ErrorHandler.handleUIError(
                        logger,
                        "resume game menu",
                        e,
                        "Starting new game by default"
                );
                return false; // Default to starting new game
            }
        }
    }

    private ResumeGameUI() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
