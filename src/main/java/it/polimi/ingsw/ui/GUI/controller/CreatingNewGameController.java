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

public class CreatingNewGameController extends GUI{
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


    public void setPlayers(ActionEvent actionEvent) {
        if(button2.isSelected()) {
        } else if(button3.isSelected()) {
        } else if(button4.isSelected()) {
        }
    }

    public void login(ActionEvent actionEvent) throws IOException {

        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        /*try {
            Parent root = FXMLLoader.load(getClass().getResource("/gc38/scenes/WaitingForPlayers.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */
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
}
