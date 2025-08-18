package org.alanc.mastermind.logic;

import org.alanc.mastermind.config.GameConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameState Tests")
class GameStateTest {

    private GameConfig defaultConfig;
    private GameState initialState;

    @BeforeEach
    void setUp() {
        defaultConfig = GameConfig.defaults();
        initialState = GameState.createNew("1 2 3 4", defaultConfig);
    }

    @Nested
    @DisplayName("Game State Creation")
    class GameStateCreationTests {

        @Test
        @DisplayName("Should create new game state with correct initial values")
        void testCreateNewGameState() {
            assertEquals("1 2 3 4", initialState.getSecretCode());
            assertArrayEquals(new int[]{1, 2, 3, 4}, initialState.getSecretCodeNumbers());
            assertEquals(10, initialState.getAttemptsRemaining());
            assertFalse(initialState.isGameEnded());
            assertTrue(initialState.getGuessHistory().isEmpty());
        }

        @Test
        @DisplayName("Should reject invalid secret code")
        void testRejectInvalidSecretCode() {
            assertThrows(IllegalArgumentException.class, () -> {
                GameState.createNew("1 2 3", defaultConfig); // Wrong length
            });

            assertThrows(IllegalArgumentException.class, () -> {
                GameState.createNew("1 2 3 9", defaultConfig); // Out of range
            });
        }
    }

    @Nested
    @DisplayName("Guess Processing")
    class GuessProcessingTests {

        @Test
        @DisplayName("Should process exact match correctly")
        void testExactMatch() {
            GameState newState = initialState.withGuess(new int[]{1, 2, 3, 4});

            assertTrue(newState.isGameEnded());
            assertTrue(newState.hasPlayerWon());
            assertEquals(1, newState.getGuessHistory().size());

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertTrue(result.allCorrect());
            assertEquals(4, result.correctNumbers());
            assertEquals(4, result.correctLocations());
        }

        @Test
        @DisplayName("Should process partial match correctly")
        void testPartialMatch() {
            // Secret: 1 2 3 4, Guess: 1 2 5 6 (2 correct positions, 2 correct numbers)
            GameState newState = initialState.withGuess(new int[]{1, 2, 5, 6});

            assertFalse(newState.isGameEnded());
            assertEquals(9, newState.getAttemptsRemaining());

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertEquals(2, result.correctNumbers());
            assertEquals(2, result.correctLocations());
        }

        @Test
        @DisplayName("Should handle complex scoring with duplicates")
        void testDuplicateHandling() {
            // Secret: 1 2 3 4, Guess: 1 1 1 1 (one correct position, one correct number)
            GameState newState = initialState.withGuess(new int[]{1, 1, 1, 1});

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertEquals(1, result.correctNumbers()); // Only one 1 in secret
            assertEquals(1, result.correctLocations()); // Only position 0 matches
        }

        @Test
        @DisplayName("Should reject guess on ended game")
        void testRejectGuessOnEndedGame() {
            GameState endedState = initialState.withGuess(new int[]{1, 2, 3, 4});

            assertThrows(IllegalStateException.class, () -> {
                endedState.withGuess(new int[]{3, 4, 5, 6});
            });
        }
    }

    @Nested
    @DisplayName("Game Flow")
    class GameFlowTests {

        @Test
        @DisplayName("Should end game when attempts run out")
        void testGameEndsWhenAttemptsRunOut() {
            GameState currentState = initialState;

            // Make 10 incorrect guesses
            for (int i = 0; i < 10; i++) {
                currentState = currentState.withGuess(new int[]{3, 4, 5, 6});
            }

            assertTrue(currentState.isGameEnded());
            assertFalse(currentState.hasPlayerWon());
            assertEquals(0, currentState.getAttemptsRemaining());
            assertEquals(10, currentState.getGuessHistory().size());
        }

        @Test
        @DisplayName("Should provide correct feedback messages")
        void testFeedbackMessages() {
            GameState.GuessResult perfectResult = new GameState.GuessResult("1 2 3 4", 4, 4, true);
            assertEquals("You guessed all the numbers correctly!", perfectResult.provideFeedback());

            GameState.GuessResult noMatchResult = new GameState.GuessResult("5 6 7 0", 0, 0, false);
            assertEquals("All incorrect.", noMatchResult.provideFeedback());

            GameState.GuessResult partialResult = new GameState.GuessResult("1 2 5 6", 2, 2, false);
            assertEquals("2 correct numbers and 2 correct locations.", partialResult.provideFeedback());
        }
    }
}