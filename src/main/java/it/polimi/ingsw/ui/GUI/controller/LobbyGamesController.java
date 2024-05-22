package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LobbyGamesController extends GUI{

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;


    public void importListGames() {
        //TODO
        /* prenderà la lista delle partite disponibili (che il server ha mandato come update nell'evento) e le mostrerà
        devo creare una HBox per ogni partita con un bottone join e un label con l'id della partita
        una possibilità è non avere il bottone sotto join game ma aver direttamente un bottone join su ogni HBox
        quando verrà cliccato il bottone join verrà chiamata la funzione joinGame e mandato al server l'id della partita scelta
        */
        //questo metodo verrà chiamato quando si passa a questa scella dalla scena di JoinAGame, viene chiamato prima di cambiare stage
    }
    public void joinGame(ActionEvent actionEvent) {
        //TODO
        /* manderà un update al server con l'id della partita a cui vuole partecipare
        e succcessivamente verrà reindirizzato alla scena di attesa
        */
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        /*try {
            Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/gc38/scenes/WaitingForPlayers.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */
        this.changeScene("/it/polimi/ingsw/scenes/WaitingForPlayers.fxml", stage);
    }
}
