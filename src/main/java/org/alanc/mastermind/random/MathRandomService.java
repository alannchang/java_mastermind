package org.alanc.mastermind.random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Local pseudo-random number generator using Math.random().
 * Serves as a fallback when external random services are unavailable.
 */
public class MathRandomService implements RandomNumberService {
    private static final Logger logger = LoggerFactory.getLogger(MathRandomService.class);

    public MathRandomService() {}

    @Override
    public String generate (int quantity, int min, int max) {
        logger.debug("Generating {} pseudo-random numbers from {} to {} using Math.random()", quantity, min, max);

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
