package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.core.model.ViewModelPlayerState;
import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.ui.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;
import java.util.Map;
import it.polimi.ingsw.ui.GUI.GUI;

public class OpponentsCodexController extends GUI{
    private Stage stage;
    private double x,y;

    private String player;
    private ViewModelPlayerState playerState;
    private List<Card> codex;

    @FXML
    private GridPane gridPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label usernameLabel;
    private String cardFront;

    /**
     * Initializes the UI components and displays the player's codex cards on a grid pane.
     * This method sets up the username label, retrieves player state and codex from the model view,
     * and dynamically adds card images to the grid pane based on their coordinates in the codex.
     *
     * @param usernamePlayer The username of the player whose codex is being displayed.
     */
    public void initialize(String usernamePlayer){
        player = usernamePlayer;
        playerState = client.getModelView().getPlayerStates().get(player);
        codex = playerState.getCodex();
        usernameLabel.setText(player + "'s Codex");

        for(Card c : codex){
            System.out.printf("the card played is %s\n", c.getId());
            if(c.isFrontSide()){
                cardFront = c.getFrontCover();
            }
            else{
                cardFront = c.getBackCover();
            }
            ImageView image = new ImageView(new Image(cardFront));
            image.setFitHeight(83);
            image.setFitWidth(120);
            gridPane.setAlignment(Pos.CENTER);
            System.out.printf("the coordinates played is %d %d\n",  c.getxMatrixCord() , c.getyMatrixCord());
            gridPane.add(image, c.getxMatrixCord() , c.getyMatrixCord());
            scrollPane.setHvalue(0.5);
            scrollPane.setVvalue(0.5);
        }
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

    /**
     * Closes the chat pop-up window and performs necessary clean-up actions.
     * closes the
     * JavaFX stage associated with the pop-up window.
     *
     * @param actionEvent The action event that triggers the closure of the pop-up window.
     */
    public void closePopUp(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}
