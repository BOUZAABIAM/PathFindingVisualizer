package main.java.backend.models;

public class Cell {
    private double x;
    private double y;
    // Value : 0 --> Open / 1 --> Source / 2 --> Destination / -1 --> Blocked
    private int value;

    public Cell(double x, double y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }

    public Cell(int value) {
        this.value = value;
        this.x = 0;
        this.y = 0;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
