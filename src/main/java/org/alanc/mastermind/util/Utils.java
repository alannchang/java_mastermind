package org.alanc.mastermind.util;

import java.util.Scanner;

public class Utils {

    public static String readLine(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}
