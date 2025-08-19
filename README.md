# Mastermind

## Objective
To implement a Mastermind game, where a user plays against the computer and tries to guess a number combination or "code". At the end of each attempt, the computer will tell the player how many numbers are correct numbers and how many numbers are in the correct position/location. Players are only allowed a fixed number of attempts to correctly guess the number combination.

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
- [X] Logging using slf4j   (IN PROGRESS)
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


## Journal

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

### 8/19



