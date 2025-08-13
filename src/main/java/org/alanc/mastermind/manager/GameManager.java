package org.alanc.mastermind.manager;

import org.alanc.mastermind.random.MathRandomService;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.random.RandomOrgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class GameManager {
    private static final Logger logger = LoggerFactory.getLogger(GameManager.class);

    public GameManager () {}

    public void start() {
        logger.info("Printing random numbers.");

        RandomNumberService randomNumberService = new RandomOrgService();

        // RandomOrgService
        String randomNumbers = randomNumberService.generate(3, 0, 8);
        System.out.printf("Random.org generated random numbers:\n%s\n", randomNumbers);

        // MathRandomService
        randomNumberService = new MathRandomService();
        randomNumbers = randomNumberService.generate(3, 0, 8);
        System.out.printf("Pseudo random numbers: %s\n", randomNumbers);
    }


}
