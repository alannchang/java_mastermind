package org.alanc.mastermind.manager;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.random.TestRandomNumberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameManager Tests")
class GameManagerTest {

    private GameManager gameManager;
    private TestRandomNumberService testRandomService;

    @BeforeEach
    void setUp() {
        testRandomService = new TestRandomNumberService("1 2 3 4");
        gameManager = new GameManager(testRandomService);
    }

    @Nested
    @DisplayName("Configuration Management")
    class ConfigurationTests {

        @Test
        @DisplayName("Should initialize with default configuration")
        void testDefaultConfiguration() {
            GameConfig config = gameManager.getCurrentConfig();

            assertEquals(10, config.getMaxAttempts());
            assertEquals(4, config.getCodeLength());
            assertEquals(7, config.getMaxNumber());
        }

        @Test
        @DisplayName("Should update configuration immutably")
        void testConfigurationUpdates() {
            GameConfig originalConfig = gameManager.getCurrentConfig();
            
            gameManager.updateMaxAttempts(15);
            GameConfig updatedConfig = gameManager.getCurrentConfig();

            // Original unchanged, new config has changes
            assertEquals(10, originalConfig.getMaxAttempts());
            assertEquals(15, updatedConfig.getMaxAttempts());
            assertEquals(4, updatedConfig.getCodeLength()); // Preserved
        }

        @Test
        @DisplayName("Should reset to defaults")
        void testResetToDefaults() {
            gameManager.updateMaxAttempts(20);
            gameManager.resetConfigToDefaults();
            
            GameConfig config = gameManager.getCurrentConfig();
            assertEquals(10, config.getMaxAttempts());
            assertEquals(4, config.getCodeLength());
            assertEquals(7, config.getMaxNumber());
        }
    }

    @Nested
    @DisplayName("Resource Management")
    class ResourceManagementTests {

        @Test
        @DisplayName("Should close resources safely")
        void testResourceClosure() {
            assertDoesNotThrow(() -> gameManager.close());
        }

        @Test
        @DisplayName("Should handle multiple close calls")
        void testMultipleCloseCalls() {
            assertDoesNotThrow(() -> {
                gameManager.close();
                gameManager.close();
            });
        }
    }
}