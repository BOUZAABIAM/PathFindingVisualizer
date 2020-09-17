package main.java.backend.models;

public class Cell {
    private int x;
    private int y;
    private int value;

    public Cell(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public Cell(int value) {
        this.value = value;
        this.x = 0;
        this.y = 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
