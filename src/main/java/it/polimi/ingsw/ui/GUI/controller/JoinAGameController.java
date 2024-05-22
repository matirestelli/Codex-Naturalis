package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class JoinAGameController extends GUI{
    @FXML
    private RadioButton button2, button3, button4;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField idtextfield;

    @FXML
    private TextField nametextfield;

    @FXML
    private ImageView iconaScreen;


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

    public void setPlayers(ActionEvent actionEvent) {
        if(button2.isSelected()) {
            //TODO
        } else if(button3.isSelected()) {
            //TODO
        } else if(button4.isSelected()) {
            //TODO
        }
    }

    public void login(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
       /* try {
            Parent root = FXMLLoader.load(getClass().getResource("/it/polimi/ingsw/gc38/scenes/LobbyGames.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        */
        this.changeScene("/it/polimi/ingsw/scenes/LobbyGames.fxml", stage);
    }
}
