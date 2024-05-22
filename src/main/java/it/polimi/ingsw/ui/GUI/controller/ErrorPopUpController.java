package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorPopUpController extends GUI{
    private Stage stage;
    private Scene scene;
    @FXML
    private Text messageText;
    private double x,y;

    @FXML
    private Button closePopUpButton;

    private Font myFont = Font.font("Poor Richard", 20);



    public void closePopUp(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }
    @FXML void dragged(MouseEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }

    //il messaggio viene personalizzato nel momento della chiamata
    public void showMessage(String errMessage){

        messageText.setText(errMessage);
        messageText.setFont(myFont);
    }


}
