package org.alanc.mastermind;

import org.alanc.mastermind.manager.GameManager;
import org.alanc.mastermind.random.MathRandomService;
import org.alanc.mastermind.random.RandomNumberService;
import org.alanc.mastermind.random.RandomOrgService;

import java.util.Random;

public class Main {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        gameManager.start();
    }
}