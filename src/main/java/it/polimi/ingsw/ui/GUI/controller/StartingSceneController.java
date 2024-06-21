package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class StartingSceneController extends GUI {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private ImageView iconaScreen;

    @FXML
    private Button buttonPlay;
    @FXML
    private HBox hboxPlay;


    public void startGame(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
         this.changeScene("scenes/WaitingForPlayers.fxml", stage);
    }

    public void setFullScreen(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if(stage.isFullScreen()){
            stage.setFullScreen(false);
            iconaScreen.setImage(new javafx.scene.image.Image("icons/iconaFullScreen.png"));
        } else {
            stage.setFullScreen(true);
            iconaScreen.setImage(new javafx.scene.image.Image("icons/iconaMinimizeScreen.png"));
        }

    }

    public void ifGameNotStarted() {
        if(!client.getModelView().getGameStarted()){
            buttonPlay.setDisable(true);
            hboxPlay.getChildren().remove(buttonPlay);
            Label label = new Label("Waiting for other players to join the game...");
            ImageView icon_loading = new ImageView();
            icon_loading.setImage(new Image("icons/icons8-loading-80.png"));
            icon_loading.setFitHeight(30);
            icon_loading.setFitWidth(30);
            RotateTransition translate = new RotateTransition();
            translate.setNode(icon_loading);
            translate.setDuration(javafx.util.Duration.seconds(2));
            translate.setCycleCount(TranslateTransition.INDEFINITE);
            translate.setInterpolator(javafx.animation.Interpolator.LINEAR);
            translate.setByAngle(360);
            translate.play();
            hboxPlay.getChildren().addAll(label, icon_loading);

            wasWaitingForPlayers = true;
        }
    }



}
