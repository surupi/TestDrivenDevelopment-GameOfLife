package org.example;

public class Cell {
    private boolean alive;

    public Cell(boolean initialState) {
        this.alive = initialState;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }



    public void update(int liveNeighbors) {
        if (this.alive) {
            // Live cell dies if it has fewer than 2 or more than 3 live neighbors
            this.alive = (liveNeighbors == 2 || liveNeighbors == 3);
        } else {
            // Dead cell becomes alive if it has exactly 3 live neighbors
            this.alive = (liveNeighbors == 3);
        }
    }


}
