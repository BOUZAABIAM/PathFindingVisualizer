package main.java.backend.models;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private int size;
    private List<List<Cell>> grid;
    private double CANVAS_HEIGHT;
    private double CANVAS_WIDTH;

    public Grid(int size, double CANVAS_HEIGHT, double CANVAS_WIDTH) {
        this.size = size;
        this.CANVAS_HEIGHT = CANVAS_HEIGHT;
        this.CANVAS_WIDTH = CANVAS_WIDTH;
        double cellWidth = CANVAS_WIDTH/size;
        grid = new ArrayList<>();
        double y = 0;
        for(int i=0;i<size;i++){
            List<Cell> line  = new ArrayList<>();
            double x = 0;
            for(int j=0;j<size;j++){
                line.add(new Cell(x,y,0));
                x += cellWidth;
            }
            grid.add(line);
            y += cellWidth;
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<List<Cell>> getGrid() {
        return grid;
    }

    public void setGrid(List<List<Cell>> grid) {
        this.grid = grid;
    }

    public void tostring(){
        for(List<Cell> line :grid){
            for (Cell cell :line){
                System.out.print("(");
                System.out.print(cell.getX());
                System.out.print(",");
                System.out.print(cell.getY());
                System.out.print(",");
                System.out.print(cell.getValue());
                System.out.print(")"+"\t");
            }
            System.out.print("\n");
        }
    }

    public static void main(String[] args) {
        Grid grid = new Grid(10,10,10);
        grid.tostring();
    }
}
