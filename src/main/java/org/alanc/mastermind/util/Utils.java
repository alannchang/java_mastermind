package org.alanc.mastermind.util;

import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 * Utility class for console I/O operations.
 * Handles user input with graceful termination on EOF (Ctrl+D).
 */
public class Utils {

    /**
     * Reads a line of input from the scanner with graceful EOF handling.
     * 
     * @param scanner the input scanner
     * @param prompt the prompt to display to the user
     * @return the user's input string
     * @throws GameTerminatedException if EOF is detected (Ctrl+D)
     */
    public static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            } else {
                // Handle Ctrl+D (EOF)
                System.out.println("\nInput stream closed. Game will exit now.");
                throw new GameTerminatedException("Input stream closed");
            }
        } catch (NoSuchElementException e) {
            // Alternative handling for EOF
            System.out.println("\nInput stream closed. Game will exit now.");
            throw new GameTerminatedException("Input stream closed");
        }
    }

    /**
     * Reads an integer within a specified range with input validation.
     * 
     * @param scanner the input scanner
     * @param prompt the prompt to display to the user
     * @param min the minimum allowed value (inclusive)
     * @param max the maximum allowed value (inclusive)
     * @return a valid integer within the specified range
     * @throws GameTerminatedException if EOF is detected (Ctrl+D)
     */
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
