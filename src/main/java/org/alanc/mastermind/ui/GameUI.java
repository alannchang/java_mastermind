package org.alanc.mastermind.ui;

import org.alanc.mastermind.manager.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import static org.alanc.mastermind.ui.GameText.*;

public class GameUI {
    private static final Logger logger = LoggerFactory.getLogger(GameUI.class);

    public static void showMainMenu(Scanner scanner, GameManager gameManager) {
        MainMenuUI.show(scanner, gameManager);
    }

    public static void showOptionsMenu(Scanner scanner, GameManager gameManager) {
        OptionsMenuUI.show(scanner, gameManager);
    }

    public static void showAbout(Scanner scanner, GameManager gameManager) {
        AboutUI.show(scanner);
    }

    public static void showGameResult(boolean playerWon) {
        if (playerWon) {
            logger.info("Displaying victory banner");
            printUI(Messages.YOU_WIN_BANNER);
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
