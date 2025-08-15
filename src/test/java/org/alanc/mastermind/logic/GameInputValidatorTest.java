package org.alanc.mastermind.logic;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

class GameInputValidatorTest {

    @Nested
    @DisplayName("validateGuess() tests")
    class ValidateGuessTests{

        @Test
        @DisplayName("Should accept valid input with correct format")
        void testValidInput() {
            ValidationResult result = GameInputValidator.validateGuess("1 2 3 4", 4, 8);

            assertTrue(result.isValid());
            assertArrayEquals(new int[]{1, 2, 3, 4}, result.getNumbers());
        }
    }
}
