package org.alanc.mastermind.manager;

import org.alanc.mastermind.logic.GameLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameManager {
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);

    private final GameLogic gameLogic;

    public GameManager () {
        this.gameLogic = new GameLogic();
    }

    public void startApplication() {
        gameLogic.startNewGame();

    }


}
