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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import main.java.backend.models.Cell;
import main.java.backend.models.Grid;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
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

    private int src_dist_clicks =0;
    private Grid grid;
    private double cellWidth;
    private Cell source;
    private Cell destination;

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
            }catch (Exception e){
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setTitle("Warning");
                a.setContentText("Please set the size of the board!"+"\n"+"Size should be > 1 !");
                a.setHeaderText(null);
                a.showAndWait();
            }

        }
        if(event.getSource()==btnAddSrcDst){
            btnAddSrcDst.setText("Save Source/Destination");
            this.canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    double pressedX = event.getX();
                    double pressedY = event.getY();
                    //System.out.println(pressedX);
                    //System.out.println(pressedY);
                    Cell cell = findPressedCell(pressedX,pressedY,grid);
                    GraphicsContext gc = canvas.getGraphicsContext2D();
                    drawCell(src_dist_clicks,cell,gc);
                    src_dist_clicks ++;

                }
            });
        }
    }

    private Cell findPressedCell(double pressedX, double pressedY, Grid grid) {
        for(List<Cell> line:grid.getGrid()){
            Cell previous = line.get(0);
            for(int i=1;i<line.size();i++){
                Cell current = line.get(i);
                if(current.getX()-cellWidth<=pressedX && current.getY()-cellWidth<=pressedY && previous.getX()+cellWidth>=pressedX&&previous.getY()+cellWidth>=pressedY) return previous;
                else if(current.getX()<=pressedX && current.getY()<=pressedY && current.getX()+cellWidth>=pressedX && current.getY()+cellWidth>=pressedY) return current;
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
                gc.fillRect(cell.getX()+1,cell.getY()+1,cellWidth-1,cellWidth-1);
                gc.drawImage(new Image(getClass().getResource("icons/start.png").toString()),cell.getX()+cellWidth/4,cell.getY()+cellWidth/4,cellWidth/2,cellWidth/2);
                source = cell;
            }else{
                gc.setFill(Color.WHITE);
                gc.fillRect(source.getX()+1,source.getY()+1,cellWidth-1,cellWidth-1);
                gc.fillRect(destination.getX()+1,destination.getY()+1,cellWidth-1,cellWidth-1);
                gc.setFill(Color.GREEN);
                gc.fillRect(cell.getX()+1,cell.getY()+1,cellWidth-1,cellWidth-1);
                gc.drawImage(new Image(getClass().getResource("icons/start.png").toString()),cell.getX()+cellWidth/4,cell.getY()+cellWidth/4,cellWidth/2,cellWidth/2);
                source = cell;
            }

        }else{
            gc.setFill(Color.RED);
            gc.fillRect(cell.getX()+1,cell.getY()+1,cellWidth-1,cellWidth-1);
            gc.drawImage(new Image(getClass().getResource("icons/finish-flag.png").toString()),cell.getX()+cellWidth/4,cell.getY()+cellWidth/4,cellWidth/2,cellWidth/2);
            destination = cell;
        }

    }


}
