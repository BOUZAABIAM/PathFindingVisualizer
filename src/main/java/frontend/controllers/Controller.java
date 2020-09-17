package main.java.frontend.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import main.java.backend.models.Cell;
import main.java.backend.models.Grid;

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
            System.out.println(gridSize.getText());
            int size = Integer.parseInt(gridSize.getText());
            Grid grid = new Grid(size,canvas.getHeight(),canvas.getWidth());
            GraphicsContext gc = this.canvas.getGraphicsContext2D();
            draw(gc,grid);
        }
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


}
