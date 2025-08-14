package org.alanc.mastermind.logic;

import org.alanc.mastermind.config.GameConfigDTO;

public final class GameState {
    private final String secretCode;
    private final int[] playerGuess;
    private final int attemptsRemaining;
    private final int maxAttempts;
    private final int codeLength;
    private final int maxNumber;
    private final boolean gameEnded;
    private final boolean playerWon;

    private GameState(String secretCode, int[] playerGuess, int attemptsRemaining, int maxAttempts, int codeLength,
                      int maxNumber, boolean gameEnded, boolean playerWon) {
        this.secretCode = secretCode;
        this.playerGuess = playerGuess;
        this.attemptsRemaining = attemptsRemaining;
        this.maxAttempts = maxAttempts;
        this.codeLength = codeLength;
        this.maxNumber = maxNumber;
        this.gameEnded = gameEnded;
        this.playerWon = playerWon;
    }

    public static GameState createNew(String secretCode, GameConfigDTO config) {
        ValidationResult validationResult = GameInputValidator.validateGuess(secretCode,
                config.getCodeLength(), config.getMaxNumber());
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException("Invalid secret code: " + validationResult.getErrorMessage());
        }

        return new GameState(
                secretCode,
                validationResult.getNumbers(),
                config.getMaxAttempts(),
                config.getMaxAttempts(),
                config.getCodeLength(),
                config.getMaxNumber(),
                false,
                false
        );
    }

    public GameState withGuess(int[] guessNumbers) {
        if (gameEnded) {
            throw new IllegalStateException("Cannot add guess to ended game");
        }

        GuessResult result = evaluateGuess(guessNumbers);

        int newAttemptsRemaining = attemptsRemaining - 1;
        boolean won = result.isAllCorrect();
        boolean ended = won || newAttemptsRemaining <= 0;

        return new GameState(
                secretCode,
                playerGuess,
                newAttemptsRemaining,
                maxAttempts,
                codeLength,
                maxNumber,
                ended,
                won
        );
    }

    private GuessResult evaluateGuess(int[] currentGuess){
        int correctLocations = 0;
        int correctNumbers = 0;

        // Count exact matches
        for (int i = 0; i < playerGuess.length; i++) {
            if (playerGuess[i] == currentGuess[i]) {
                correctLocations++;
            }
        }

        // Count total correct numbers (including wrong positions)
        correctNumbers = countTotalCorrectNumbers(playerGuess, currentGuess, correctLocations);

        return new GuessResult(
                GameInputValidator.intArrayToString(currentGuess),
                correctNumbers,
                correctLocations,
                correctLocations == playerGuess.length
        );
    }

    private int countTotalCorrectNumbers(int[] secret, int[] currentGuess, int correctLocations) {
        // Use frequency maps to count correct numbers in wrong locations
        int[] secretFrequency = new int[codeLength];
        int[] guessFrequency = new int[codeLength];

        // Count frequencies excluding exact matches
        for (int i = 0; i < secret.length; i++) {
            if (secret[i] != currentGuess[i]) {
                secretFrequency[secret[i]]++;
                guessFrequency[currentGuess[i]]++;
            }
        }

        // Count correct numbers in wrong locations
        int wrongLocations = 0;
        for (int i = 0; i < secretFrequency.length; i++) {
            wrongLocations += Math.min(secretFrequency[i], guessFrequency[i]);
        }

        return correctLocations + wrongLocations;
    }

    // Getters
    public String getSecretCode() { return secretCode; }
    public int[] getPlayerGuess() { return playerGuess.clone(); }
    public int getAttemptsRemaining() { return attemptsRemaining; }
    public int getMaxAttempts() { return maxAttempts; }
    public int getCodeLength() { return codeLength; }
    public int getMaxNumber() { return maxNumber; }
    public boolean isGameEnded() { return gameEnded; }
    public boolean hasPlayerWon() { return playerWon; }

    public static final class GuessResult {
        private final String guess;
        private final int correctNumbers;
        private final int correctLocations;
        private final boolean allCorrect;

        public GuessResult(String guess, int correctNumbers, int correctLocations, boolean allCorrect) {
            this.guess = guess;
            this.correctNumbers = correctNumbers;
            this.correctLocations = correctLocations;
            this.allCorrect = allCorrect;
        }

        // Getters
        public String getGuess() { return guess;}
        public int getCorrectNumbers() { return correctNumbers; }
        public int getCorrectLocations() { return correctLocations; }
        public boolean isAllCorrect() { return allCorrect; }

        public String formatFeedback() {
            if (allCorrect) {
                return "You guessed all the numbers correctly!";
            }
            if (correctNumbers == 0) {
                return "All incorrect.";
            }
            return String.format("%d correct number%s and %d correct location%s.",
                    correctNumbers,
                    correctNumbers == 1 ? "" : "s",
                    correctLocations,
                    correctLocations == 1 ? "" : "s"
            );
        }

    }



}
