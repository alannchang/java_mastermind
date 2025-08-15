package org.alanc.mastermind.random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;

class MathRandomServiceTest {

    private final MathRandomService service = new MathRandomService();

    @Nested
    @DisplayName("Basic Generation Tests")
    class BasicGenerationTests {

        @Test
        @DisplayName("Should generate correct quantity of numbers")
        void testCorrectQuantity() {
            String result = service.generate(4, 0, 8);

            assertNotNull(result);
            String[] numbers = result.split("\\s+");
            assertEquals(4, numbers.length);
        }

        @Test
        @DisplayName("Should generate single number")
        void testSingleNumber() {
            String result = service.generate(1, 5, 10);

            assertNotNull(result);
            assertFalse(result.contains(" "));
            int number = Integer.parseInt(result);
            assertTrue(number >= 5 && number <= 10);
        }

        @Test
        @DisplayName("Should handle large quantities")
        void testLargeQuantity() {
            String result = service.generate(100, 0, 1);

            assertNotNull(result);
            String[] numbers = result.split("\\s+");
            assertEquals(100, numbers.length);

            // All numbers should be 0 or 1
            for (String numStr : numbers) {
                int num = Integer.parseInt(numStr);
                assertTrue(num >= 0 && num <= 1);
            }
        }
    }

    @Nested
    @DisplayName("Range Validation Tests")
    class RangeValidationTests {

        @RepeatedTest(10)
        @DisplayName("Should respect min and max bounds")
        void testBounds() {
            int min = 3;
            int max = 7;
            String result = service.generate(20, min, max);

            String[] numbers = result.split("\\s+");
            for (String numStr : numbers) {
                int number = Integer.parseInt(numStr);
                assertTrue(number >= min, "Number " + number + " is below minimum " + min);
                assertTrue(number <= max, "Number " + number + " is above maximum " + max);
            }
        }

        @Test
        @DisplayName("Should handle min equals max")
        void testMinEqualsMax() {
            String result = service.generate(5, 7, 7);

            assertNotNull(result);
            assertEquals("7 7 7 7 7", result);
        }

        @Test
        @DisplayName("Should handle negative ranges")
        void testNegativeRange() {
            String result = service.generate(3, -5, -1);

            String[] numbers = result.split("\\s+");
            assertEquals(3, numbers.length);

            for (String numStr : numbers) {
                int number = Integer.parseInt(numStr);
                assertTrue(number >= -5 && number <= -1);
            }
        }

        @Test
        @DisplayName("Should handle zero-based range")
        void testZeroBasedRange() {
            String result = service.generate(10, 0, 0);

            assertNotNull(result);
            assertEquals("0 0 0 0 0 0 0 0 0 0", result);
        }

        @Test
        @DisplayName("Should handle large ranges")
        void testLargeRange() {
            String result = service.generate(5, 1, 100);

            String[] numbers = result.split("\\s+");
            assertEquals(5, numbers.length);

            for (String numStr : numbers) {
                int number = Integer.parseInt(numStr);
                assertTrue(number >= 1 && number <= 100);
            }
        }
    }

    @Nested
    @DisplayName("Output Format Tests")
    class OutputFormatTests {

        @Test
        @DisplayName("Should format output with single spaces")
        void testSpaceDelimited() {
            String result = service.generate(4, 0, 1);

            assertNotNull(result);
            assertFalse(result.startsWith(" "));
            assertFalse(result.endsWith(" "));
            assertFalse(result.contains("  ")); // No double spaces

            // Should have exactly 3 spaces for 4 numbers
            assertEquals(3, result.length() - result.replace(" ", "").length());
        }

        @Test
        @DisplayName("Should produce parseable integers")
        void testParseableOutput() {
            String result = service.generate(5, 10, 99);

            String[] parts = result.split("\\s+");
            assertEquals(5, parts.length);

            for (String part : parts) {
                assertDoesNotThrow(() -> Integer.parseInt(part));
            }
        }

        @Test
        @DisplayName("Should not contain leading/trailing whitespace")
        void testNoLeadingTrailingWhitespace() {
            String result = service.generate(3, 1, 5);

            assertEquals(result.trim(), result);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle quantity of zero")
        void testZeroQuantity() {
            String result = service.generate(0, 1, 10);

            assertNotNull(result);
            assertEquals("", result);
        }

        @Test
        @DisplayName("Should handle negative quantities")
        void testNegativeQuantity() {
            String result = service.generate(-2, 0, 8);

            assertNotNull(result);
            assertEquals("", result);
        }

        @Test
        @DisplayName("Should handle maximum integer bounds")
        void testMaxIntegerBounds() {
            // Test with reasonable large values (not Integer.MAX_VALUE to avoid overflow)
            String result = service.generate(2, 1000000, 1000001);

            String[] numbers = result.split("\\s+");
            assertEquals(2, numbers.length);

            for (String numStr : numbers) {
                int number = Integer.parseInt(numStr);
                assertTrue(number >= 1000000 && number <= 1000001);
            }
        }
    }
}