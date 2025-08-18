package org.alanc.mastermind;

import org.alanc.mastermind.manager.GameManager;
import org.alanc.mastermind.random.QuotaChecker;
import org.alanc.mastermind.random.RandomOrgService;
import org.alanc.mastermind.util.GameTerminatedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main application class for Mastermind.
 * Handles application bootstrapping, resource management, and graceful shutdown.
 */
public class MastermindApplication {
    private static final Logger logger = LoggerFactory.getLogger(MastermindApplication.class);

    public void run() {
        logger.info("Starting Mastermind application");

        setupShutdownHook();

        try (GameManager gameManager = new GameManager(new RandomOrgService())) {
            gameManager.launch();
        } catch (GameTerminatedException e) {
            logger.info("Game terminated gracefully: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred during application execution", e);
            System.err.println("An error occurred: " + e.getMessage());
        }

        logger.info("Mastermind application terminated");
    }

    private void setupShutdownHook() {
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
    }
}
