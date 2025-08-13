package org.alanc.mastermind;

import org.alanc.mastermind.manager.GameManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting Mastermind game");

        GameManager gameManager = new GameManager();
        gameManager.startApplication();
    }
}