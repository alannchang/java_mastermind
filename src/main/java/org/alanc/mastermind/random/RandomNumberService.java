package org.alanc.mastermind.random;

public interface RandomNumberService extends AutoCloseable {
    String generate(int quantity, int min, int max);
    
    @Override
    default void close() throws Exception {
        // Default no-op implementation for services that don't need cleanup
    }
}
