package org.alanc.mastermind.manager;

import org.alanc.mastermind.random.MathRandomService;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.random.RandomOrgService;

public class GameManager {
    public GameManager () {}

    public void start() {
        System.out.println("Let's see if we can print some random numbers from Random.org!");

        RandomNumberService randomNumberService = new RandomOrgService();

        // RandomOrgService
        String randomNumbers = randomNumberService.generate(3, 0, 8);
        System.out.printf("Random.org generated random numbers: %s\n", randomNumbers);

        // MathRandomService
        randomNumberService = new MathRandomService();
        randomNumbers = randomNumberService.generate(3, 0, 8);
        System.out.printf("Pseudo random numbers: %s\n", randomNumbers);
    }


}
