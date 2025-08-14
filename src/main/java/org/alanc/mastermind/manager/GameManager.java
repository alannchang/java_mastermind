package org.alanc.mastermind.manager;

import org.alanc.mastermind.logic.GameLogic;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.random.RandomOrgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameManager {
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);

    private final GameLogic gameLogic;
    private RandomNumberService randomNumberService;


    public GameManager () {
        this.randomNumberService = new RandomOrgService();
        this.gameLogic = new GameLogic(randomNumberService);
    }

    public void startApplication() {
        logger.info("Application started...");
        gameLogic.startNewGame();

    }


}
