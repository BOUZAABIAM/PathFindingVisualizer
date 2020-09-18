package main.java.backend.models;

import java.security.PublicKey;

public class Cell {
    private double x;
    private double y;
    // Value : 0 --> Open / 1 --> Source / 2 --> Destination / -1 --> Blocked
    private int value;
    private int i;
    private int j;
    private int heuristicCost = 0; //Heuristic cost
    private int finalCost = 0; //G+H
    private Cell parent;

    public Cell(double x, double y,int i,int j, int value) {
        this.x = x;
        this.y = y;
        this.i = i;
        this.j = j;
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

    public int getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    public int getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(int finalCost) {
        this.finalCost = finalCost;
    }

    public Cell getParent() {
        return parent;
    }

    public void setParent(Cell parent) {
        this.parent = parent;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + i +
                ", y=" + j +
                ", value=" + value +
                '}';
    }
}
