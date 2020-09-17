package main.java.frontend.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private ImageView btnClose;

    @FXML
    private TextField gridSize;

    @FXML
    private Button btnSetGrid;


    public void handleClose(MouseEvent event) {
        if(event.getSource()==btnClose) System.exit(0);
    }

    public void handleClicks(MouseEvent event) {
        if(event.getSource()==btnSetGrid){
            System.out.println(gridSize.getText());
        }
    }

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
}
