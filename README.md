# Conway's Game of Life (TDD Implementation)

This repository contains an implementation of Conway's Game of Life, a classic cellular automaton built following Test-Driven Development (TDD) principles in Java with Gradle.

---

## 🧬 Conway's Game of Life Rules

Conway's Game of Life is a zero-player game simulating cellular evolution across discrete time steps:

1. **Underpopulation**: A live cell with fewer than two live neighbors dies.
2. **Survival**: A live cell with two or three live neighbors lives on to the next generation.
3. **Overpopulation**: A live cell with more than three live neighbors dies.
4. **Reproduction**: A dead cell with exactly three live neighbors becomes a live cell.

---

## 🧪 Test-Driven Development (TDD) Suite

The core domain logic is written in Java under `src/main/java` and fully tested under `src/test/java`.

### Running Unit Tests (Gradle)
To execute the JUnit 5 test suite:
```bash
./gradlew test
```

### Key Improvements & Refactoring
- **Delegated State Evolution**: `Grid.updateGrid()` delegates cell state calculations directly to `Cell.update(liveNeighbors)`.
- **Boundary Handling & Exceptions**: Unit test coverage verifies `IndexOutOfBoundsException` handling for grid boundaries.


