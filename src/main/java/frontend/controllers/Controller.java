package main.java.frontend.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import main.java.backend.models.Cell;
import main.java.backend.models.Grid;
import main.java.backend.services.AStar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ImageView btnClose;

    @FXML
    private TextField gridSize;

    @FXML
    private Button btnSetGrid;

    @FXML
    private Canvas canvas;

    @FXML
    private Button btnAddSrcDst;

    @FXML
    private Button btnAddBlocks;

    @FXML
    private Button btnRun;

    @FXML
    private ImageView eraser;

    private int src_dist_clicks =0;
    private Grid grid;
    private double cellWidth;
    private Cell source;
    private Cell destination;
    private int btnSrcDstClicks = 0;
    private int btnAddBlocksClicks = 0;
    private double pressedX;
    private double pressedY;
    private int eraserClicks =0;
    List<Cell> path = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gridSize.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    gridSize.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public void handleClose(MouseEvent event) {
        if(event.getSource()==btnClose) System.exit(0);
    }

    public void handleClicks(MouseEvent event) {
        if(event.getSource()==btnSetGrid){
            try {
                int size = Integer.parseInt(gridSize.getText());
                Grid grid = new Grid(size,canvas.getHeight(),canvas.getWidth());
                this.grid = grid;
                this.cellWidth = canvas.getWidth()/size;
                GraphicsContext gc = this.canvas.getGraphicsContext2D();
                draw(gc,grid);
                btnAddSrcDst.setDisable(false);
                //source =null;
                //destination = null;
            }catch (Exception e){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Warning");
                a.setContentText("Please set the size of the board!"+"\n"+"Size should be > 1 !");
                a.setHeaderText(null);
                a.showAndWait();
            }

        }
        if(event.getSource()==btnAddSrcDst){
            if(btnSrcDstClicks%2==0){
                btnAddSrcDst.setText("Save Source/Destination");
                this.canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        double pressedX = event.getX();
                        double pressedY = event.getY();
                        Cell cell = findPressedCell(src_dist_clicks%2+1,pressedX,pressedY);
                        GraphicsContext gc = canvas.getGraphicsContext2D();
                        drawCell(src_dist_clicks,cell,gc);
                        src_dist_clicks ++;

                    }
                });
                btnSrcDstClicks ++;
            }else{
                btnAddSrcDst.setText("Change Source/Destination");
                this.canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                    }
                });
                btnAddBlocks.setDisable(false);
                btnSrcDstClicks++;
            }

        }
        if(event.getSource()==btnAddBlocks){
            if(btnAddBlocksClicks%2== 0){
                btnAddBlocks.setText("Save Blocks");
                this.canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        pressedX = event.getX();
                        pressedY = event.getY();
                        Cell cell = findPressedCell(-1,pressedX,pressedY);
                        GraphicsContext gc = canvas.getGraphicsContext2D();
                        drawBlocks(cell,gc);
                    }
                });
                this.canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        Cell cell = findPressedCell(-1,event.getX(),event.getY());
                        GraphicsContext gc = canvas.getGraphicsContext2D();
                        drawBlocks(cell,gc);
                    }
                });
                btnAddBlocksClicks++;
            }else{
                btnAddBlocks.setText("Change Blocks");
                this.canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    }
                });
                this.canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    }
                });
                btnAddBlocksClicks++;
            }
            btnRun.setDisable(false);
        }
        if(event.getSource()==btnRun){
            this.prepareGrid();
            //grid.tostring();
            this.runAStar();
        }
    }

    private Cell findPressedCell(int value,double pressedX, double pressedY) {
        for(int j=0;j<grid.getGrid().size();j++){
            List<Cell> line = grid.getGrid().get(j);
            Cell previous = line.get(0);
            for(int i=1;i<line.size();i++){
                Cell current = line.get(i);
                if(current.getX()-cellWidth<=pressedX && current.getY()-cellWidth<=pressedY && previous.getX()+cellWidth>=pressedX&&previous.getY()+cellWidth>=pressedY){
                    if(previous != null && previous!=source && previous != destination){
                        grid.getGrid().get(j).get(i-1).setValue(value);
                    }
                    return previous;
                }
                else if(current.getX()<=pressedX && current.getY()<=pressedY && current.getX()+cellWidth>=pressedX && current.getY()+cellWidth>=pressedY) {
                    if(current != null && current!=source && current != destination){
                        grid.getGrid().get(j).get(i).setValue(value);
                    }
                    return current;
                }
                else previous = current;
            }
        }
        return null;
    }


    public void draw(GraphicsContext gc,Grid grid){
        double cellWidth = grid.getGrid().get(0).get(1).getX();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(1);
        double y = 0;
        for(int i=0;i<=grid.getGrid().size();i++){
            gc.strokeLine(0, y, canvas.getWidth(), y);
            y += cellWidth;
        }

        double x = 0;
        for(int i=0;i<=grid.getGrid().size();i++){
            gc.strokeLine(x, 0, x, canvas.getHeight());
            x += cellWidth;
        }
    }

    public void drawCell(int value,Cell cell,GraphicsContext gc){
        if(value%2==0){
            if(value == 0){
                gc.setFill(Color.GREEN);
                gc.fillRect(cell.getX()+2,cell.getY()+2,cellWidth-4,cellWidth-4);
                gc.drawImage(new Image(getClass().getResource("icons/start.png").toString()),cell.getX()+cellWidth/4,cell.getY()+cellWidth/4,cellWidth/2,cellWidth/2);
                source = cell;
            }else{
                gc.setFill(Color.WHITE);
                gc.fillRect(source.getX()+2,source.getY()+2,cellWidth-4,cellWidth-4);
                gc.fillRect(destination.getX()+2,destination.getY()+2,cellWidth-4,cellWidth-4);
                gc.setFill(Color.GREEN);
                gc.fillRect(cell.getX()+2,cell.getY()+2,cellWidth-4,cellWidth-4);
                gc.drawImage(new Image(getClass().getResource("icons/start.png").toString()),cell.getX()+cellWidth/4,cell.getY()+cellWidth/4,cellWidth/2,cellWidth/2);
                source = cell;
            }

        }else{
            gc.setFill(Color.RED);
            gc.fillRect(cell.getX()+2,cell.getY()+2,cellWidth-4,cellWidth-4);
            gc.drawImage(new Image(getClass().getResource("icons/finish-flag.png").toString()),cell.getX()+cellWidth/4,cell.getY()+cellWidth/4,cellWidth/2,cellWidth/2);
            destination = cell;
        }

    }

    private void drawBlocks(Cell cell, GraphicsContext gc) {
        if(cell != null && cell!=source && cell != destination){
            gc.setFill(Color.BLACK);
            gc.fillRect(cell.getX()+2,cell.getY()+2,cellWidth-4,cellWidth-4);
        }
    }

    private void drawCellPath(Cell cell, GraphicsContext gc) {
        if(cell != null && cell!=source && cell != destination){
            gc.setFill(Color.BLUE);
            gc.fillRect(cell.getX()+2,cell.getY()+2,cellWidth-4,cellWidth-4);
        }
    }

    private void eraseCell(Cell cell, GraphicsContext gc) {
        if(cell != null && cell!=source && cell != destination){
            gc.setFill(Color.WHITE);
            gc.fillRect(cell.getX()+2,cell.getY()+2,cellWidth-4,cellWidth-4);
        }
    }

    // delete changed src/dest
    private void prepareGrid() {
        for(int i=0;i<grid.getGrid().size();i++){
            for(int j=0;j<grid.getGrid().size();j++){
                Cell cell = grid.getGrid().get(i).get(j);
                if(cell.getValue()==1 && (cell.getX()!=source.getX() || cell.getY()!=source.getY())) {
                    grid.getGrid().get(i).get(j).setValue(0);
                }
                if(cell.getValue()==2 && (cell.getX()!=destination.getX() || cell.getY()!=destination.getY())) {
                    grid.getGrid().get(i).get(j).setValue(0);
                }
            }
        }
    }


    public void erase(MouseEvent event) {
        if(event.getSource()==eraser){
            if(eraserClicks%2==0){
                this.canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        pressedX = event.getX();
                        pressedY = event.getY();
                        Cell cell = findPressedCell(0,pressedX,pressedY);
                        GraphicsContext gc = canvas.getGraphicsContext2D();
                        eraseCell(cell,gc);
                    }
                });
                this.canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                        Cell cell = findPressedCell(0,event.getX(),event.getY());
                        GraphicsContext gc = canvas.getGraphicsContext2D();
                        eraseCell(cell,gc);
                    }
                });
                eraserClicks++;
            }else{
                this.canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    }
                });
                this.canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    public void handle(MouseEvent event) {
                    }
                });
                eraserClicks++;
            }
        }
    }

    public void runAStar(){
        AStar aStar = new AStar(source,destination,grid.getGrid());
        path = aStar.runAStar();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        for(Cell cell:path) drawCellPath(cell,gc);
    }
}
