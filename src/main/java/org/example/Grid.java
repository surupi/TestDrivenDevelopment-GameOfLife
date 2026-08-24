package org.example;

public class Grid {

    private int rows;
    private int cols;
    private Cell[][] cells;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.cells = new Cell[rows][cols];

        // Initialize the grid with dead cells
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(false);
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean getCellState(int row, int col) {
        if (isCellWithinGrid(row, col)) {
            return cells[row][col].isAlive();
        }
        throw new IndexOutOfBoundsException("Cell out of bounds");
    }

    public void setCellState(int row, int col, boolean newState) {
        if (isCellWithinGrid(row, col)) {
            cells[row][col].setAlive(newState);
        } else {
            throw new IndexOutOfBoundsException("Cell out of bounds");
        }
    }

    public boolean isCellWithinGrid(int row, int col) {
        return 0 <= row && row < rows && 0 <= col && col < cols;
    }

    public int countLiveNeighbors(int row, int col) {
        int liveNeighbors = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!(i == 0 && j == 0) && isCellWithinGrid(row + i, col + j)) {
                    if (cells[row + i][col + j].isAlive()) {
                        liveNeighbors++;
                    }
                }
            }
        }
        return liveNeighbors;
    }


    public void updateGrid() {
        Cell[][] newCells = new Cell[rows][cols];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int liveNeighbors = countLiveNeighbors(row, col);
                boolean currentState = cells[row][col].isAlive();
                Cell newCell = new Cell(currentState);
                newCell.update(liveNeighbors);
                newCells[row][col] = newCell;
            }
        }
        cells = newCells;
    }

}
