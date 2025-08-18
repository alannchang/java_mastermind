package org.alanc.mastermind.config;

public final class GameConfig {
    private final int maxAttempts;
    private final int codeLength;
    private final int maxNumber;

    private GameConfig(int maxAttempts, int codeLength, int maxNumber) {
        // Validate parameters
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

    // Convenience method for default configuration
    public static GameConfig defaults() {
        return new Builder().build();
    }

    // Builder
    public static class Builder {
        private int maxAttempts = 10; // Default values
        private int codeLength = 4;
        private int maxNumber = 7;

        public Builder maxAttempts(int maxAttempts) {
            this.maxAttempts = maxAttempts;
            return this;
        }

        public Builder codeLength(int codeLength) {
            this.codeLength = codeLength;
            return this;
        }

        public Builder maxNumber(int maxNumber) {
            this.maxNumber = maxNumber;
            return this;
        }

        // Create builder from existing config for "mutation" pattern
        public static Builder from(GameConfig existing) {
            return new Builder()
                .maxAttempts(existing.maxAttempts)
                .codeLength(existing.codeLength)
                .maxNumber(existing.maxNumber);
        }

        public GameConfig build() {
            return new GameConfig(maxAttempts, codeLength, maxNumber);
        }
    }
}
