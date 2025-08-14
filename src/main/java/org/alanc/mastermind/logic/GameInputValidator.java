package org.alanc.mastermind.logic;

public class GameInputValidator {

    public static ValidationResult validateGuess(String playerGuess, int expectedLength, int maxValue) {
        if (playerGuess == null || playerGuess.trim().isEmpty()) {
            return ValidationResult.failure("Please enter your guess.");
        }

        String[] parts = playerGuess.trim().split("\\s+");

        if (parts.length != expectedLength) {
            return ValidationResult.failure(String.format("Guess must consist of %d numbers.", expectedLength));
        }

        int[] numbers = new int[expectedLength];

        for (int i = 0; i < parts.length; i++) {
            try {
                int number = Integer.parseInt(parts[i]);
                if (number < 0 || number > maxValue) {
                    return ValidationResult.failure(String.format("Numbers must be between 0 and %d.", maxValue));
                }
                numbers[i] = number;
            } catch (NumberFormatException e) {
                return ValidationResult.failure(String.format("'%s' is not a valid number.", parts[i]));
            }
        }
        return ValidationResult.success(numbers);
    }

    public static String intArrayToString(int[] numbers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numbers.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(numbers[i]);
        }
        return sb.toString();
    }
}
