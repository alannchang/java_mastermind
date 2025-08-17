package org.alanc.mastermind.ui;

import org.alanc.mastermind.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

import static org.alanc.mastermind.ui.GameText.*;
import static org.alanc.mastermind.util.Utils.readNumberInRange;

public class EndgameUI {
    private static final Logger logger = LoggerFactory.getLogger(EndgameUI.class);

    public static boolean show(Scanner scanner) {
        while (true) {
            printUI(Messages.ENDGAME_MENU);

            try {
                int selection = readNumberInRange(scanner, "Please enter a number: ", 1, 2);
                logger.info("User selected end game option: {}", selection);

                switch(selection) {
                    case 1 -> {
                        logger.info("User chose to play again");
                        return true;
                    }
                    case 2 -> {
                        logger.info("User chose to return to main menu");
                        return false;
                    }
                    default -> {
                        System.out.println("Invalid entry, please try again.");
                        logger.debug("Invalid selection in end game menu: {}", selection);
                    }
                }
            } catch (RuntimeException e) {
                ErrorHandler.handleUIError(
                        logger,
                        "end game menu",
                        e,
                        "Returning to main menu by default"
                );
                return false; // Default to returning to main menu
            }
        }
    }

    private EndgameUI() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
