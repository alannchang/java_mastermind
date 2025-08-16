# Mastermind

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
- Added more tests (MathRandomService, RandomOrgService, etc.)
- 

### 8/16

### 8/17

### 8/18

### 8/19

## Checklist
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
- [ ] Automated tests, unit tests, etc.   (IN PROGRESS)
- [X] Logging using slf4j   (IN PROGRESS)
- [ ] Error & exception handling   (IN PROGRESS)
- [X] Input validation

### Extensions/Extras (Optional)
- [X] Add ability to check random.org api quota
- [ ] Add ability to load/save games, history, etc. (persistence using a database)
- [ ] Add hint system
- [ ] Add configurable difficulty (number range / attempts)
- [ ] Add multiplayer mode
- [ ] Keep score across games
- [ ] Add timers (per guess or per game)
