package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class WaitingForPlayersController extends GUI implements Initializable {

    @FXML
    private ImageView icon_loading;
    private Stage stage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        RotateTransition translate = new RotateTransition();
        translate.setNode(icon_loading);
        translate.setDuration(javafx.util.Duration.seconds(2));
        translate.setCycleCount(TranslateTransition.INDEFINITE);
        translate.setInterpolator(javafx.animation.Interpolator.LINEAR);
        translate.setByAngle(360);
        translate.play();
    }

    /* TODO nel momento in cui il server invia il messaggio di inizio partita:
        - l'immagine dall'icona di caricamento verrà modificata in un segnale di validità (tick verde)
        - l'animazione viene interrotta
        - il client dovrà schiacciare il bottone start game (magari lo faccio apparire da codice solo in quel momento)
        nb: ad ora il bottone start game è visibile perchè mi serve per proseguire tra le scene
     */

    public void startGame(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        //this.changeScene("/it/polimi/ingsw/scenes/BoardScene.fxml", stage);
        this.changeScene("/it/polimi/ingsw/scenes/ChoosingObjective.fxml", stage);
    }


}
