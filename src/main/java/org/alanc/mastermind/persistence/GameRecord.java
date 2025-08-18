package org.alanc.mastermind.persistence;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a game record in the database.
 * Contains game state, configuration, and metadata for persistence.
 */
public class GameRecord {
    private final Long id;
    private final String secretCode;
    private final int maxAttempts;
    private final int codeLength;
    private final int maxNumber;
    private final String status; // IN_PROGRESS, WON, LOST
    private final LocalDateTime startedAt;
    private final LocalDateTime completedAt;
    private final String guessesJson; // JSON string of guess history

    public GameRecord(Long id, String secretCode, int maxAttempts, int codeLength, 
                     int maxNumber, String status, LocalDateTime startedAt, 
                     LocalDateTime completedAt, String guessesJson) {
        this.id = id;
        this.secretCode = secretCode;
        this.maxAttempts = maxAttempts;
        this.codeLength = codeLength;
        this.maxNumber = maxNumber;
        this.status = status;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.guessesJson = guessesJson;
    }

    // Getters
    public Long getId() { return id; }
    public String getSecretCode() { return secretCode; }
    public int getMaxAttempts() { return maxAttempts; }
    public int getCodeLength() { return codeLength; }
    public int getMaxNumber() { return maxNumber; }
    public String getStatus() { return status; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public String getGuessesJson() { return guessesJson; }
}
