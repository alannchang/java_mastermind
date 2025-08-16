package org.alanc.mastermind.config;

public class GameConfigDTO {
    private int maxAttempts;
    private int codeLength;
    private int maxNumber;

    public GameConfigDTO (int maxAttempts, int codeLength, int maxNumber) {
        this.maxAttempts = maxAttempts;
        this.codeLength = codeLength;
        this.maxNumber = maxNumber;
    }

    // default config
    public GameConfigDTO() {
        this(10, 4, 7);
    }

    // Getters
    public int getMaxAttempts() { return maxAttempts; }
    public int getCodeLength() { return codeLength; }
    public int getMaxNumber() { return maxNumber; }

    // Setters used when players want to customize their game
    public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
    public void setCodeLength(int codeLength) { this.codeLength = codeLength; }
    public void setMaxNumber(int maxNumber) { this.maxNumber = maxNumber; }

    private GameConfigDTO(Builder builder) {
        this.maxAttempts = builder.maxAttempts;
        this.codeLength = builder.codeLength;
        this.maxNumber = builder.maxNumber;
    }

    // Builder used for default and
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

        public GameConfigDTO build() {
            return new GameConfigDTO(this);
        }
    }
}
