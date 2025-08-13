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

### 8/14

### 8/15

### 8/16

### 8/17

### 8/18

### 8/19

## Checklist
### Core Game Requirements
- [ ] Randomly select a pattern of **4 numbers** (0–7) at game start (duplicates allowed)
- [X] Use **Random.org Integer Generator API** for number selection
- [ ] Player has **10 attempts** to guess the combination
- [ ] After each guess, provide feedback:
  - [ ] Number of correct digits (regardless of position)
  - [ ] Number of correct digits in the correct position
  - [ ] "All incorrect" message if no matches
- [ ] Do **not** reveal which numbers are correct
- [ ] End game when:
  - [ ] Player guesses correctly within 10 attempts (win)
  - [ ] Player fails after 10 attempts (lose)

### User Interface
- [ ] Allow player to enter 4-number guess
- [ ] Display guess history with feedback
- [ ] Show remaining attempts

### API Integration
- [X] Send HTTP GET request to `https://www.random.org/integers`
- [X] Parse API response into the secret combination

### Other
- [ ] Automated Tests (Unit tests?)
- [X] Logging using slf4j
- [ ] Error & exception handling
- [ ] Input validation

### Extensions/Extras (Optional)
- [X] Add ability to check random.org api quota
- [ ] Add hint system
- [ ] Add configurable difficulty (number range / attempts)
- [ ] Add multiplayer mode
- [ ] Keep score across games
- [ ] Add timers (per guess or per game)
- [ ] Other creative features
