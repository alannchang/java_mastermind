# Mastermind

## Journal
### 8/11
- Created repo, added Gradle
- Added support for random number generation using Random.org API and local pseudo random generation as a fallback

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
- [ ] Logging

### Extensions (Optional)
- [ ] Add hint system
- [ ] Add configurable difficulty (number range / attempts)
- [ ] Add multiplayer mode
- [ ] Keep score across games
- [ ] Add timers (per guess or per game)
- [ ] Other creative features
