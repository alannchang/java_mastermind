package org.alanc.mastermind.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GameConfig Tests")
class GameConfigTest {

    @Nested
    @DisplayName("Default Config Tests")
    class DefaultConfigurationTests {

        @Test
        @DisplayName("Should provide correct default values")
        void testDefaultValues() {
            GameConfig config = new GameConfig();

            assertNotNull(config);
            assertEquals(10, config.getMaxAttempts());
            assertEquals(4, config.getCodeLength());
            assertEquals(7, config.getMaxNumber());
        }

        @Test
        @DisplayName("Should create new instance each time")
        void testDefaultConfigCreatesNewInstances() {
            GameConfig config1 = new GameConfig();
            GameConfig config2 = new GameConfig();

            assertNotSame(config1, config2);
            assertEquals(config1.getMaxAttempts(), config2.getMaxAttempts());
            assertEquals(config1.getCodeLength(), config2.getCodeLength());
            assertEquals(config1.getMaxNumber(), config2.getMaxNumber());
        }
    }

    @Nested
    @DisplayName("Setter Tests")
    class SetterTests {

        @Test
        @DisplayName("Should change configuration with custom max attempts")
        void testSetterUsingCustomMaxAttempts() {
            GameConfig config = new GameConfig();
            config.setMaxAttempts(15);

            assertEquals(15, config.getMaxAttempts());
            assertEquals(4, config.getCodeLength()); // Default
            assertEquals(7, config.getMaxNumber());  // Default
        }

        @Test
        @DisplayName("Should chnage configuration with custom code length")
        void testSetterUsingCustomCodeLength() {
            GameConfig config = new GameConfig();
            config.setCodeLength(6);

            assertEquals(10, config.getMaxAttempts()); // Default
            assertEquals(6, config.getCodeLength());
            assertEquals(7, config.getMaxNumber());   // Default
        }

        @Test
        @DisplayName("Should change configuration with custom max number")
        void testSetterUsingCustomMaxNumber() {
            GameConfig config = new GameConfig();
            config.setMaxNumber(10);

            assertEquals(10, config.getMaxAttempts()); // Default
            assertEquals(4, config.getCodeLength());   // Default
            assertEquals(10, config.getMaxNumber());
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderPatternTests {

        @Test
        @DisplayName("Should build configuration with default values")
        void testBuilderDefaults() {
            GameConfig config = new GameConfig.Builder().build();

            assertEquals(10, config.getMaxAttempts());
            assertEquals(4, config.getCodeLength());
            assertEquals(7, config.getMaxNumber());
        }

        @Test
        @DisplayName("Should build configuration with all custom values")
        void testBuilderWithAllCustomValues() {
            GameConfig config = new GameConfig.Builder()
                    .maxAttempts(20)
                    .codeLength(8)
                    .maxNumber(15)
                    .build();

            assertEquals(20, config.getMaxAttempts());
            assertEquals(8, config.getCodeLength());
            assertEquals(15, config.getMaxNumber());
        }

        @Test
        @DisplayName("Should allow setting values in any order")
        void testBuilderAnyOrder() {
            GameConfig config1 = new GameConfig.Builder()
                    .maxAttempts(25)
                    .codeLength(7)
                    .maxNumber(12)
                    .build();

            GameConfig config2 = new GameConfig.Builder()
                    .maxNumber(12)
                    .maxAttempts(25)
                    .codeLength(7)
                    .build();

            assertEquals(config1.getMaxAttempts(), config2.getMaxAttempts());
            assertEquals(config1.getCodeLength(), config2.getCodeLength());
            assertEquals(config1.getMaxNumber(), config2.getMaxNumber());
        }

        @Test
        @DisplayName("Should allow overriding values")
        void testBuilderOverrideValues() {
            GameConfig config = new GameConfig.Builder()
                    .maxAttempts(10)
                    .maxAttempts(20) // Override
                    .codeLength(4)
                    .codeLength(6)   // Override
                    .maxNumber(8)
                    .maxNumber(12)   // Override
                    .build();

            assertEquals(20, config.getMaxAttempts()); // Last value used
            assertEquals(6, config.getCodeLength());   // Last value used
            assertEquals(12, config.getMaxNumber());   // Last value used
        }

        @Test
        @DisplayName("Should allow reusing builder")
        void testBuilderReuse() {
            GameConfig.Builder builder = new GameConfig.Builder()
                    .maxAttempts(20)
                    .codeLength(6);

            GameConfig config1 = builder.maxNumber(10).build();
            GameConfig config2 = builder.maxNumber(15).build();

            assertEquals(10, config1.getMaxNumber());
            assertEquals(15, config2.getMaxNumber());
            // Other values should be the same
            assertEquals(config1.getMaxAttempts(), config2.getMaxAttempts());
            assertEquals(config1.getCodeLength(), config2.getCodeLength());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundaries Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle minimum valid values")
        void testMinimumValues() {
            GameConfig config = new GameConfig.Builder()
                    .maxAttempts(1)
                    .codeLength(1)
                    .maxNumber(1)
                    .build();

            assertEquals(1, config.getMaxAttempts());
            assertEquals(1, config.getCodeLength());
            assertEquals(1, config.getMaxNumber());
        }

        @Test
        @DisplayName("Should handle large valid values")
        void testLargeValues() {
            GameConfig config = new GameConfig.Builder()
                    .maxAttempts(100)
                    .codeLength(20)
                    .maxNumber(100)
                    .build();

            assertEquals(100, config.getMaxAttempts());
            assertEquals(20, config.getCodeLength());
            assertEquals(100, config.getMaxNumber());
        }

        @Test
        @DisplayName("Should handle zero values")
        void testZeroValues() {
            GameConfig config = new GameConfig.Builder()
                    .maxAttempts(0)
                    .codeLength(0)
                    .maxNumber(0)
                    .build();

            assertEquals(0, config.getMaxAttempts());
            assertEquals(0, config.getCodeLength());
            assertEquals(0, config.getMaxNumber());
        }

        @Test
        @DisplayName("Should handle negative values")
        void testNegativeValues() {
            GameConfig config = new GameConfig.Builder()
                    .maxAttempts(-5)
                    .codeLength(-3)
                    .maxNumber(-1)
                    .build();

            assertEquals(-5, config.getMaxAttempts());
            assertEquals(-3, config.getCodeLength());
            assertEquals(-1, config.getMaxNumber());
        }
    }
}
