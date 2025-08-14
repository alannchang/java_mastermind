package org.alanc.mastermind.manager;

import org.alanc.mastermind.config.GameConfigDTO;
import org.alanc.mastermind.logic.GameLogic;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.random.RandomOrgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;


public class GameManager {
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);

    private final GameLogic gameLogic;
    private final Scanner scanner;
    private RandomNumberService randomNumberService;


    public GameManager () {
        this.randomNumberService = new RandomOrgService();
        this.gameLogic = new GameLogic(randomNumberService);
        this.scanner = new Scanner(System.in);
    }

    public void startApplication() {
        logger.info("Application started...");
        gameLogic.startNewGame();
    }

    private void showWelcomeMessage(GameConfigDTO config) {
        System.out.println("WELCOME TO MASTERMIND!");
        System.out.printf("You have %d chances to guess the secret code.", config.getMaxAttempts());
        System.out.printf("The secret code consists of %d integers from %d to %d.\n",
                config.getCodeLength(), 0, config.getCodeLength());
        System.out.println("When entering your guess, please separate each integer with a single space.");
    }


}
