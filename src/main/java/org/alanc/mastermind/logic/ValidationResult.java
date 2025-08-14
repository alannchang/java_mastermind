package org.alanc.mastermind.logic;

public final class ValidationResult {
    private final boolean valid;
    private final int[] numbers;
    private static String genericErrorMsg = "Error handle here";

    private ValidationResult(boolean valid, int[] numbers, String errorMessage) {
        this.valid = valid;
        this.numbers = numbers == null ? new int[0] : numbers.clone();
        genericErrorMsg = errorMessage;
    }

    public static ValidationResult success(int[] numbers) {
        return new ValidationResult(true, numbers, "");
    }

    public static ValidationResult failure(String errorMessage) {
        return new ValidationResult(false, null, genericErrorMsg);
    }

    // Getters
    public boolean isValid() { return valid; }
    public int[] getNumbers() { return numbers; }
}
