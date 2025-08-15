package org.alanc.mastermind.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class GameInputValidatorTest {

    @Nested
    @DisplayName("validateGuess() tests")
    class ValidateGuessTests {

        @Test
        @DisplayName("Should accept valid input with correct format")
        void testValidInput() {
            ValidationResult result = GameInputValidator.validateGuess("1 2 3 4", 4, 8);

            assertTrue(result.isValid());
            assertArrayEquals(new int[]{1, 2, 3, 4}, result.getNumbers());
        }

        @Test
        @DisplayName("Should accept input with edge case numbers (0 and max)")
        void testEdgeCaseNumbers() {
            ValidationResult result = GameInputValidator.validateGuess("0 8 0 8", 4, 8);

            assertTrue(result.isValid());
            assertArrayEquals(new int[]{0, 8, 0, 8}, result.getNumbers());
        }

        @Test
        @DisplayName("Should accept input with different code length")
        void testDifferentCodeLength() {
            ValidationResult result = GameInputValidator.validateGuess("1 2", 2, 8);

            assertTrue(result.isValid());
            assertArrayEquals(new int[]{1, 2}, result.getNumbers());
        }

        @Test
        @DisplayName("Should handle input with multiple spaces between numbers")
        void testMultipleSpaces() {
            ValidationResult result = GameInputValidator.validateGuess("1  2   3    4", 4, 8);

            assertTrue(result.isValid());
            assertArrayEquals(new int[]{1, 2, 3, 4}, result.getNumbers());
        }

        @Test
        @DisplayName("Should handle input with leading and trailing spaces")
        void testLeadingTrailingSpaces() {
            ValidationResult result = GameInputValidator.validateGuess("  1 2 3 4  ", 4, 8);

            assertTrue(result.isValid());
            assertArrayEquals(new int[]{1, 2, 3, 4}, result.getNumbers());
        }

        @Test
        @DisplayName("Should reject null input")
        void testNullInput() {
            ValidationResult result = GameInputValidator.validateGuess(null, 4, 8);

            assertFalse(result.isValid());
            assertTrue(result.getErrorMessage().contains("Please enter your guess"));
        }

        @Test
        @DisplayName("Should reject empty input")
        void testEmptyInput() {
            ValidationResult result = GameInputValidator.validateGuess("", 4, 8);

            assertFalse(result.isValid());
            assertTrue(result.getErrorMessage().contains("Please enter your guess"));
        }

        @Test
        @DisplayName("Should reject whitespace-only input")
        void testWhitespaceOnlyInput() {
            ValidationResult result = GameInputValidator.validateGuess("   ", 4, 8);

            assertFalse(result.isValid());
            assertTrue(result.getErrorMessage().contains("Please enter your guess"));
        }

        @Test
        @DisplayName("Should reject input with too few numbers")
        void testTooFewNumbers() {
            ValidationResult result = GameInputValidator.validateGuess("1 2 3", 4, 8);

            assertFalse(result.isValid());
            assertTrue(result.getErrorMessage().contains("Guess must consist of 4 numbers."));
        }

        @Test
        @DisplayName("Should reject input with too many numbers")
        void testTooManyNumbers() {
            ValidationResult result = GameInputValidator.validateGuess("1 2 3 4 5", 4, 8);

            assertFalse(result.isValid());
            assertTrue(result.getErrorMessage().contains("Guess must consist of 4 numbers."));
        }

        @Test
        @DisplayName("Should reject input with non-numeric characters")
        void testNonNumericInput() {
            ValidationResult result = GameInputValidator.validateGuess("1 a 3 4", 4, 8);

            assertFalse(result.isValid());
            assertTrue(result.getErrorMessage().contains("'a' is not a valid number"));
        }

        @Test
        @DisplayName("Should reject input with negative numbers")
        void testNegativeNumbers() {
            ValidationResult result = GameInputValidator.validateGuess("-1 2 3 4", 4, 8);

            assertFalse(result.isValid());
            assertTrue(result.getErrorMessage().contains("Numbers must be between 0 and 8."));
        }

        @Test
        @DisplayName("Should reject input with numbers above maximum")
        void testNumbersAboveMax() {
            ValidationResult result = GameInputValidator.validateGuess("1 2 3 9", 4, 8);

            assertFalse(result.isValid());
            assertTrue(result.getErrorMessage().contains("Numbers must be between 0 and 8."));
        }

        @Test
        @DisplayName("Should reject input with decimal numbers")
        void testDecimalNumbers() {
            ValidationResult result = GameInputValidator.validateGuess("1 2.5 3 4", 4, 8);

            assertFalse(result.isValid());
            assertTrue(result.getErrorMessage().contains("'2.5' is not a valid number"));
        }

        @Test
        @DisplayName("Should work with different maximum values")
        void testDifferentMaxValues() {
            ValidationResult result = GameInputValidator.validateGuess("0 1 2", 3, 2);

            assertTrue(result.isValid());
            assertArrayEquals(new int[]{0, 1, 2}, result.getNumbers());
        }

        @Test
        @DisplayName("Should reject number equal to max+1 with custom max")
        void testCustomMaxValidation() {
            ValidationResult result = GameInputValidator.validateGuess("0 1 3", 3, 2);

            assertFalse(result.isValid());
            assertTrue(result.getErrorMessage().contains("Numbers must be between 0 and 2."));
        }

    }


    @Nested
    @DisplayName("intArrayToString() tests")
    class intArraytoStringTests {

        @Test
        @DisplayName("Should format single digit array correctly")
        void testSingleDigitArray() {
            String result = GameInputValidator.intArrayToString(new int[]{1, 2, 3, 4});

            assertEquals("1 2 3 4", result);
        }

        @Test
        @DisplayName("Should format empty array correctly")
        void testEmptyArray() {
            String result = GameInputValidator.intArrayToString(new int[0]);

            assertEquals("", result);
        }

        @Test
        @DisplayName("Should format single element array correctly")
        void testSingleElementArray() {
            String result = GameInputValidator.intArrayToString(new int[]{1});

            assertEquals("1", result);
        }

        @Test
        @DisplayName("Should format array with zeros correctly")
        void testArrayWithZeros() {
            String result = GameInputValidator.intArrayToString(new int[]{0, 0, 0, 1});

            assertEquals("0 0 0 1", result);
        }

        @Test
        @DisplayName("Should format larger numbers correctly")
        void testArrayWithLargeNumbers() {
            String result = GameInputValidator.intArrayToString(new int[]{10, 99, 50});

            assertEquals("10 99 50", result);
        }
    }
}
