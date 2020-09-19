package main.java.backend.services;

import main.java.backend.models.Cell;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class AStar {
    public static final int DIAGONAL_COST = 14;
    public static final int V_H_COST = 10;

    List<List<Cell>> grid = new ArrayList<>();
    PriorityQueue<Cell> open;
    boolean closed[][];
    Cell source;
    Cell destination;

    List<List<Cell>> openStates = new ArrayList<>();
    List<Cell> closedStates = new ArrayList<>();

    public AStar(Cell source, Cell destination, List<List<Cell>> grid){
        this.source = source;
        this.destination = destination;
        this.grid = grid;
        closed = new boolean[grid.size()][grid.size()];
        open = new PriorityQueue<>((Object o1, Object o2) -> {
            Cell c1 = (Cell)o1;
            Cell c2 = (Cell)o2;

            return c1.getFinalCost()<c2.getFinalCost()?-1:
                    c1.getFinalCost()>c2.getFinalCost()?1:0;
        });

        for(int i=0;i<grid.size();++i){
            for(int j=0;j<grid.size();++j){
                grid.get(i).get(j).setHeuristicCost(Math.abs(i-destination.getI())+Math.abs(j-destination.getJ()));
                //if(grid.get(i).get(j).getValue()==-1) setBlocked(i,j);
            }
        }
        grid.get(source.getI()).get(source.getJ()).setFinalCost(0);
    }
    public void setBlocked(int i, int j){
        grid.get(i).set(j,null);
    }

    public void setStartCell(Cell source){
        this.source = source;
    }

    public void setEndCell(Cell destination){
        this.destination = destination;
    }

    public List<List<Cell>> getOpenStates() {
        return openStates;
    }

    public List<Cell> getClosedStates() {
        return closedStates;
    }

    public void checkAndUpdateCost(Cell current, Cell t, int cost){
        if(t.getValue() == -1 || closed[t.getI()][t.getJ()])return;
        //if(t == null || closed[t.getI()][t.getJ()])return;

        int t_final_cost = t.getHeuristicCost()+cost;

        boolean inOpen = open.contains(t);
        if(!inOpen || t_final_cost<t.getFinalCost()){
            t.setFinalCost(t_final_cost);
            t.setParent(current);
            if(!inOpen)open.add(t);
        }
    }

    public List<Cell> runAStar(){
        //add the start location to open list.
        open.add(source);
        List<Cell> path ;
        Cell current;
        while(true){
            List<Cell> currentOpen = new ArrayList<>();
            for(Cell cell : open) currentOpen.add(cell);
            openStates.add(currentOpen);

            current = open.poll();
            if(current.getValue()==-1)break;
            //if(current==null)break;

            closedStates.add(current);
            closed[current.getI()][current.getJ()]=true;

            if(current.equals(destination)){
                if(closed[destination.getI()][destination.getJ()]){
                    path = new ArrayList<>();
                    //Trace back the path
                    //System.out.println("Path: ");
                    current = grid.get(destination.getI()).get(destination.getJ());
                    path.add(current);
                    //System.out.print(current.toString());
                    while(current.getParent()!=null){
                        path.add(current.getParent());
                        //System.out.print(" -> "+current.getParent().toString());
                        current = current.getParent();
                    }
                    //System.out.println();
                    return path;
                }else {
                    return null;
                    //System.out.println("No possible path");
                }
            }

            Cell t;
            if(current.getI()-1>=0){
                t = grid.get(current.getI()-1).get(current.getJ());
                checkAndUpdateCost(current, t, current.getFinalCost()+V_H_COST);

                if(current.getJ()-1>=0){
                    t = grid.get(current.getI()-1).get(current.getJ()-1);
                    checkAndUpdateCost(current, t, current.getFinalCost()+DIAGONAL_COST);
                }

                if(current.getJ()+1<grid.size()){
                    t = grid.get(current.getI()-1).get(current.getJ()+1);
                    checkAndUpdateCost(current, t, current.getFinalCost()+DIAGONAL_COST);
                }
            }

            if(current.getJ()-1>=0){
                t = grid.get(current.getI()).get(current.getJ()-1);
                checkAndUpdateCost(current, t, current.getFinalCost()+V_H_COST);
            }

            if(current.getJ()+1<grid.size()){
                t = grid.get(current.getI()).get(current.getJ()+1);
                checkAndUpdateCost(current, t, current.getFinalCost()+V_H_COST);
            }

            if(current.getI()+1<grid.size()){
                t = grid.get(current.getI()+1).get(current.getJ());
                checkAndUpdateCost(current, t, current.getFinalCost()+V_H_COST);

                if(current.getJ()-1>=0){
                    t = grid.get(current.getI()+1).get(current.getJ()-1);
                    checkAndUpdateCost(current, t, current.getFinalCost()+DIAGONAL_COST);
                }

                if(current.getJ()+1<grid.size()){
                    t = grid.get(current.getI()+1).get(current.getJ()+1);
                    checkAndUpdateCost(current, t, current.getFinalCost()+DIAGONAL_COST);
                }
            }
        }

        return null;
    }

}
