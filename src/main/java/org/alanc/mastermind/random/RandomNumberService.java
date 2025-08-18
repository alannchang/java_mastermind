package org.alanc.mastermind.random;

/**
 * Service interface for generating random numbers for Mastermind games.
 * Supports different implementations (external APIs, local generators, test mocks).
 */
public interface RandomNumberService extends AutoCloseable {
    
    /**
     * Generates a sequence of random numbers within the specified range.
     * 
     * @param quantity the number of random numbers to generate (must be positive)
     * @param min the minimum value (inclusive)
     * @param max the maximum value (inclusive)
     * @return space-separated string (e.g., "3 7 1 4") or null if generation fails
     */
    String generate(int quantity, int min, int max);
    
    /**
     * Closes any resources held by this service.
     * Default implementation is a no-op for services that don't require cleanup.
     * 
     * @throws Exception if an error occurs during resource cleanup
     */
    @Override
    default void close() throws Exception {
        // Default no-op implementation for services that don't need cleanup
    }
}
