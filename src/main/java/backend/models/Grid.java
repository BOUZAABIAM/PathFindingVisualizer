package main.java.backend.models;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int size;
    private List<Cell> grid;

    public Grid(int size) {
        this.size = size;
        grid = new ArrayList<>();
        for(int i=0;i<size;i++){
            grid.add(new Cell(0));
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Cell> getGrid() {
        return grid;
    }

    public void setGrid(List<Cell> grid) {
        this.grid = grid;
    }
}
