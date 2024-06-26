package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import it.polimi.ingsw.core.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitingForPlayersController extends GUI  {

    @FXML
    private ImageView icon_pawn;
    @FXML
    private Label welcomeLabel;
    private Stage stage;



    /**
     * Initializes the welcome message and sets the player's pawn icon based on their color.
     * This method updates the welcome label with the player's username and sets the player's
     * pawn icon image accordingly.
     */
    public void initializeMessage() {
       welcomeLabel.setText("Welcome " + client.getModelView().getMyUsername());
        String color = client.getModelView().getPlayerPawns().get(client.getModelView().getMyUsername()).toString();
        icon_pawn.setImage(new Image("images/pawn/" + color + ".png"));
    }


    public void startGame(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        //this.changeScene("/scenes/BoardScene.fxml", stage);
        this.changeScene("scenes/ChoosingObjective.fxml", stage);
    }


}
