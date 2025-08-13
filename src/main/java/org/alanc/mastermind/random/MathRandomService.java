package org.alanc.mastermind.random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MathRandomService implements RandomNumberService {
    private static final Logger logger = LoggerFactory.getLogger(MathRandomService.class);

    public MathRandomService() {}

    @Override
    public String generate (int quantity, int min, int max) {
        logger.debug("Generating {} pseudo-random numbers using Math.random()", quantity);

        StringBuilder pseudoRandomNumbers = new StringBuilder();
        for (int i = 0; i < quantity; i++) {
            if (i > 0) {
                pseudoRandomNumbers.append(" ");
            }
            int randomNumber = (int) (Math.random() * (max - min + 1)) + min;
            pseudoRandomNumbers.append(randomNumber);
        }

        String pseudoRandomNumberString = pseudoRandomNumbers.toString();
        logger.debug("Successfully generated {} pseudo-random numbers: {}", quantity, pseudoRandomNumberString);
        return pseudoRandomNumberString;
    }
}
