package org.alanc.mastermind.logic;

import org.alanc.mastermind.config.GameConfigDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameState Tests")
class GameStateTest {

    private GameConfigDTO defaultConfig;
    private GameState initialState;

    @BeforeEach
    void setUp() {
        defaultConfig = new GameConfigDTO();
        initialState = GameState.createNew("1 2 3 4", defaultConfig);
    }

    @Nested
    @DisplayName("Game State Creation Tests")
    class GameStateCreationTests {

        @Test
        @DisplayName("Should create new game state correctly")
        void testCreateNewGameState() {
            assertNotNull(initialState);
            assertEquals("1 2 3 4", initialState.getSecretCode());
            assertArrayEquals(new int[]{1, 2, 3, 4}, initialState.getPlayerGuess());
            assertEquals(10, initialState.getAttemptsRemaining());
            assertEquals(0, initialState.getAttemptsMade());
            assertEquals(10, initialState.getMaxAttempts());
            assertEquals(4, initialState.getCodeLength());
            assertEquals(7, initialState.getMaxNumber());
            assertFalse(initialState.isGameEnded());
            assertFalse(initialState.hasPlayerWon());
            assertTrue(initialState.getGuessHistory().isEmpty());
        }

        @Test
        @DisplayName("Should create game state with custom configuration")
        void testCreateWithCustomConfig() {
            GameConfigDTO customConfig = new GameConfigDTO.Builder()
                    .maxAttempts(15)
                    .codeLength(6)
                    .maxNumber(10)
                    .build();

            GameState customState = GameState.createNew("1 2 3 4 5 6", customConfig);

            assertEquals("1 2 3 4 5 6", customState.getSecretCode());
            assertArrayEquals(new int[]{1, 2, 3, 4, 5, 6}, customState.getPlayerGuess());
            assertEquals(15, customState.getAttemptsRemaining());
            assertEquals(0, customState.getAttemptsMade());
            assertEquals(15, customState.getMaxAttempts());
            assertEquals(6, customState.getCodeLength());
            assertEquals(10, customState.getMaxNumber());
            assertFalse(initialState.isGameEnded());
            assertFalse(initialState.hasPlayerWon());
            assertTrue(initialState.getGuessHistory().isEmpty());
        }

        @Test
        @DisplayName("Should reject invalid secret code")
        void testRejectInvalidSecretCode() {
            assertThrows(IllegalArgumentException.class, () -> {
                GameState.createNew("invalid", defaultConfig);
            });

            assertThrows(IllegalArgumentException.class, () -> {
                GameState.createNew("1 2 3", defaultConfig); // Wrong length
            });

            assertThrows(IllegalArgumentException.class, () -> {
                GameState.createNew("1 2 3 9", defaultConfig); // Out of range
            });
        }
    }

    @Nested
    @DisplayName("Guess Processing Tests")
    class GuessProcessingTests {

        @Test
        @DisplayName("Should process exact match correctly")
        void testExactMatch() {
            GameState newState = initialState.withGuess(new int[]{1, 2, 3, 4});

            assertTrue(newState.isGameEnded());
            assertTrue(newState.hasPlayerWon());
            assertEquals(1, newState.getGuessHistory().size());
            assertEquals(9, newState.getAttemptsRemaining());

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertTrue(result.allCorrect());
            assertEquals(4, result.correctNumbers());
            assertEquals(4, result.correctLocations());
            assertEquals("1 2 3 4", result.guess());
        }

        @Test
        @DisplayName("Should process partial match correctly")
        void testPartialMatch() {
            // Secret: 1 2 3 4, Guess: 1 2 5 6 (2 correct positions, 2 correct numbers)
            GameState newState = initialState.withGuess(new int[]{1, 2, 5, 6});

            assertFalse(newState.isGameEnded());
            assertFalse(newState.hasPlayerWon());
            assertEquals(1, newState.getGuessHistory().size());
            assertEquals(9, newState.getAttemptsRemaining());

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertFalse(result.allCorrect());
            assertEquals(2, result.correctNumbers());
            assertEquals(2, result.correctLocations());
        }

