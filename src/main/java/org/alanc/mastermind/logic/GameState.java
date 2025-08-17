package org.alanc.mastermind.logic;

import org.alanc.mastermind.config.GameConfig;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public final class GameState {
    private final String secretCode;
    private final int[] playerGuess;
    private final List<GuessResult> guessHistory;
    private final int attemptsRemaining;
    private final int maxAttempts;
    private final int codeLength;
    private final int maxNumber;
    private final boolean gameEnded;
    private final boolean playerWon;

    private GameState(String secretCode, int[] playerGuess, List<GuessResult> guessHistory,int attemptsRemaining,
                      int maxAttempts, int codeLength, int maxNumber, boolean gameEnded, boolean playerWon) {
        this.secretCode = secretCode;
        this.playerGuess = playerGuess.clone();
        this.guessHistory = new ArrayList<>(guessHistory);
        this.attemptsRemaining = attemptsRemaining;
        this.maxAttempts = maxAttempts;
        this.codeLength = codeLength;
        this.maxNumber = maxNumber;
        this.gameEnded = gameEnded;
        this.playerWon = playerWon;
    }

    public static GameState createNew(String secretCode, GameConfig config) {
        ValidationResult validationResult = GameInputValidator.validateGuess(secretCode,
                config.getCodeLength(), config.getMaxNumber());
        if (!validationResult.isValid()) {
            throw new IllegalArgumentException("Invalid secret code: " + validationResult.getErrorMessage());
        }

        return new GameState(
                secretCode,
                validationResult.getNumbers(),
                Collections.emptyList(),
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
        List<GuessResult> newHistory = new ArrayList<>(guessHistory);
        newHistory.add(result);

        int newAttemptsRemaining = attemptsRemaining - 1;
        boolean won = result.allCorrect();
        boolean ended = won || newAttemptsRemaining <= 0;

        return new GameState(
                secretCode,
                playerGuess,
                newHistory,
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
        int[] secretFrequency = new int[maxNumber + 1];
        int[] guessFrequency = new int[maxNumber + 1];

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
    public List<GuessResult> getGuessHistory() { return Collections.unmodifiableList(guessHistory); }
    public int getAttemptsRemaining() { return attemptsRemaining; }
    public int getAttemptsMade() { return maxAttempts - attemptsRemaining; }
    public int getMaxAttempts() { return maxAttempts; }
    public int getCodeLength() { return codeLength; }
    public int getMaxNumber() { return maxNumber; }
    public boolean isGameEnded() { return gameEnded; }
    public boolean hasPlayerWon() { return playerWon; }

    public record GuessResult(String guess, int correctNumbers, int correctLocations, boolean allCorrect) {

        public String provideFeedback() {
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
