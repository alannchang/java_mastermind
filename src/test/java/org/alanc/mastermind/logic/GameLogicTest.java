package org.alanc.mastermind.logic;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.random.TestRandomNumberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameLogic Tests")
class GameLogicTest {

    private GameLogic gameLogic;
    private GameConfig defaultConfig;
    private TestRandomNumberService testRandomService;

    @BeforeEach
    void setUp() {
        testRandomService = new TestRandomNumberService("1 2 3 4");
        gameLogic = new GameLogic(testRandomService);
        defaultConfig = GameConfig.defaults();
    }

    @Nested
    @DisplayName("Game Creation")
    class GameCreationTests {

        @Test
        @DisplayName("Should create new game with injected random service")
        void testCreateNewGame() {
            GameState gameState = gameLogic.createNewGame(defaultConfig);

            assertEquals("1 2 3 4", gameState.getSecretCode());
            assertEquals(defaultConfig.getMaxAttempts(), gameState.getAttemptsRemaining());
            assertFalse(gameState.isGameEnded());
            assertTrue(gameState.getGuessHistory().isEmpty());
        }

        @Test
        @DisplayName("Should fallback to MathRandom when primary service fails")
        void testFallbackToMathRandom() {
            TestRandomNumberService failingService = new TestRandomNumberService("", true);
            GameLogic logicWithFailingService = new GameLogic(failingService);

            GameState gameState = logicWithFailingService.createNewGame(defaultConfig);

            assertNotNull(gameState.getSecretCode());
            String[] numbers = gameState.getSecretCode().split(" ");
            assertEquals(4, numbers.length);
        }
    }

    @Nested
    @DisplayName("Guess Processing")
    class GuessProcessingTests {

        private GameState gameState;

        @BeforeEach
        void setUpGameState() {
            gameState = gameLogic.createNewGame(defaultConfig);
        }

        @Test
        @DisplayName("Should process winning guess")
        void testProcessWinningGuess() {
            GameState newState = gameLogic.processGuess(gameState, "1 2 3 4");

            assertTrue(newState.isGameEnded());
            assertTrue(newState.hasPlayerWon());
            assertEquals(1, newState.getGuessHistory().size());
        }

        @Test
        @DisplayName("Should reject invalid guess format")
        void testProcessInvalidGuess() {
            assertThrows(IllegalArgumentException.class, () -> {
                gameLogic.processGuess(gameState, "invalid guess");
            });
        }

        @Test
        @DisplayName("Should reject processing guess on ended game")
        void testProcessGuessOnEndedGame() {
            GameState endedState = gameLogic.processGuess(gameState, "1 2 3 4");

            assertThrows(IllegalStateException.class, () -> {
                gameLogic.processGuess(endedState, "2 3 4 5");
            });
        }
    }

    @Nested
    @DisplayName("Input Validation")
    class ValidationTests {

        @Test
        @DisplayName("Should validate correct guess formats")
        void testValidGuessFormat() {
            GameState gameState = gameLogic.createNewGame(defaultConfig);
            
            assertTrue(gameLogic.isValidGuess("1 2 3 4", gameState));
            assertTrue(gameLogic.isValidGuess("  0  7  0  7  ", gameState));
        }

        @Test
        @DisplayName("Should reject invalid guess formats")
        void testInvalidGuessFormats() {
            GameState gameState = gameLogic.createNewGame(defaultConfig);
            
            assertFalse(gameLogic.isValidGuess("1 2 3", gameState)); // Too few
            assertFalse(gameLogic.isValidGuess("1 a 3 4", gameState)); // Non-numeric
            assertFalse(gameLogic.isValidGuess("1 2 3 9", gameState)); // Out of range
            assertFalse(gameLogic.isValidGuess(null, gameState)); // Null
        }
    }
}