package org.example;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {

    @Test
    @DisplayName("Initialize a grid with all dead cells and confirm its dimensions and state")
    public void initializeGridTest() {
        Grid grid = new Grid(5, 5);
        assertEquals(5, grid.getRows());
        assertEquals(5, grid.getCols());
        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                assertFalse(grid.getCellState(row, col));
            }
        }
    }

    @Test
    @DisplayName("Set and retrieve the state of a specific cell in a grid")
    public void testSetGridStatus() {
        Grid grid = new Grid(5, 5);
        grid.setCellState(3, 4, true);
        assertTrue(grid.getCellState(3, 4));
    }

    @DisplayName("Should set and retrieve the state of a specific cell in the grid (Parameterized)")
    @ParameterizedTest(name = "Should set and retrieve the state of a specific cell in the grid")
    @CsvSource({"3, 4", "0, 0", "2, 2"})
    public void testSetGridCellState(int row, int col) {
        Grid grid = new Grid(5, 5);
        grid.setCellState(row, col, true);
        assertTrue(grid.getCellState(row, col));
    }

    @Test
    @DisplayName("Should determine if a given cell is within the grid")
    public void testCellWithinGrid() {
        Grid grid = new Grid(5, 5);
        assertTrue(grid.isCellWithinGrid(2, 3));
        assertTrue(grid.isCellWithinGrid(0, 0));
        assertTrue(grid.isCellWithinGrid(4, 4));
    }

    @Test
    @DisplayName("Should determine if a given cell is outside the grid")
    public void testCellOutsideGrid() {
        Grid grid = new Grid(5, 5);
        assertFalse(grid.isCellWithinGrid(5, 5));
        assertFalse(grid.isCellWithinGrid(-1, 3));
        assertFalse(grid.isCellWithinGrid(3, -1));
        assertFalse(grid.isCellWithinGrid(6, 1));
        assertFalse(grid.isCellWithinGrid(2, 6));

        assertThrows(IndexOutOfBoundsException.class, () -> grid.getCellState(-1, 0));
        assertThrows(IndexOutOfBoundsException.class, () -> grid.setCellState(5, 5, true));
    }

    @Test
    @DisplayName("Correctly calculate the number of live neighbors for a cell in the middle of the grid")
    public void testCountLiveNeighbors() {
        Grid grid = new Grid(3, 3);
        grid.setCellState(0, 0, true);
        grid.setCellState(0, 1, true);
        grid.setCellState(1, 1, true);
        int liveNeighbors = grid.countLiveNeighbors(1, 1);
        assertEquals(2, liveNeighbors);
    }

    @Test
    @DisplayName("Correctly calculate the number of live neighbors for a cell at the edge of the grid")
    public void testCountLiveNeighborsAtEdge() {
        Grid grid = new Grid(3, 3);
        grid.setCellState(0, 1, true);
        grid.setCellState(0, 0, true);
        grid.setCellState(0, 2, true);
        grid.setCellState(1, 1, true);
        int liveNeighbors = grid.countLiveNeighbors(0, 1);
        assertEquals(3, liveNeighbors);
    }

    @Test
    @DisplayName("Correctly calculate the number of live neighbors for a corner cell")
    public void testCountLiveNeighborsAtCorner() {
        Grid grid = new Grid(3, 3);
        grid.setCellState(0, 1, true);
        grid.setCellState(1, 0, true);
        grid.setCellState(1, 1, true);
        int liveNeighbors = grid.countLiveNeighbors(0, 0);
        assertEquals(3, liveNeighbors);
    }

    @Test
    @DisplayName("Handles neighbor calculation with grid boundaries (dead cells outside the grid)")
    public void testCountLiveNeighborsWithGridBoundaries() {
        Grid grid = new Grid(3, 3);
        grid.setCellState(0, 1, true);
        grid.setCellState(1, 0, true);
        grid.setCellState(1, 1, true);
        int liveNeighbors = grid.countLiveNeighbors(0, 0);
        assertEquals(3, liveNeighbors);
        grid = new Grid(3, 3);
        grid.setCellState(1, 2, true);
        liveNeighbors = grid.countLiveNeighbors(0, 2);
        assertEquals(1, liveNeighbors);
    }

    @Test
    @DisplayName("Apply rules to the entire grid and produce the next generation")
    public void testGridUpdateToNextGeneration() {
        Grid grid = new Grid(3, 3);
        grid.setCellState(1, 0, true);
        grid.setCellState(1, 1, true);
        grid.setCellState(1, 2, true);

        assertTrue(grid.getCellState(1, 0));
        assertTrue(grid.getCellState(1, 1));
        assertTrue(grid.getCellState(1, 2));

        grid.updateGrid();

        assertFalse(grid.getCellState(1, 0));
        assertTrue(grid.getCellState(0, 1));
        assertTrue(grid.getCellState(1, 1));
        assertTrue(grid.getCellState(2, 1));
        assertFalse(grid.getCellState(1, 2));
    }


    @Test
    @DisplayName("Verify that an isolated live cell dies in the next generation")
    public void testIsolatedLiveCellDies() {
        Grid grid = new Grid(3, 3);
        grid.setCellState(1, 1, true);
        assertTrue(grid.getCellState(1, 1));
        grid.updateGrid();
        assertFalse(grid.getCellState(1, 1));
    }

    @Test
    @DisplayName("Verify that a stable block structure remains unchanged over generations")
    public void testStableBlockStructure() {
        Grid grid = new Grid(4, 4);
        grid.setCellState(1, 1, true);
        grid.setCellState(1, 2, true);
        grid.setCellState(2, 1, true);
        grid.setCellState(2, 2, true);

        assertTrue(grid.getCellState(1, 1));
        assertTrue(grid.getCellState(1, 2));
        assertTrue(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(2, 2));

        grid.updateGrid();

        assertTrue(grid.getCellState(1, 1));
        assertTrue(grid.getCellState(1, 2));
        assertTrue(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(2, 2));

        grid.updateGrid();

        assertTrue(grid.getCellState(1, 1));
        assertTrue(grid.getCellState(1, 2));
        assertTrue(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(2, 2));
    }

    @Test
    @DisplayName("Verify that an oscillating blinker structure oscillates correctly over generations")
    public void testOscillatingBlinker() {
        Grid grid = new Grid(5, 5);

        grid.setCellState(2, 1, true);
        grid.setCellState(2, 2, true);
        grid.setCellState(2, 3, true);

        assertTrue(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(2, 2));
        assertTrue(grid.getCellState(2, 3));

        grid.updateGrid();

        assertFalse(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(1, 2));
        assertTrue(grid.getCellState(2, 2));
        assertTrue(grid.getCellState(3, 2));
        assertFalse(grid.getCellState(2, 3));

        grid.updateGrid();

        assertTrue(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(2, 2));
        assertTrue(grid.getCellState(2, 3));
        assertFalse(grid.getCellState(1, 2));
        assertFalse(grid.getCellState(3, 2));
    }

    @Test
    @DisplayName("A blinker (a line of three live cells) should oscillate between horizontal and vertical")
    public void testBlinkerOscillatesBetweenHorizontalAndVertical() {
        Grid grid = new Grid(5, 5);

        grid.setCellState(2, 1, true);
        grid.setCellState(2, 2, true);
        grid.setCellState(2, 3, true);

        assertTrue(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(2, 2));
        assertTrue(grid.getCellState(2, 3));

        grid.updateGrid();

        assertFalse(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(1, 2));
        assertTrue(grid.getCellState(2, 2));
        assertTrue(grid.getCellState(3, 2));
        assertFalse(grid.getCellState(2, 3));

        grid.updateGrid();

        assertTrue(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(2, 2));
        assertTrue(grid.getCellState(2, 3));
        assertFalse(grid.getCellState(1, 2));
        assertFalse(grid.getCellState(3, 2));
    }

    @Test
    @DisplayName("An empty grid should remain empty over generations")
    public void testEmptyGridRemainsEmpty() {
        Grid grid = new Grid(5, 5);

        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                assertFalse(grid.getCellState(row, col));
            }
        }

        for (int i = 0; i < 3; i++) {
            grid.updateGrid();
        }

        for (int row = 0; row < grid.getRows(); row++) {
            for (int col = 0; col < grid.getCols(); col++) {
                assertFalse(grid.getCellState(row, col));
            }
        }
    }

    @Test
    @DisplayName("Ensure Game of Life rules apply correctly on a 3x5 grid")
    public void nonSquareGridTest() {
        Grid grid = new Grid(3, 5);

        grid.setCellState(0, 1, true);
        grid.setCellState(0, 2, true);
        grid.setCellState(1, 1, true);
        grid.setCellState(1, 2, true);

        grid.updateGrid();
        assertFalse(grid.getCellState(0, 0));
        assertTrue(grid.getCellState(0, 1));
        assertTrue(grid.getCellState(0, 2));
        assertFalse(grid.getCellState(0, 3));
        assertFalse(grid.getCellState(0, 4));
        assertFalse(grid.getCellState(1, 0));
        assertTrue(grid.getCellState(1, 1));
        assertTrue(grid.getCellState(1, 2));
        assertFalse(grid.getCellState(1, 3));
        assertFalse(grid.getCellState(1, 4));
        assertFalse(grid.getCellState(2, 0));
        assertFalse(grid.getCellState(2, 1));
        assertFalse(grid.getCellState(2, 2));
        assertFalse(grid.getCellState(2, 3));
        assertFalse(grid.getCellState(2, 4));

        grid.updateGrid();
        assertFalse(grid.getCellState(0, 0));
        assertTrue(grid.getCellState(0, 1));
        assertTrue(grid.getCellState(0, 2));
        assertFalse(grid.getCellState(0, 3));
        assertFalse(grid.getCellState(0, 4));
        assertFalse(grid.getCellState(1, 0));
        assertTrue(grid.getCellState(1, 1));
        assertTrue(grid.getCellState(1, 2));
        assertFalse(grid.getCellState(1, 3));
        assertFalse(grid.getCellState(1, 4));
        assertFalse(grid.getCellState(2, 0));
        assertFalse(grid.getCellState(2, 1));
        assertFalse(grid.getCellState(2, 2));
        assertFalse(grid.getCellState(2, 3));
        assertFalse(grid.getCellState(2, 4));
    }

    @Test
    @DisplayName("Verify Blinker pattern oscillates and then stabilizes")
    public void testBlinkerPatternOscillatesAndStabilizes() {
        Grid grid = new Grid(5, 5);

        grid.setCellState(2, 1, true);
        grid.setCellState(2, 2, true);
        grid.setCellState(2, 3, true);

        assertTrue(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(2, 2));
        assertTrue(grid.getCellState(2, 3));
        assertFalse(grid.getCellState(1, 2));
        assertFalse(grid.getCellState(3, 2));

        grid.updateGrid();

        assertFalse(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(1, 2));
        assertTrue(grid.getCellState(2, 2));
        assertTrue(grid.getCellState(3, 2));
        assertFalse(grid.getCellState(2, 3));

        grid.updateGrid();

        assertTrue(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(2, 2));
        assertTrue(grid.getCellState(2, 3));
        assertFalse(grid.getCellState(1, 2));
        assertFalse(grid.getCellState(3, 2));

        grid.updateGrid();

        assertFalse(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(1, 2));
        assertTrue(grid.getCellState(2, 2));
        assertTrue(grid.getCellState(3, 2));
        assertFalse(grid.getCellState(2, 3));
    }

    @Test
    @DisplayName("Verify Glider spaceship movement across the grid")
    public void testGliderMovement() {
        // Create a grid of sufficient size to allow Glider movement
        Grid grid = new Grid(5, 5);  // 5x5 grid for the Glider to move

        grid.setCellState(0, 1, true);  // .O.
        grid.setCellState(1, 2, true);  // ..O
        grid.setCellState(2, 0, true);  // OOO
        grid.setCellState(2, 1, true);
        grid.setCellState(2, 2, true);

        grid.updateGrid();

        assertTrue(grid.getCellState(1, 0));
        assertTrue(grid.getCellState(1, 2));
        assertTrue(grid.getCellState(2, 1));
        assertTrue(grid.getCellState(2, 2));
        assertTrue(grid.getCellState(3, 1));

        assertFalse(grid.getCellState(0, 1));
        assertFalse(grid.getCellState(2, 0));
    }



    @Test
    @DisplayName("Ensure all cells die due to overcrowding when the grid is full of live cells")
    public void testFullGridDiesDueToOvercrowding() {
        int rows = 4;
        int cols = 4;
        Grid grid = new Grid(rows, cols);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                grid.setCellState(row, col, true);
            }
        }

        grid.updateGrid();
        grid.updateGrid();
        grid.updateGrid();
        grid.updateGrid();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                assertFalse(grid.getCellState(row, col), "Cell at (" + row + ", " + col + ") should be dead due to overcrowding.");
            }
        }
    }
}