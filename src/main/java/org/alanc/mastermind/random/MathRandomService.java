package org.alanc.mastermind.random;

public class MathRandomService implements RandomNumberService {
    public MathRandomService() {}

    @Override
    public String generate (int quantity, int min, int max) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < quantity; i++) {
            if (i > 0) {
                result.append(" ");
            }
            int randomNumber = (int) (Math.random() * (max - min + 1)) + min;
            result.append(randomNumber);
        }
        return result.toString();
    }
}
