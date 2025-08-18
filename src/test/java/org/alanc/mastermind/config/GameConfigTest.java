package org.alanc.mastermind.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameConfig Tests")
class GameConfigTest {

    @Nested
    @DisplayName("Creation and Default Values")
    class CreationTests {

        @Test
        @DisplayName("Should provide correct default values")
        void testDefaultValues() {
            GameConfig config = GameConfig.defaults();

            assertEquals(10, config.getMaxAttempts());
            assertEquals(4, config.getCodeLength());
            assertEquals(7, config.getMaxNumber());
        }

        @Test
        @DisplayName("Should build custom configurations")
        void testCustomValues() {
            GameConfig config = new GameConfig.Builder()
                    .maxAttempts(20)
                    .codeLength(6)
                    .maxNumber(15)
                    .build();

            assertEquals(20, config.getMaxAttempts());
            assertEquals(6, config.getCodeLength());
            assertEquals(15, config.getMaxNumber());
        }
    }

    @Nested
    @DisplayName("Builder Pattern")
    class BuilderTests {

        @Test
        @DisplayName("Should build from existing config for immutable updates")
        void testBuilderFromExisting() {
            GameConfig original = GameConfig.defaults();
            GameConfig modified = GameConfig.Builder.from(original)
                    .maxAttempts(20)
                    .build();

            // Original unchanged
            assertEquals(10, original.getMaxAttempts());
            assertEquals(4, original.getCodeLength());
            assertEquals(7, original.getMaxNumber());

            // Modified has changes
            assertEquals(20, modified.getMaxAttempts());
            assertEquals(4, modified.getCodeLength());  // Preserved
            assertEquals(7, modified.getMaxNumber());   // Preserved
        }
    }

    @Nested
    @DisplayName("Validation")
    class ValidationTests {

        @Test
        @DisplayName("Should reject invalid parameters")
        void testInvalidParameters() {
            assertThrows(IllegalArgumentException.class, () -> 
                new GameConfig.Builder().maxAttempts(0).build());
            
            assertThrows(IllegalArgumentException.class, () -> 
                new GameConfig.Builder().codeLength(-1).build());
            
            assertThrows(IllegalArgumentException.class, () -> 
                new GameConfig.Builder().maxNumber(-1).build());
        }

        @Test
        @DisplayName("Should accept valid boundary values")
        void testValidBoundaryValues() {
            assertDoesNotThrow(() -> {
                GameConfig config = new GameConfig.Builder()
                        .maxAttempts(1)
                        .codeLength(1)
                        .maxNumber(0)
                        .build();
                
                assertEquals(1, config.getMaxAttempts());
                assertEquals(1, config.getCodeLength());
                assertEquals(0, config.getMaxNumber());
            });
        }
    }
}
