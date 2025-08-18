package org.alanc.mastermind.config;

/**
 * Immutable configuration for Mastermind game parameters.
 * Defines maxAttempts (default: 10), codeLength (default: 4), and maxNumber (default: 7).
 * Use the Builder pattern to create instances.
 */
public final class GameConfig {
    private final int maxAttempts;
    private final int codeLength;
    private final int maxNumber;

    private GameConfig(int maxAttempts, int codeLength, int maxNumber) {
        if (maxAttempts <= 0) {
            throw new IllegalArgumentException("maxAttempts must be positive, got: " + maxAttempts);
        }
        if (codeLength <= 0) {
            throw new IllegalArgumentException("codeLength must be positive, got: " + codeLength);
        }
        if (maxNumber < 0) {
            throw new IllegalArgumentException("maxNumber must be non-negative, got: " + maxNumber);
        }
        
        this.maxAttempts = maxAttempts;
        this.codeLength = codeLength;
        this.maxNumber = maxNumber;
    }

    // Getters
    public int getMaxAttempts() { return maxAttempts; }
    public int getCodeLength() { return codeLength; }
    public int getMaxNumber() { return maxNumber; }

    /**
     * Creates a GameConfig with default settings.
     * 
     * @return a new GameConfig with default values (10 attempts, 4-digit code, numbers 0-7)
     */
    public static GameConfig defaults() {
        return new Builder().build();
    }

    /**
     * Builder for creating GameConfig instances with custom parameters.
     */
    public static class Builder {
        private int maxAttempts = 10;
        private int codeLength = 4;
        private int maxNumber = 7;

        /**
         * Sets the maximum number of attempts allowed.
         * 
         * @param maxAttempts the maximum number of guesses (must be positive)
         * @return this builder for method chaining
         */
        public Builder maxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return this;
        }

        /**
         * Sets the length of the secret code.
         * 
         * @param codeLength the number of digits in the secret code (must be positive)
         * @return this builder for method chaining
         */
        public Builder codeLength(int codeLength) {
            this.codeLength = codeLength;
            return this;
        }

        /**
         * Sets the maximum number value allowed in the secret code.
         * 
         * @param maxNumber the highest number allowed (creates range 0 to maxNumber)
         * @return this builder for method chaining
         */
        public Builder maxNumber(int maxNumber) {
            this.maxNumber = maxNumber;
            return this;
        }

        /**
         * Creates a builder pre-populated with values from an existing configuration.
         * 
         * @param existing the configuration to copy values from
         * @return a new builder with the existing configuration's values
         */
        public static Builder from(GameConfig existing) {
            return new Builder()
                .maxAttempts(existing.maxAttempts)
                .codeLength(existing.codeLength)
                .maxNumber(existing.maxNumber);
        }

        /**
         * Builds a new GameConfig with the current builder settings.
         * 
         * @return a new immutable GameConfig instance
         * @throws IllegalArgumentException if any parameter is invalid
         */
        public GameConfig build() {
            return new GameConfig(maxAttempts, codeLength, maxNumber);
        }
    }
}
