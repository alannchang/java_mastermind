package org.alanc.mastermind.config;

public class GameConfig {
    private int maxAttempts;
    private int codeLength;
    private int maxNumber;

    // constructor sets game parameters to default
    public GameConfig() {
        this.maxAttempts = 10;
        this.codeLength = 4;
        this.maxNumber = 7;
    }

    // used in UI
    public void resetToDefaults() {
        this.maxAttempts = 10;
        this.codeLength = 4;
        this.maxNumber = 7;
    }

    // Getters
    public int getMaxAttempts() { return maxAttempts; }
    public int getCodeLength() { return codeLength; }
    public int getMaxNumber() { return maxNumber; }

    // Setters used when players want to customize their game
    public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
    public void setCodeLength(int codeLength) { this.codeLength = codeLength; }
    public void setMaxNumber(int maxNumber) { this.maxNumber = maxNumber; }



    private GameConfig(Builder builder) {
        this.maxAttempts = builder.maxAttempts;
        this.codeLength = builder.codeLength;
        this.maxNumber = builder.maxNumber;
    }

    // Builder
    public static class Builder {
        private int maxAttempts = 10; // Defaults
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

        public GameConfig build() {
            return new GameConfig(this);
        }
    }
}
