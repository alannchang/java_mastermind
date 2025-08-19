package org.alanc.mastermind.ui;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.alanc.mastermind.manager.GameManager;
import org.alanc.mastermind.persistence.GameRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import static org.alanc.mastermind.util.Utils.readLine;

/**
 * UI for displaying game history.
 */
public class GameHistoryUI {
    private static final Logger logger = LoggerFactory.getLogger(GameHistoryUI.class);
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Shows the game history display.
     * 
     * @param scanner the input scanner
     * @param gameManager the game manager to get history from
     */
    public static void show(Scanner scanner, GameManager gameManager) {
        logger.info("Displaying game history");
        
        List<GameRecord> games = gameManager.getAllGames();
        
        if (games.isEmpty()) {
            logger.info("No games found in history database");
            System.out.println("No games found in history.");
        } else {
            logger.info("Displaying {} games in history", games.size());
            System.out.println("GAME HISTORY");
            System.out.println("==============");
            System.out.println();
            
            for (int i = 0; i < games.size(); i++) {
                GameRecord game = games.get(i);
                System.out.printf("%d. %s | Status: %s | Started: %s |%n",
                    (i + 1),
                    formatGameInfo(game),
                    game.getStatus(),
                    game.getStartedAt().format(DATE_FORMAT)
                );
                
                String guessHistory = formatGuessHistory(game.getGuessesJson());
                if (!guessHistory.isEmpty()) {
                    System.out.printf("   Guesses:\n%s", guessHistory);
                }
                System.out.println();
            }
        }
        
        System.out.println();
        readLine(scanner, "Press Enter to return to Options menu\n");
    }

    private static String formatGameInfo(GameRecord game) {
        StringBuilder info = new StringBuilder();
        info.append("Code: ").append(game.getSecretCode());
        info.append(" | Config: ").append(game.getMaxAttempts()).append(" attempts, ");
        info.append(game.getCodeLength()).append(" digits, 0-").append(game.getMaxNumber());
        return info.toString();
    }

    private static String formatGuessHistory(String guessesJson) {
        try {
            if (guessesJson == null || guessesJson.trim().isEmpty()) {
                return "";
            }
            
            List<GuessResult> guesses = mapper.readValue(guessesJson,
                mapper.getTypeFactory().constructCollectionType(List.class, GuessResult.class));
            
            if (guesses.isEmpty()) {
                return "";
            }
            
            StringBuilder history = new StringBuilder();
            for (int i = 0; i < guesses.size(); i++) {
                GuessResult guess = guesses.get(i);
                history.append(String.format("   [%s: %d correct numbers, %d correct locations]\n",
                    guess.guess(), guess.correctNumbers(), guess.correctLocations()));
            }
            
            return history.toString();
        } catch (JsonProcessingException e) {
            logger.error("Failed to parse guess history: {}", guessesJson, e);
            return "";
        }
    }

    /**
     * Simple record to match the JSON structure of GuessResult.
     */
    public record GuessResult(String guess, int correctNumbers, int correctLocations, boolean allCorrect) {}

    private GameHistoryUI() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
