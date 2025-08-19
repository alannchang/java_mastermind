package org.alanc.mastermind.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Simple DAO for game persistence using SQLite.
 * Handles database operations for saving and loading games.
 */
public class GameDAO implements AutoCloseable {
    private static final Logger logger = LoggerFactory.getLogger(GameDAO.class);
    private static final String DEFAULT_DB_URL = "jdbc:sqlite:mastermind_games.db";
    
    private Connection connection;
    private final String dbUrl;

    public GameDAO() {
        this(DEFAULT_DB_URL);
    }

    public GameDAO(String dbFileName) {
        this.dbUrl = "jdbc:sqlite:" + dbFileName;
        try {
            initializeDatabase();
        } catch (SQLException e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    private void initializeDatabase() throws SQLException {
        connection = DriverManager.getConnection(dbUrl);
        
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS games (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                secret_code TEXT NOT NULL,
                max_attempts INTEGER NOT NULL,
                code_length INTEGER NOT NULL,
                max_number INTEGER NOT NULL,
                status TEXT NOT NULL,
                started_at TEXT NOT NULL,
                completed_at TEXT,
                guesses_json TEXT
            )
            """;
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
            logger.debug("Database initialized successfully");
        }
    }

    /**
     * Saves a new game record and returns it with the generated ID.
     */
    public GameRecord saveGame(GameRecord record) {
        String sql = """
            INSERT INTO games (secret_code, max_attempts, code_length, max_number, 
                             status, started_at, completed_at, guesses_json)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, record.getSecretCode());
            stmt.setInt(2, record.getMaxAttempts());
            stmt.setInt(3, record.getCodeLength());
            stmt.setInt(4, record.getMaxNumber());
            stmt.setString(5, record.getStatus());
            stmt.setString(6, record.getStartedAt().toString());
            stmt.setString(7, record.getCompletedAt() != null ? record.getCompletedAt().toString() : null);
            stmt.setString(8, record.getGuessesJson());
            
            stmt.executeUpdate();
            
            // Get the generated ID using SQLite's last_insert_rowid()
            try (PreparedStatement idStmt = connection.prepareStatement("SELECT last_insert_rowid()");
                 ResultSet rs = idStmt.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    logger.debug("Saved game record with ID: {}", id);
                    // Return new record with generated ID
                    return new GameRecord(id, record.getSecretCode(), record.getMaxAttempts(), 
                                        record.getCodeLength(), record.getMaxNumber(), record.getStatus(),
                                        record.getStartedAt(), record.getCompletedAt(), record.getGuessesJson());
                }
            }
            throw new RuntimeException("Failed to retrieve generated ID for new game record");
        } catch (SQLException e) {
            logger.error("Failed to save game record", e);
            throw new RuntimeException("Failed to save game record", e);
        }
    }

    /**
     * Updates an existing game record.
     */
    public void updateGame(GameRecord record) {
        String sql = """
            UPDATE games SET status = ?, completed_at = ?, guesses_json = ?
            WHERE id = ?
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, record.getStatus());
            stmt.setString(2, record.getCompletedAt() != null ? record.getCompletedAt().toString() : null);
            stmt.setString(3, record.getGuessesJson());
            stmt.setLong(4, record.getId());
            
            stmt.executeUpdate();
            logger.debug("Updated game record ID: {}", record.getId());
        } catch (SQLException e) {
            logger.error("Failed to update game record ID: {}", record.getId(), e);
            throw new RuntimeException("Failed to update game record", e);
        }
    }

    /**
     * Checks if the most recent game is incomplete.
     */
    public boolean isLastGameIncomplete() {
        String sql = """
            SELECT status FROM games 
            ORDER BY started_at DESC 
            LIMIT 1
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                String status = rs.getString("status");
                return GameStatus.IN_PROGRESS.name().equals(status);
            }
        } catch (SQLException e) {
            logger.error("Failed to check if last game is incomplete", e);
            throw new RuntimeException("Failed to check if last game is incomplete", e);
        }
        return false; // No games exist
    }

    /**
     * Gets the most recent game record.
     */
    public Optional<GameRecord> getLastGame() {
        String sql = """
            SELECT * FROM games 
            ORDER BY started_at DESC 
            LIMIT 1
            """;
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return Optional.of(mapResultSetToRecord(rs));
            }
        } catch (SQLException e) {
            logger.error("Failed to get last game", e);
            throw new RuntimeException("Failed to get last game", e);
        }
        return Optional.empty();
    }

    /**
     * Gets all game records ordered by start time (newest first).
     */
    public List<GameRecord> getAllGames() {
        String sql = "SELECT * FROM games ORDER BY started_at DESC";
        List<GameRecord> games = new ArrayList<>();
        
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                games.add(mapResultSetToRecord(rs));
            }
        } catch (SQLException e) {
            logger.error("Failed to retrieve all games", e);
            throw new RuntimeException("Failed to retrieve all games", e);
        }
        
        logger.debug("Retrieved {} game records", games.size());
        return games;
    }

    /**
     * Deletes all game records.
     */
    public void deleteAllGames() {
        try (Statement stmt = connection.createStatement()) {
            int count = stmt.executeUpdate("DELETE FROM games");
            logger.info("Deleted {} game records", count);
        } catch (SQLException e) {
            logger.error("Failed to delete all games", e);
            throw new RuntimeException("Failed to delete all games", e);
        }
    }

    private GameRecord mapResultSetToRecord(ResultSet rs) throws SQLException {
        return new GameRecord(
            rs.getLong("id"),
            rs.getString("secret_code"),
            rs.getInt("max_attempts"),
            rs.getInt("code_length"),
            rs.getInt("max_number"),
            rs.getString("status"),
            LocalDateTime.parse(rs.getString("started_at")),
            rs.getString("completed_at") != null ? LocalDateTime.parse(rs.getString("completed_at")) : null,
            rs.getString("guesses_json")
        );
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.debug("Database connection closed");
            }
        } catch (SQLException e) {
            logger.warn("Error closing database connection", e);
        }
    }
}
