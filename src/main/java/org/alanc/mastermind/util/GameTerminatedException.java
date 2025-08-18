package org.alanc.mastermind.util;

/**
 * Exception thrown when the game should terminate gracefully
 * (e.g., when user presses Ctrl+D or input stream is closed)
 */
public class GameTerminatedException extends RuntimeException {
    public GameTerminatedException(String message) {
        super(message);
    }
}
