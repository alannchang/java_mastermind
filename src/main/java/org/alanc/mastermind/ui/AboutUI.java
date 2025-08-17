package org.alanc.mastermind.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import static org.alanc.mastermind.ui.GameText.*;
import static org.alanc.mastermind.util.Utils.readLine;

public class AboutUI {
    private static final Logger logger = LoggerFactory.getLogger(AboutUI.class);

    public static void show(Scanner scanner) {
        logger.info("Displaying about information");

        printUI(Messages.ABOUT_BANNER);
        printUI(Messages.ABOUT_INFO);

        readLine(scanner, "Press Enter to return to Main Menu\n");

        logger.debug("User returned from about screen");
    }

    private AboutUI() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
