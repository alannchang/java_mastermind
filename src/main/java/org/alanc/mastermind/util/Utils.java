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
                System.out.println("\nInput stream closed. Exiting game...");
                System.exit(0);
                return null; // This line will never be reached
            }
        } catch (NoSuchElementException e) {
            // Alternative handling for EOF
            System.out.println("\nInput stream closed. Exiting game...");
            System.exit(0);
            return null; // This line will never be reached
        }
    }
}
