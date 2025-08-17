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
        defaultConfig = new GameConfig();
    }

    @Nested
    @DisplayName("Game Creation Tests")
    class GameCreationTests {

        @Test
        @DisplayName("Should create new game with random secret code")
        void testCreateNewGame() {
            GameState gameState = gameLogic.createNewGame(defaultConfig);

            assertNotNull(gameState);
            assertEquals("1 2 3 4", gameState.getSecretCode());
            assertEquals(defaultConfig.getMaxAttempts(), gameState.getAttemptsRemaining());
            assertFalse(gameState.isGameEnded());
            assertFalse(gameState.hasPlayerWon());
            assertTrue(gameState.getGuessHistory().isEmpty());
        }

        @Test
        @DisplayName("Should create game with custom configuration")
        void testCreateGameWithCustomConfig() {
            GameConfig customConfig = new GameConfig.Builder()
                    .maxAttempts(15)
                    .codeLength(6)
                    .maxNumber(10)
                    .build();

            TestRandomNumberService customService = new TestRandomNumberService("1 2 3 4 5 6");
            GameLogic customLogic = new GameLogic(customService);

            GameState gameState = customLogic.createNewGame(customConfig);

            assertNotNull(gameState);
            assertEquals("1 2 3 4 5 6", gameState.getSecretCode());
            assertEquals(15, gameState.getAttemptsRemaining());
            assertEquals(customConfig.getMaxAttempts(), gameState.getMaxAttempts());
            assertEquals(customConfig.getCodeLength(), gameState.getCodeLength());
            assertEquals(customConfig.getMaxNumber(), gameState.getMaxNumber());
        }

        @Test
        @DisplayName("Should fallback to MathRandom when primary service fails")
        void testFallbackToMathRandom() {
            TestRandomNumberService failingService = new TestRandomNumberService("", true);
            GameLogic logicWithFailingService = new GameLogic(failingService);

            GameState gameState = logicWithFailingService.createNewGame(defaultConfig);

            assertNotNull(gameState);
            assertNotNull(gameState.getSecretCode());
            assertFalse(gameState.getSecretCode().isEmpty());

            // Should have 4 numbers in valid range
            String[] numbers = gameState.getSecretCode().split(" ");
            assertEquals(4, numbers.length);
            for (String numberStr : numbers) {
                int number = Integer.parseInt(numberStr);
                assertTrue(number >= 0 && number <= 8);
            }
        }
    }

    @Nested
    @DisplayName("Guess Processing Tests")
    class GuessProcessingTests {

        private GameState gameState;

        @BeforeEach
        void setUpGameState() {
            gameState = gameLogic.createNewGame(defaultConfig);
        }

        @Test
        @DisplayName("Should process valid guess correctly")
        void testProcessValidGuess() {
            String guess = "1 2 3 5"; // Partially correct
            GameState newState = gameLogic.processGuess(gameState, guess);

            assertNotNull(newState);
            assertEquals(1, newState.getGuessHistory().size());
            assertEquals(9, newState.getAttemptsRemaining());
            assertFalse(newState.isGameEnded());

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertEquals(guess, result.guess());
            assertFalse(result.allCorrect());
        }

        @Test
        @DisplayName("Should process winning guess correctly")
        void testProcessWinningGuess() {
            String winningGuess = "1 2 3 4"; // Exact match
            GameState newState = gameLogic.processGuess(gameState, winningGuess);

            assertNotNull(newState);
            assertTrue(newState.isGameEnded());
            assertTrue(newState.hasPlayerWon());

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertTrue(result.allCorrect());
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
            // Create a game that's already ended
            GameState winningState = gameLogic.processGuess(gameState, "1 2 3 4");

            assertThrows(IllegalStateException.class, () -> {
                gameLogic.processGuess(winningState, "2 3 4 5");
            });
        }

        @Test
        @DisplayName("Should handle game ending when attempts run out")
        void testGameEndingWithoutWin() {
            GameState currentState = gameState;

            // Make 10 incorrect guesses (using valid numbers 0-7)
            for (int i = 0; i < 10; i++) {
                String guess = "3 4 5 6"; // Incorrect guess
                currentState = gameLogic.processGuess(currentState, guess);
            }

            assertTrue(currentState.isGameEnded());
            assertFalse(currentState.hasPlayerWon());
            assertEquals(0, currentState.getAttemptsRemaining());
            assertEquals(10, currentState.getGuessHistory().size());
        }
    }

    @Nested
    @DisplayName("Guess Validation Tests")
    class GuessValidationTests {

        @Test
        @DisplayName("Should validate correct guess format")
        void testValidGuessFormat() {
            assertTrue(gameLogic.isValidGuess("1 2 3 4", defaultConfig));
            assertTrue(gameLogic.isValidGuess("0 7 0 7", defaultConfig));
            assertTrue(gameLogic.isValidGuess("  1  2  3  4  ", defaultConfig));
        }

        @Test
        @DisplayName("Should reject invalid guess formats")
        void testInvalidGuessFormats() {
            assertFalse(gameLogic.isValidGuess("1 2 3", defaultConfig)); // Too few
            assertFalse(gameLogic.isValidGuess("1 2 3 4 5", defaultConfig)); // Too many
            assertFalse(gameLogic.isValidGuess("1 a 3 4", defaultConfig)); // Non-numeric
            assertFalse(gameLogic.isValidGuess("-1 2 3 4", defaultConfig)); // Negative
            assertFalse(gameLogic.isValidGuess("1 2 3 9", defaultConfig)); // Out of range
            assertFalse(gameLogic.isValidGuess("", defaultConfig)); // Empty
            assertFalse(gameLogic.isValidGuess(null, defaultConfig)); // Null
        }

        @Test
        @DisplayName("Should validate with custom configuration")
        void testValidationWithCustomConfig() {
            GameConfig customConfig = new GameConfig.Builder()
                    .codeLength(3)
                    .maxNumber(5)
                    .build();

            assertTrue(gameLogic.isValidGuess("0 3 5", customConfig));
            assertFalse(gameLogic.isValidGuess("0 3 6", customConfig)); // 6 > maxNumber
            assertFalse(gameLogic.isValidGuess("0 3", customConfig)); // Too short
        }
    }

    @Nested
    @DisplayName("Random Service Integration Tests")
    class RandomServiceIntegrationTests {

        @Test
        @DisplayName("Should use injected random service")
        void testUsesInjectedRandomService() {
            TestRandomNumberService customService = new TestRandomNumberService("7 6 5 4");
            GameLogic customLogic = new GameLogic(customService);

            GameState gameState = customLogic.createNewGame(defaultConfig);

            assertEquals("7 6 5 4", gameState.getSecretCode());
        }

        @Test
        @DisplayName("Should respect configuration parameters in random generation")
        void testRespectsConfigurationInGeneration() {
            // Create service that returns sequential numbers starting from min
            TestRandomNumberService parameterAwareService = new TestRandomNumberService("") {
                @Override
                public String generate(int quantity, int min, int max) {
                    StringBuilder result = new StringBuilder();
                    for (int i = 0; i < quantity; i++) {
                        if (i > 0) result.append(" ");
                        result.append(min + i); // Sequential from min
                    }
                    return result.toString();
                }
            };

            GameLogic customLogic = new GameLogic(parameterAwareService);

            GameConfig config = new GameConfig.Builder()
                    .codeLength(3)
                    .maxNumber(5)
                    .build();

            GameState gameState = customLogic.createNewGame(config);

            assertEquals("0 1 2", gameState.getSecretCode()); // Sequential from 0
        }

        @Test
        @DisplayName("Should handle null response from random service")
        void testHandlesNullFromRandomService() {
            TestRandomNumberService nullService = new TestRandomNumberService(null);
            GameLogic logicWithNullService = new GameLogic(nullService);

            // Fallback to MathRandom and not throw exception
            assertDoesNotThrow(() -> {
                GameState gameState = logicWithNullService.createNewGame(defaultConfig);
                assertNotNull(gameState.getSecretCode());
                assertFalse(gameState.getSecretCode().isEmpty());
            });
        }
    }
}