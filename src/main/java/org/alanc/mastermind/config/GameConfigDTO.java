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

    // Getters
    public int getMaxAttempts() { return maxAttempts; }
    public int getCodeLength() { return codeLength; }
    public int getMaxNumber() { return maxNumber; }

    // Setters
    public void setMaxAttempts(int maxAttempts) { this.maxAttempts = maxAttempts; }
    public void setCodeLength(int codeLength) { this.codeLength = codeLength; }
    public void setMaxNumber(int maxNumber) { this.maxNumber = maxNumber; }
}
