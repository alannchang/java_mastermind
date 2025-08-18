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
    @DisplayName("Resource Management Tests")
    class ResourceManagementTests {

        @Test
        @DisplayName("Should close resources safely")
        void testResourceClosure() {
            assertDoesNotThrow(() -> gameManager.close());
        }

        @Test
        @DisplayName("Should handle multiple close calls safely")
        void testMultipleCloseCalls() {
            assertDoesNotThrow(() -> {
                gameManager.close();
                gameManager.close();
                gameManager.close();
            });
        }
    }
}
