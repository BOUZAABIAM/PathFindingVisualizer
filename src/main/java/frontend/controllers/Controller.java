package main.java.frontend.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Controller {

    @FXML
    private ImageView btnClose;

    public void handleClose(MouseEvent event) {
        if(event.getSource()==btnClose) System.exit(0);
    }
}
