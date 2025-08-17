package org.alanc.mastermind.ui;

import org.alanc.mastermind.random.QuotaChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;
import static org.alanc.mastermind.util.Utils.*;

public class QuotaUI {
    private static final Logger logger = LoggerFactory.getLogger(QuotaUI.class);

    public static void show(Scanner scanner) {
        logger.info("Checking random.org API quota");

        System.out.println("Checking random.org API quota...");
        QuotaChecker quotaChecker = new QuotaChecker();
        int quota = quotaChecker.getQuota();

        if (quota > 0) {
            System.out.printf("✓ Current Random.org quota: %,d bits remaining\n", quota);
            logger.info("Random.org quota check successful: {} bits remaining", quota);

        } else if (quota == 0) {
            System.out.println("⚠ Your Random.org quota is exhausted.");
            System.out.println("The application will fall back to pseudo-random number generation.");
            logger.warn("Random.org quota exhausted");

        } else {
            // quota == -1 means error retrieving quota
            System.out.println("✗ Unable to check Random.org quota");
            System.out.println("Possible reasons:");
            System.out.println("  • No internet connection");
            System.out.println("  • Random.org service is temporarily unavailable");
            System.out.println("  • Network firewall is blocking the request");
            logger.warn("Failed to retrieve Random.org quota");
        }

        System.out.println();
        System.out.println("For more information, visit https://www.random.org/quota/");
        System.out.println();

        readLine(scanner, "Press Enter to return to Options menu\n");
    }

    private QuotaUI() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
