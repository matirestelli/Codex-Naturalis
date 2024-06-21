package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingViewController extends GUI{

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private ImageView iconaScreen;
    @FXML
    private RadioButton rButtonCli, rButtonGui, rButtonRmi, rButtonSocket;


    private String interfaceUser;
    private String network;

    private BoardViewController boardViewController = new BoardViewController();


    public void setInterface(ActionEvent actionEvent) {
        if(rButtonCli.isSelected()) {
            interfaceUser = "CLI";
        } else if(rButtonGui.isSelected()) {
            interfaceUser = "GUI";
        }
    }

    public void setNetwork(ActionEvent actionEvent) {
        if(rButtonRmi.isSelected()) {
            network = "RMI";
        } else if(rButtonSocket.isSelected()) {
            network = "Socket";
        }
    }
    @FXML
    public void startGame(ActionEvent actionEvent) throws IOException {
        if(interfaceUser == null && network == null) {
            //error message that you have to choose a network and an interface
            Stage errorStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            this.showErrorPopUp("You have to choose a network and an interface", errorStage);

//            var alert = new Alert(Alert.AlertType.ERROR);
//            alert.setTitle("Error");
//            alert.setHeaderText("error");
//            alert.setContentText("You have to choose a network and an interface");
//            alert.showAndWait();


        }
        else{
            if (network == "RMI") {
                //TODO
            } else if (network == "Socket") {
                //TODO
            }
            else{
                //error message that you have to choose a network
                Stage errorStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                this.showErrorPopUp("You have to choose a network", errorStage);

            }
            if (interfaceUser == "CLI") {
                //si apre la cli e l'utente può giocare da lì
                javafx.application.Platform.exit();
            } else if (interfaceUser == "GUI") {
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                this.changeScene("scenes/SettingUsername.fxml", stage);
            }
            else{
                //error message that you have to choose a network
                Stage errorStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                this.showErrorPopUp("You have to choose an interface", errorStage);

            }
        }


    }

    public void setFullScreen(ActionEvent actionEvent) {
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            if (stage.isFullScreen()) {
                stage.setFullScreen(false);
                iconaScreen.setImage(new javafx.scene.image.Image("icons/iconaFullScreen.png"));
            } else {
                stage.setFullScreen(true);
                iconaScreen.setImage(new javafx.scene.image.Image("icons/iconaMinimizeScreen.png"));
            }
        }



}
