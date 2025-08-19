package org.alanc.mastermind.game;

import org.alanc.mastermind.config.GameConfig;
import org.alanc.mastermind.game.GameLogic;
import org.alanc.mastermind.game.GameState;
import org.alanc.mastermind.persistence.GamePersistenceService;
import org.alanc.mastermind.persistence.GameRecord;
import org.alanc.mastermind.ui.GameUI;
import org.alanc.mastermind.util.ErrorHandler;
import org.alanc.mastermind.util.GameTerminatedException;
import org.alanc.mastermind.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * Manages the execution of individual Mastermind game sessions.
 * Handles the game loop, user input, and round-by-round gameplay.
 */
public class GameSession {
    private static final Logger logger = LoggerFactory.getLogger(GameSession.class);
    
    private final GameLogic gameLogic;
    private final Scanner scanner;
    private final GamePersistenceService persistenceService;

    public GameSession(GameLogic gameLogic, Scanner scanner, GamePersistenceService persistenceService) {
        this.gameLogic = gameLogic;
        this.scanner = scanner;
        this.persistenceService = persistenceService;
    }

    public void play(GameConfig config) {
        GameState initialState = gameLogic.createNewGame(config);
        LocalDateTime startTime = LocalDateTime.now();
        GameRecord savedRecord = persistenceService.saveNewGame(initialState, startTime);
        Long gameId = savedRecord.getId();
        
        while (true) {
            GameState endState = playOneRound(initialState, gameId, startTime);
            
            // Mark game as completed
            persistenceService.updateGame(endState, gameId, startTime);
            
            GameUI.showEndGameMessage(endState);
            
            if (!GameUI.showEndGameMenu(scanner)) {
                return; // Exit to main menu
            }
            
            // Start new round with same configuration
            initialState = gameLogic.createNewGame(config);
            startTime = LocalDateTime.now();
            savedRecord = persistenceService.saveNewGame(initialState, startTime);
            gameId = savedRecord.getId();
        }
    }

    /**
     * Resumes a game from an existing game state.
     */
    public void resumeGame(GameState resumedState, GameConfig config, Long gameId, LocalDateTime startTime) {
        logger.info("Resuming game with {} attempts remaining", resumedState.getAttemptsRemaining());
        
        while (true) {
            GameState endState = playOneRound(resumedState, gameId, startTime);
            
            // Mark game as completed
            persistenceService.updateGame(endState, gameId, startTime);
            
            GameUI.showEndGameMessage(endState);
            
            if (!GameUI.showEndGameMenu(scanner)) {
                return; // Exit to main menu
            }
            
            // Start new round with same configuration
            resumedState = gameLogic.createNewGame(config);
            startTime = LocalDateTime.now();
            GameRecord savedRecord = persistenceService.saveNewGame(resumedState, startTime);
            gameId = savedRecord.getId();
        }
    }

    private GameState playOneRound(GameState gameState, Long gameId, LocalDateTime startTime) {
        GameUI.showWelcomeMessage(gameState.getAttemptsRemaining(), gameState.getCodeLength(), gameState.getMaxNumber());

        while (!gameState.isGameEnded()) {
            String playerGuess = null;
            try {
                playerGuess = Utils.readLine(scanner, "What is the secret code? ");
                gameState = gameLogic.processGuess(gameState, playerGuess);

                // Auto-save after each guess
                persistenceService.updateGame(gameState, gameId, startTime);

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
