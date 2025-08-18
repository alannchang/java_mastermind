package org.alanc.mastermind.random;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MathRandomService Tests")
class MathRandomServiceTest {

    private final MathRandomService service = new MathRandomService();

    @Nested
    @DisplayName("Random Generation")
    class GenerationTests {

        @Test
        @DisplayName("Should generate correct quantity and format")
        void testBasicGeneration() {
            String result = service.generate(4, 0, 7);

            assertNotNull(result);
            String[] numbers = result.split(" ");
            assertEquals(4, numbers.length);
        }

        @Test
        @DisplayName("Should respect min and max bounds")
        void testRangeBounds() {
            String result = service.generate(10, 3, 5);
            String[] numbers = result.split(" ");

            for (String numberStr : numbers) {
                int number = Integer.parseInt(numberStr);
                assertTrue(number >= 3 && number <= 5, 
                    "Number " + number + " is outside range [3, 5]");
            }
        }

        @Test
        @DisplayName("Should handle edge cases")
        void testEdgeCases() {
            // Single number
            String single = service.generate(1, 0, 0);
            assertEquals("0", single);

            // Zero quantity
            String empty = service.generate(0, 0, 7);
            assertEquals("", empty);
        }
    }
}