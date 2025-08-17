package org.alanc.mastermind.random;

public class TestRandomNumberService implements RandomNumberService {

    private final String predefinedResult;
    private final boolean simulateFailure;

    public TestRandomNumberService(String predefinedResult) {
        this(predefinedResult, false);
    }

    public TestRandomNumberService(String predefinedResult, boolean simulateFailure) {
        this.predefinedResult = predefinedResult;
        this.simulateFailure = simulateFailure;
    }

    @Override
    public String generate(int quantity, int min, int max) {
        if (simulateFailure) {
            return null; // Simulate generation failure
        }
        return predefinedResult;
    }
}