package org.alanc.mastermind.game;

import org.alanc.mastermind.config.GameConfig;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Immutable representation of a Mastermind game state.
 *
 * This class encapsulates all information about a game in progress, including:
 * - the secret code to be guessed
 * - history of all player guesses and their results
 * - remaining attempts and game configuration
 * - current game status (active, won, lost)
 */
public final class GameState {
    private final String secretCode;
    private final int[] secretCodeNumbers;
    private final List<GuessResult> guessHistory;
    private final int attemptsRemaining;
    private final int maxAttempts;
    private final int codeLength;
    private final int maxNumber;
    private final boolean gameEnded;
    private final boolean playerWon;

    private GameState(String secretCode, int[] secretCodeNumbers, List<GuessResult> guessHistory,int attemptsRemaining,
                      int maxAttempts, int codeLength, int maxNumber, boolean gameEnded, boolean playerWon) {
        this.secretCode = secretCode;
        this.secretCodeNumbers = secretCodeNumbers.clone();
        this.guessHistory = new ArrayList<>(guessHistory);
        this.attemptsRemaining = attemptsRemaining;
        this.maxAttempts = maxAttempts;
        this.codeLength = codeLength;
        this.maxNumber = maxNumber;
        this.gameEnded = gameEnded;
        this.playerWon = playerWon;
    }

    /**
     * Creates a new game state with the specified secret code and configuration.
     * 
     * @param secretCode the secret code that players must guess (e.g., "1 2 3 4")
     * @param config the game configuration containing rules and limits
     * @return a new GameState instance ready for play
     * @throws IllegalArgumentException if the secret code is invalid for the given configuration
     */
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

    /**
     * Creates a new game state by processing a player's guess.
     * 
     * This method evaluates the guess against the secret code, updates the
     * guess history, decrements attempts, and determines if the game has ended.
     * 
     * @param guessNumbers the player's guess as an array of integers
     * @return a new GameState reflecting the result of this guess
     * @throws IllegalStateException if the game has already ended
     */
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
                secretCodeNumbers,
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
        for (int i = 0; i < secretCodeNumbers.length; i++) {
            if (secretCodeNumbers[i] == currentGuess[i]) {
                correctLocations++;
            }
        }

        // Count total correct numbers (including wrong positions)
        correctNumbers = countTotalCorrectNumbers(secretCodeNumbers, currentGuess, correctLocations);

        return new GuessResult(
                GameInputValidator.intArrayToString(currentGuess),
                correctNumbers,
                correctLocations,
                correctLocations == secretCodeNumbers.length
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
    public int[] getSecretCodeNumbers() { return secretCodeNumbers.clone(); }
    public List<GuessResult> getGuessHistory() { return Collections.unmodifiableList(guessHistory); }
    public int getAttemptsRemaining() { return attemptsRemaining; }
    public int getAttemptsMade() { return maxAttempts - attemptsRemaining; }
    public int getMaxAttempts() { return maxAttempts; }
    public int getCodeLength() { return codeLength; }
    public int getMaxNumber() { return maxNumber; }
    public boolean isGameEnded() { return gameEnded; }
    public boolean hasPlayerWon() { return playerWon; }

    /**
     * Represents the result of evaluating a player's guess against the secret code.
     * 
     * @param guess the original guess as a string (e.g., "1 2 3 4")
     * @param correctNumbers total count of correct numbers (regardless of position)
     * @param correctLocations count of numbers in the correct position
     * @param allCorrect true if this guess matches the secret code exactly
     */
    public record GuessResult(String guess, int correctNumbers, int correctLocations, boolean allCorrect) {

        /**
         * Generates human-readable feedback for this guess result.
         * 
         * @return a descriptive message about the guess accuracy
         */
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
