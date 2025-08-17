package org.alanc.mastermind.util;

import java.util.Scanner;
import java.util.NoSuchElementException;

public class Utils {

    public static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                // Handle Ctrl+D (EOF)
                System.out.println("\nInput stream closed. Game will exit now.");
                System.exit(0);
                return null; // This line will never be reached
            }
        } catch (NoSuchElementException e) {
            // Alternative handling for EOF
            System.out.println("\nInput stream closed. Game will exit now.");
            System.exit(0);
            return null; // This line will never be reached
        }
    }

    public static int readNumberInRange(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            String input = readLine(scanner, prompt);
            try {
                int number = Integer.parseInt(input);
                if (number >= min && number <= max) {
                    return number;
                } else {
                    System.out.println("Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e){
                System.out.println("Invalid entry. Please try again.");
            }
        }


    }
}
