package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingUsernameController extends GUI {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TextField nametextfield;

    @FXML
    private ImageView iconaScreen;

    @FXML
    private TextField idGame;

    @FXML
    private VBox setParam;

    @FXML
    private AnchorPane anchorPane;


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



    public void CreateGame(ActionEvent actionEvent) throws IOException {
       // String user = nametextfield.getText();
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
       /* try {
            Parent root = FXMLLoader.load(getClass().getResource("/gc38/scenes/CreatingNewGame.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        */
        this.changeScene("scenes/CreatingNewGame.fxml", stage);
    }


    public void JoinGame(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        /* try {
            Parent root = FXMLLoader.load(getClass().getResource("/gc38/scenes/JoinAGame.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

         */
        this.changeScene("scenes/JoinAGame.fxml", stage);
    }
}