        @Test
        @DisplayName("Should process no match correctly")
        void testNoMatch() {
            // Secret: 1 2 3 4, Guess: 0 5 6 7 (no matches)
            GameState newState = initialState.withGuess(new int[]{0, 5, 6, 7});

            assertFalse(newState.isGameEnded());
            assertFalse(newState.hasPlayerWon());

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertFalse(result.allCorrect());
            assertEquals(0, result.correctNumbers());
            assertEquals(0, result.correctLocations());
        }

        @Test
        @DisplayName("Should process wrong positions correctly")
        void testWrongPositions() {
            // Secret: 1 2 3 4, Guess: 4 3 2 1 (all numbers correct, all wrong positions)
            GameState newState = initialState.withGuess(new int[]{4, 3, 2, 1});

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertFalse(result.allCorrect());
            assertEquals(4, result.correctNumbers()); // All numbers are in secret
            assertEquals(0, result.correctLocations()); // None in correct position
        }

        @Test
        @DisplayName("Should handle duplicates correctly")
        void testDuplicates() {
            // Secret: 1 2 3 4, Guess: 1 1 1 1 (one correct position, one correct number)
            GameState newState = initialState.withGuess(new int[]{1, 1, 1, 1});

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertFalse(result.allCorrect());
            assertEquals(1, result.correctNumbers()); // Only one 1 in secret
            assertEquals(1, result.correctLocations()); // Only position 0 matches
        }

        @Test
        @DisplayName("Should reject guess on ended game")
        void testRejectGuessOnEndedGame() {
            // End the game with a winning guess
            GameState endedState = initialState.withGuess(new int[]{1, 2, 3, 4});

            assertThrows(IllegalStateException.class, () -> {
                endedState.withGuess(new int[]{3, 4, 5, 6});
            });
        }

        @Test
        @DisplayName("Should end game when attempts run out")
        void testGameEndsWhenAttemptsRunOut() {
            GameState currentState = initialState;

            // Make 10 incorrect guesses (using valid numbers 0-7)
            for (int i = 0; i < 10; i++) {
                currentState = currentState.withGuess(new int[]{3, 4, 5, 6});

                if (i < 9) {
                    assertFalse(currentState.isGameEnded(), "Game should not end before 10 attempts");
                }
            }

            assertTrue(currentState.isGameEnded());
            assertFalse(currentState.hasPlayerWon());
            assertEquals(0, currentState.getAttemptsRemaining());
            assertEquals(10, currentState.getAttemptsMade());
        }
    }

    @Nested
    @DisplayName("Guess Evaluation Logic Tests")
    class GuessEvaluationTests {

        @Test
        @DisplayName("Should handle mixed correct positions and wrong positions")
        void testMixedCorrectAndWrongPositions() {
            // Secret: 1 2 3 4, Guess: 1 3 2 4
            // Position 0: correct (1), Position 1: wrong but number exists (3->2)
            // Position 2: wrong but number exists (2->3), Position 3: correct (4)
            GameState newState = initialState.withGuess(new int[]{1, 3, 2, 4});

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertEquals(4, result.correctNumbers()); // All numbers exist in secret
            assertEquals(2, result.correctLocations()); // Positions 0 and 3 correct
        }

        @Test
        @DisplayName("Should handle repeated numbers in secret")
        void testRepeatedNumbersInSecret() {
            GameState stateWithRepeats = GameState.createNew("1 1 2 2", defaultConfig);

            // Guess: 1 2 1 2 (all numbers correct, but mixed positions)
            // Secret: 1 1 2 2, Guess: 1 2 1 2
            // Position 0: correct (1=1), Position 1: wrong (1≠2), Position 2: wrong (2≠1), Position 3: correct (2=2)
            GameState newState = stateWithRepeats.withGuess(new int[]{1, 2, 1, 2});

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertEquals(4, result.correctNumbers()); // All numbers exist in secret
            assertEquals(2, result.correctLocations()); // Positions 0 and 3 are correct
        }

