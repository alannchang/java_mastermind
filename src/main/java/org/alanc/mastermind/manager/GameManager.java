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

/**
 * Manages the Mastermind application lifecycle and configuration.
 * Handles application startup/shutdown, configuration management, and resource cleanup.
 * Delegates game execution to GameSession.
 */
public class GameManager implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);

    private final GameLogic gameLogic;
    private final Scanner scanner;
    private final RandomNumberService randomNumberService;
    private GameConfig currentConfig;

    /**
     * Constructs a new GameManager with the specified random number service.
     * 
     * @param randomNumberService the service to use for generating secret codes
     */
    public GameManager(RandomNumberService randomNumberService) {
        logger.info("Initializing GameManager");

        this.randomNumberService = randomNumberService;
        this.gameLogic = new GameLogic(randomNumberService);
        this.scanner = new Scanner(System.in);
        this.currentConfig = GameConfig.defaults();

        logger.debug("GameManager initialized with {} service", randomNumberService.getClass().getSimpleName());
    }

    /** Launches the main application menu. */
    public void launch() {
        GameUI.showMainMenu(scanner, this);
    }

    /**
     * Gets the current game configuration.
     * 
     * @return the current GameConfig instance
     */
    public GameConfig getCurrentConfig() {
        return currentConfig;
    }

    /**
     * Updates the maximum number of attempts allowed.
     * 
     * @param maxAttempts the new maximum number of attempts
     */
    public void updateMaxAttempts(int maxAttempts) {
        this.currentConfig = GameConfig.Builder.from(currentConfig)
            .maxAttempts(maxAttempts)
            .build();
        logger.info("Updated max attempts to: {}", maxAttempts);
    }

    /**
     * Updates the length of the secret code.
     * 
     * @param codeLength the new code length
     */
    public void updateCodeLength(int codeLength) {
        this.currentConfig = GameConfig.Builder.from(currentConfig)
            .codeLength(codeLength)
            .build();
        logger.info("Updated code length to: {}", codeLength);
    }

    /**
     * Updates the maximum number value allowed in the secret code.
     * 
     * @param maxNumber the new maximum number value
     */
    public void updateMaxNumber(int maxNumber) {
        this.currentConfig = GameConfig.Builder.from(currentConfig)
            .maxNumber(maxNumber)
            .build();
        logger.info("Updated max number to: {}", maxNumber);
    }

    /** Resets the game configuration to default values. */
    public void resetConfigToDefaults() {
        this.currentConfig = GameConfig.defaults();
        logger.info("Reset configuration to defaults");
    }

    /** Starts a new game session with the current configuration. */
    public void startNewGame() {
        logger.info("Starting new game session");
        
        GameSession gameSession = new GameSession(gameLogic, scanner);
        gameSession.play(currentConfig);
    }

    /** Closes all resources managed by this GameManager. */
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