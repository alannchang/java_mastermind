package org.alanc.mastermind.util;

/**
 * Exception thrown when the game should terminate gracefully.
 * Used for user-initiated exits (Ctrl+D, quit commands) to avoid abrupt shutdowns.
 */
public class GameTerminatedException extends RuntimeException {
    public GameTerminatedException(String message) {
        super(message);
    }
}