        @Test
        @DisplayName("Should handle edge case with all same numbers")
        void testAllSameNumbers() {
            GameState sameNumberState = GameState.createNew("2 2 2 2", defaultConfig);

            // Test with partial match
            GameState newState = sameNumberState.withGuess(new int[]{2, 2, 5, 5});

            GameState.GuessResult result = newState.getGuessHistory().get(0);
            assertEquals(2, result.correctNumbers()); // Only 2 correct numbers (not 4)
            assertEquals(2, result.correctLocations()); // Positions 0 and 1
        }
    }

    @Nested
    @DisplayName("GuessResult Tests")
    class GuessResultTests {

        @Test
        @DisplayName("Should format feedback correctly for perfect guess")
        void testProvideFeedbackPerfect() {
            GameState.GuessResult perfectResult = new GameState.GuessResult("1 2 3 4", 4, 4, true);
            assertEquals("You guessed all the numbers correctly!", perfectResult.provideFeedback());
        }

        @Test
        @DisplayName("Should format feedback correctly for no matches")
        void testProvideFeedbackNoMatch() {
            GameState.GuessResult noMatchResult = new GameState.GuessResult("5 6 7 8", 0, 0, false);
            assertEquals("All incorrect.", noMatchResult.provideFeedback());
        }

        @Test
        @DisplayName("Should format feedback correctly for partial matches")
        void testProvideFeedbackPartial() {
            GameState.GuessResult partialResult = new GameState.GuessResult("1 2 5 6", 2, 2, false);
            assertEquals("2 correct numbers and 2 correct locations.", partialResult.provideFeedback());
        }

        @Test
        @DisplayName("Should handle singular/plural correctly in feedback")
        void testSingularPluralInFeedback() {
            GameState.GuessResult singularResult = new GameState.GuessResult("1 5 6 7", 1, 1, false);
            assertEquals("1 correct number and 1 correct location.", singularResult.provideFeedback());

            GameState.GuessResult mixedResult = new GameState.GuessResult("1 3 2 6", 3, 1, false);
            assertEquals("3 correct numbers and 1 correct location.", mixedResult.provideFeedback());
        }

        @Test
        @DisplayName("Should have correct guess result properties")
        void testGuessResultProperties() {
            GameState.GuessResult result = new GameState.GuessResult("1 2 3 5", 3, 3, false);

            assertEquals("1 2 3 5", result.guess());
            assertEquals(3, result.correctNumbers());
            assertEquals(3, result.correctLocations());
            assertFalse(result.allCorrect());
        }
    }

    @Nested
    @DisplayName("Game Progress Tracking Tests")
    class GameProgressTests {

        @Test
        @DisplayName("Should track attempts correctly")
        void testAttemptsTracking() {
            assertEquals(10, initialState.getAttemptsRemaining());
            assertEquals(0, initialState.getAttemptsMade());

            GameState afterGuess = initialState.withGuess(new int[]{3, 4, 5, 6});
            assertEquals(9, afterGuess.getAttemptsRemaining());
            assertEquals(1, afterGuess.getAttemptsMade());
        }

        @Test
        @DisplayName("Should maintain guess history order")
        void testGuessHistoryOrder() {
            GameState state = initialState;

            state = state.withGuess(new int[]{3, 4, 5, 6});
            state = state.withGuess(new int[]{1, 2, 5, 6});
            state = state.withGuess(new int[]{1, 2, 3, 5});

            assertEquals(3, state.getGuessHistory().size());
            assertEquals("3 4 5 6", state.getGuessHistory().get(0).guess());
            assertEquals("1 2 5 6", state.getGuessHistory().get(1).guess());
            assertEquals("1 2 3 5", state.getGuessHistory().get(2).guess());
        }

        @Test
        @DisplayName("Should correctly identify game end conditions")
        void testGameEndConditions() {
            // Test win condition
            GameState winState = initialState.withGuess(new int[]{1, 2, 3, 4});
            assertTrue(winState.isGameEnded());
            assertTrue(winState.hasPlayerWon());

            // Test lose condition (run out of attempts)
            GameState loseState = initialState;
            for (int i = 0; i < 10; i++) {
                loseState = loseState.withGuess(new int[]{7, 7, 7, 7});
            }
            assertTrue(loseState.isGameEnded());
            assertFalse(loseState.hasPlayerWon());
        }
    }
}
