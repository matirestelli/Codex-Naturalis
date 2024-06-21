package it.polimi.ingsw.ui.GUI.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SearchingPlayersController {

    //todo guardare se no la elimino
    private Stage stage;
    private Scene scene;
    private Parent root;

    private BoardViewController boardViewController;
    @FXML
    Label nameLabel;

    public void displayUsername(String user) {
        nameLabel.setText("Welcome " + user);
    }

/*
    public void startPreGame(ActionEvent actionEvent) {
        //per ora va a questa scena della board solo perchè mi serve vedere come si vede, poi scena in mezzo del pregame dove ad ogni giocatore il suo colore ecc
        //oppure non scena ma viene settato tutto qui
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/gc38/scenes/BoardScene.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/

    public void startPreGame(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("scenes/BoardScene.fxml"));
            root = loader.load();
            boardViewController = loader.getController();
            //boardViewController.initialize();

            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
