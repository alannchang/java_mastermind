package org.alanc.mastermind;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.manager.GameManager;
import org.alanc.mastermind.random.QuotaChecker;
import org.alanc.mastermind.random.RandomOrgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Starting application");

        // handle Ctrl+D and cleanup resources
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Shutdown hook triggered - application terminating gracefully");
            try {
                QuotaChecker.shutdown();
                logger.debug("QuotaChecker HTTP client shut down successfully");
            } catch (Exception e) {
                logger.warn("Error shutting down QuotaChecker", e);
            }
            System.out.println("Closing application -- Goodbye!");
        }));

        try (GameManager gameManager = new GameManager(new RandomOrgService())) {
            gameManager.launch();
        } catch (Exception e) {
            logger.error("An unexpected error occurred during application execution", e);
            System.err.println("An error occurred: " + e.getMessage());
        }

        logger.info("Mastermind application terminated");
    }
}