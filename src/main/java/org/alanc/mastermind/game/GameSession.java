package org.alanc.mastermind.game;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.game.GameLogic;
import org.alanc.mastermind.game.GameState;
import org.alanc.mastermind.ui.GameUI;
import org.alanc.mastermind.util.ErrorHandler;
import org.alanc.mastermind.util.GameTerminatedException;
import org.alanc.mastermind.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Scanner;

/**
 * Manages the execution of individual Mastermind game sessions.
 * Handles the game loop, user input, and round-by-round gameplay.
 */
public class GameSession {
    private static final Logger logger = LoggerFactory.getLogger(GameSession.class);
    
    private final GameLogic gameLogic;
    private final Scanner scanner;

    public GameSession(GameLogic gameLogic, Scanner scanner) {
        this.gameLogic = gameLogic;
        this.scanner = scanner;
    }

    public void play(GameConfig config) {
        GameState initialState = gameLogic.createNewGame(config);
        
        while (true) {
            GameState endState = playOneRound(initialState);
            
            GameUI.showEndGameMessage(endState);
            
            if (!GameUI.showEndGameMenu(scanner)) {
                return; // Exit to main menu
            }
            
            // Start new round with same configuration
            initialState = gameLogic.createNewGame(config);
        }
    }

    /**
     * Resumes a game from an existing game state.
     */
    public void resumeGame(GameState resumedState, GameConfig config) {
        logger.info("Resuming game with {} attempts remaining", resumedState.getAttemptsRemaining());
        
        while (true) {
            GameState endState = playOneRound(resumedState);
            
            GameUI.showEndGameMessage(endState);
            
            if (!GameUI.showEndGameMenu(scanner)) {
                return; // Exit to main menu
            }
            
            // Start new round with same configuration
            resumedState = gameLogic.createNewGame(config);
        }
    }

    private GameState playOneRound(GameState gameState) {
        GameUI.showWelcomeMessage(gameState.getMaxAttempts(), gameState.getCodeLength(), gameState.getMaxNumber());

        while (!gameState.isGameEnded()) {
            String playerGuess = null;
            try {
                playerGuess = Utils.readLine(scanner, "What is the secret code? ");
                gameState = gameLogic.processGuess(gameState, playerGuess);

                // Show feedback for the most recent guess
                if (!gameState.getGuessHistory().isEmpty()) {
                    GameState.GuessResult latestGuess =
                            gameState.getGuessHistory().get(gameState.getGuessHistory().size() - 1);
                    GameUI.showGuessResult(latestGuess, gameState.getAttemptsRemaining());
                }
            } catch (IllegalArgumentException e) {
                ErrorHandler.handleInputValidationError(logger, playerGuess, e.getMessage());
            } catch (GameTerminatedException e) {
                logger.info("Game session terminated by user input: {}", e.getMessage());
                throw e; // Re-throw to be handled at application level
            }
        }
        return gameState;
    }
}
