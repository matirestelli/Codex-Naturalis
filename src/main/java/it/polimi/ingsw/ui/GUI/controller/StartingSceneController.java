package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class StartingSceneController extends GUI {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ImageView iconaScreen;


    public void startGame(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
         this.changeScene("/it/polimi/ingsw/scenes/SettingView.fxml", stage);
    }

    public void setFullScreen(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if(stage.isFullScreen()){
            stage.setFullScreen(false);
            iconaScreen.setImage(new javafx.scene.image.Image("/it/polimi/ingsw/gc38/icons/iconaFullScreen.png"));
        } else {
            stage.setFullScreen(true);
            iconaScreen.setImage(new javafx.scene.image.Image("/it/polimi/ingsw/gc38/icons/iconaMinimizeScreen.png"));
        }

    }



}
