package org.alanc.mastermind.manager;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.game.GameSession;
import org.alanc.mastermind.game.GameLogic;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.ui.GameUI;
import org.alanc.mastermind.util.ErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

public class GameManager implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);

    private final GameLogic gameLogic;
    private final Scanner scanner;
    private final RandomNumberService randomNumberService;
    private GameConfig currentConfig;

    public GameManager(RandomNumberService randomNumberService) {
        logger.info("Initializing GameManager");

        this.randomNumberService = randomNumberService;
        this.gameLogic = new GameLogic(randomNumberService);
        this.scanner = new Scanner(System.in);
        this.currentConfig = GameConfig.defaults();

        logger.debug("GameManager initialized with {} service", randomNumberService.getClass().getSimpleName());
    }

    // Application lifecycle
    public void launch() {
        GameUI.showMainMenu(scanner, this);
    }

    // Configuration management
    public GameConfig getCurrentConfig() {
        return currentConfig;
    }

    public void updateMaxAttempts(int maxAttempts) {
        this.currentConfig = GameConfig.Builder.from(currentConfig)
            .maxAttempts(maxAttempts)
            .build();
        logger.info("Updated max attempts to: {}", maxAttempts);
    }

    public void updateCodeLength(int codeLength) {
        this.currentConfig = GameConfig.Builder.from(currentConfig)
            .codeLength(codeLength)
            .build();
        logger.info("Updated code length to: {}", codeLength);
    }

    public void updateMaxNumber(int maxNumber) {
        this.currentConfig = GameConfig.Builder.from(currentConfig)
            .maxNumber(maxNumber)
            .build();
        logger.info("Updated max number to: {}", maxNumber);
    }

    public void resetConfigToDefaults() {
        this.currentConfig = GameConfig.defaults();
        logger.info("Reset configuration to defaults");
    }

    // Game session delegation
    public void startNewGame() {
        logger.info("Starting new game session");
        
        GameSession gameSession = new GameSession(gameLogic, scanner);
        gameSession.play(currentConfig);
    }

    // Resource management
    @Override
    public void close() {
        logger.debug("Closing GameManager resources");
        
        closeResource("scanner", scanner);
        closeResource("random number service", randomNumberService);
    }

    private void closeResource(String resourceName, AutoCloseable resource) {
        try {
            if (resource != null) {
                resource.close();
                logger.debug("{} closed successfully", resourceName);
            }
        } catch (Exception e) {
            ErrorHandler.handleResourceError(logger, resourceName, e, false);
        }
    }
}