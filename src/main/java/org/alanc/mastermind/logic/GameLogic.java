package org.alanc.mastermind.logic;

import org.alanc.mastermind.manager.GameManager;
import org.alanc.mastermind.random.MathRandomService;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.random.RandomOrgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class GameLogic {
    private static final Logger logger = LoggerFactory.getLogger(GameLogic.class);

    private RandomNumberService randomNumberService;

    public GameLogic() {}

    public void startNewGame() {
        System.out.println("Starting new game!");
        String code = generateSecretCode(4, 0 , 8);
        System.out.printf("Secret Code is: %s\n", code);
    }

    private String generateSecretCode(int quantity, int min, int max) {
        RandomNumberService randomNumberService = new RandomOrgService();
        String code = randomNumberService.generate(quantity, min, max);

        // Fallback to Math.random
        if (code == null) {
            randomNumberService = new MathRandomService();
            code = randomNumberService.generate(quantity, min, max);
        }

        return code;
    }
}
