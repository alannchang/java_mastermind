package org.alanc.mastermind.logic;

public final class ValidationResult {
    private final boolean valid;
    private final int[] numbers;
    private final String errorMessage;

    private ValidationResult(boolean valid, int[] numbers, String errorMessage) {
        this.valid = valid;
        this.numbers = numbers == null ? new int[0] : numbers.clone();
        this.errorMessage = errorMessage;
    }

    public static ValidationResult success(int[] numbers) {
        return new ValidationResult(true, numbers, null);
    }

    public static ValidationResult failure(String errorMessage) {
        return new ValidationResult(false, null, errorMessage);
    }

    // Getters
    public boolean isValid() { return valid; }
    public int[] getNumbers() { return numbers.clone(); }
    public String getErrorMessage() { return errorMessage; }
}
