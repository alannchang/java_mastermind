package org.alanc.mastermind.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameInputValidator Tests")
class GameInputValidatorTest {

    @Nested
    @DisplayName("Input Validation")
    class ValidateGuessTests {

        @Test
        @DisplayName("Should accept valid input")
        void testValidInput() {
            ValidationResult result = GameInputValidator.validateGuess("1 2 3 4", 4, 7);

            assertTrue(result.isValid());
            assertArrayEquals(new int[]{1, 2, 3, 4}, result.getNumbers());
        }

        @Test
        @DisplayName("Should handle whitespace variations")
        void testWhitespaceHandling() {
            ValidationResult result = GameInputValidator.validateGuess("  1  2   3    4  ", 4, 7);

            assertTrue(result.isValid());
            assertArrayEquals(new int[]{1, 2, 3, 4}, result.getNumbers());
        }

        @Test
        @DisplayName("Should reject invalid input formats")
        void testInvalidFormats() {
            // Null/empty
            assertFalse(GameInputValidator.validateGuess(null, 4, 7).isValid());
            assertFalse(GameInputValidator.validateGuess("", 4, 7).isValid());
            
            // Wrong length
            assertFalse(GameInputValidator.validateGuess("1 2 3", 4, 7).isValid());
            assertFalse(GameInputValidator.validateGuess("1 2 3 4 5", 4, 7).isValid());
            
            // Non-numeric
            assertFalse(GameInputValidator.validateGuess("1 a 3 4", 4, 7).isValid());
            
            // Out of range
            assertFalse(GameInputValidator.validateGuess("-1 2 3 4", 4, 7).isValid());
            assertFalse(GameInputValidator.validateGuess("1 2 3 8", 4, 7).isValid());
        }

        @Test
        @DisplayName("Should work with custom configurations")
        void testCustomConfigurations() {
            ValidationResult result = GameInputValidator.validateGuess("0 1 2", 3, 2);

            assertTrue(result.isValid());
            assertArrayEquals(new int[]{0, 1, 2}, result.getNumbers());
        }
    }

    @Nested
    @DisplayName("Array Formatting")
    class ArrayFormattingTests {

        @Test
        @DisplayName("Should format arrays to strings correctly")
        void testArrayToString() {
            assertEquals("1 2 3 4", GameInputValidator.intArrayToString(new int[]{1, 2, 3, 4}));
            assertEquals("0", GameInputValidator.intArrayToString(new int[]{0}));
            assertEquals("", GameInputValidator.intArrayToString(new int[0]));
        }
    }
}