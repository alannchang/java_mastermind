# Mastermind

A console-based implementation of the classic Mastermind code-breaking game with database persistence, configurable difficulty, and Random.org API integration.

## Quick Start

```bash
# Clone and run (requires Java 17+)
git clone https://github.com/alannchang/java_mastermind
cd java_mastermind
./gradlew run

# Or build and run manually
./gradlew build
java -jar build/libs/java_mastermind-1.0-SNAPSHOT.jar
```

## Prerequisites

Required:
- Java 17 or higher (OpenJDK or Oracle JDK)
- Internet connection (for Random.org API - has local fallback)

Optional:
- Git (for cloning repository)
- IDE with Java support (IntelliJ IDEA, Eclipse, VS Code)

Verification:
```bash
java --version    # Should show Java 17+
./gradlew --version  # Gradle wrapper included
```

## Building and Running

### Option 1: Direct Run (Recommended)
```bash
./gradlew run --console=plain
```

### Option 2: Build then Run
```bash
# Build the application
./gradlew build

# Run the JAR file
java -jar build/libs/java_mastermind-1.0-SNAPSHOT.jar
```

### Example Gameplay:
```
WELCOME TO MASTERMIND!
You have 10 chances to guess the secret code.
The secret code consists of 4 integers from 0 to 7.
When entering your guess, please separate each integer with a single space.

What is the secret code? 1 2 3 4
1 correct number and 0 correct locations.
Try Again. Attempts Remaining: 9
```

#### Menu Options:
1) START NEW GAME
2) OPTIONS
3) ABOUT
4) QUIT

#### Options Menu:
1) CHANGE NUMBER OF ATTEMPTS
2) CHANGE SECRET CODE LENGTH
3) CHANGE SECRET CODE NUMBER RANGE
4) CHECK RANDOM.ORG API QUOTA
5) RESET TO DEFAULT SETTINGS
6) VIEW GAME HISTORY
7) CLEAR GAME HISTORY
8) RETURN TO MAIN MENU

## Architecture Overview

### Code Organization
```
src/main/java/org/alanc/mastermind/
├── config/           # Game configuration (GameConfig)
├── game/             # Core game logic (GameLogic, GameState, validation)
├── manager/          # Application management (GameManager lifecycle)
├── persistence/      # Database layer (SQLite DAO, converters, records)
├── random/           # Number generation (Random.org + Math.random fallback)
├── ui/               # User interface (menus, game history, resume functionality)
└── util/             # Shared utilities (error handling, I/O helpers)
```

### Key Design Patterns
- **Builder Pattern**: Immutable GameConfig construction with fluent API
- **Strategy Pattern**: Dual random number generation with fallback  
- **DAO Pattern**: Clean database access abstraction with SQLite
- **Dependency Injection**: Services injected into GameManager
- **Immutable Objects**: GameState, GameConfig for thread safety
- **Factory Methods**: ValidationResult, GameState creation
- **Auto-Closeable**: Proper resource management throughout

## Requirements Checklist

### Core Game Requirements
- [X] Randomly select a pattern of **4 numbers** (0–7) at game start (duplicates allowed)
- [X] Use **Random.org Integer Generator API** for number selection
- [X] Player has **10 attempts** to guess the combination
- [X] After each guess, provide feedback:
  - [X] Number of correct digits (regardless of position)
  - [X] Number of correct digits in the correct position
  - [X] "All incorrect" message if no matches
- [X] Do **not** reveal which numbers are correct
- [X] End game when:
  - [X] Player guesses correctly within 10 attempts (win)
  - [X] Player fails after 10 attempts (lose)

### User Interface
- [X] Allow player to enter 4-number guess
- [X] Display guess history with feedback
- [X] Show remaining attempts

### API Integration
- [X] Send HTTP GET request to `https://www.random.org/integers`
- [X] Parse API response into the secret combination

### Other
- [X] Tests using JUnit, etc.
- [X] Logging using slf4j
- [X] Error & exception handling
- [X] Input validation
- [X] Proper resource management
- [X] JavaDocs
- [X] Follow SOLID principles
  - [X] Single Responsibility
  - [X] Open/Closed Principle
  - [X] Liskov Substitution Principle
  - [X] Interface Segregation Principle
  - [X] Dependency Inversion Principle
 
### Extensions/Extras (Optional)
- [X] Add ability to check random.org api quota
- [X] Add configurable difficulty (number range / attempts)
- [X] Add ability to load/save games, history, etc. (persistence using a database)
- [ ] Add hint system
- [ ] Add multiplayer mode
- [ ] Keep score across games
- [ ] Add timers (per guess or per game)

## Creative Extensions

### Database Persistence
- SQLite database with auto-save on every guess
- Resume interrupted games automatically on startup
- Complete game history with detailed guess records and timestamps
- View and clear history via options menu

### Configurable Difficulty  
- Adjustable attempts (1-100), code length (1-100), number range (1-100)
- Settings persist between sessions
- Input validation for all parameters

### Dual Random Generation
- Primary: Random.org API for true randomness
- Fallback: Math.random() when API unavailable
- Quota monitoring and graceful degradation

## Development Journal

### 8/11
- Created repo, added Gradle
- Added support for random number generation using Random.org API and local pseudo random generation as a fallback

### 8/12
- Added class to manage game operations (GameManager)
- Added logging using slf4j
- Added QuotaChecker so users can check their random.org API, pulled it from RandomOrgService

### 8/13
- Added GameLogic to handle core game operations
- Added GameConfig (record? DTO? neither?) so user can set game parameters
- Added GameInputValidator and ValidationResult for validating player guesses
- Added GameState to store and track state
- Core game requirements satisfied

### 8/14
- Fixed bug in GameState that was breaking the build, game is playable now
- Added JUnit5 tests

### 8/15
- Added more tests
- Added mock tests for Http-based services (QuotaChecker, RandomOrgService)
- Added ErrorHandling for customizable error messages

### 8/16
- Added proper resource management at application level using try-with-resources 
- Application handles Ctrl+D
- Added UI menus so players can customize their games, check quota, etc.
- Added more tests

### 8/17
- Added end game menu and option to play again or exit to main menu
- GameConfig changed to pure builder pattern
- Removed game session logic from GameManager and created GameSession
- Added proper resource management for http client based services (RandomOrgService, QuotaChecker)
- Cleaned up main() entrypoint by creating MastermindApplication class

### 8/18
- Combined logic and game directory
- Added JavaDocs
- Added SQLite (jdbc) to store game state/data
- Added support for auto-saving and loading incomplete games
- Added support for viewing and deleting game state history
- Game history includes full guess history including feedback
- Added UI options/menus for resuming incomplete games, viewing and deleting history
- Added GameHistoryUI and ResumeGameUI components
- Added GameConverter for domain-persistence mapping
- Added proper JSON serialization of game data
- Added more logging
- Updated README.md

### 8/19

## Future Extensions

- Multiplayer mode using a game server that enables players to play against each other 
- Play against computer mode, where players play against AI-powered opponents

