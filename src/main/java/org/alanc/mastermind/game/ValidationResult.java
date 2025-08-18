package org.alanc.mastermind.game;

/**
 * Result of input validation containing success/failure status and parsed data.
 */
public final class ValidationResult {
    private final boolean valid;
    private final int[] numbers;
    private final String errorMessage;

    private ValidationResult(boolean valid, int[] numbers, String errorMessage) {
        this.valid = valid;
        this.numbers = numbers == null ? new int[0] : numbers.clone();
        this.errorMessage = errorMessage;
    }

    /**
     * Creates a successful validation result with parsed numbers.
     * 
     * @param numbers the successfully parsed numbers
     * @return a ValidationResult indicating success
     */
    public static ValidationResult success(int[] numbers) {
        return new ValidationResult(true, numbers, null);
    }

    /**
     * Creates a failed validation result with an error message.
     * 
     * @param errorMessage the validation error message
     * @return a ValidationResult indicating failure
     */
    public static ValidationResult failure(String errorMessage) {
        return new ValidationResult(false, null, errorMessage);
    }

    /** @return true if validation was successful, false otherwise */
    public boolean isValid() { return valid; }
    
    /** @return a defensive copy of the parsed numbers (null if validation failed) */
    public int[] getNumbers() { return numbers.clone(); }
    
    /** @return the error message if validation failed, null otherwise */
    public String getErrorMessage() { return errorMessage; }
}
