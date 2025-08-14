package org.alanc.mastermind.logic;

public class GameInputValidator {

    public static int[] validGuess(String playerGuess, int expectedLength, int maxValue) {
        if (playerGuess == null || playerGuess.trim().isEmpty()) {
            return new int[0];
        }

        String[] parts = playerGuess.trim().split("\\s+");

        if (parts.length != expectedLength) {
            return new int[0];
        }

        int[] numbers = new int[expectedLength];

        for (int i = 0; i < parts.length; i++) {
            int number = Integer.parseInt(parts[i]);
            numbers[i] = number;
        }
        return numbers;
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
