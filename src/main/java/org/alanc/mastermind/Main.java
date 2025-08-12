package org.alanc.mastermind;

import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.random.RandomOrgService;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        System.out.println("Let's see if we can print some random numbers from Random.org!");
        RandomNumberService randomNumberService = new RandomOrgService();
        String randomNumbers = randomNumberService.generate(4, 0, 8);
        System.out.print(randomNumbers);
    }
}