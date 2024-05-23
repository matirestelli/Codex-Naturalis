package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
import it.polimi.ingsw.ui.ObserverUI;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import it.polimi.ingsw.core.model.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ChoosingStarterController extends GUI {
    @FXML
    private Button buttonFrontSide, buttonBackSide;
    @FXML
    private ImageView frontSide, backSide;
    private CardGame starterCard;
    private Stage stage;

    private static boolean settedSide = false;

    public void setStarterCard(CardGame starterCard){
        String imageFront = starterCard.getFrontCover();
        String imageBack = starterCard.getBackCover();
        Image front = new Image(imageFront);
        Image back = new Image(imageBack);
        frontSide.setImage(front);
        backSide.setImage(back);
    }

    public void chooseStarterSide(){
        //quando clicco il bottone mando update al client della scelta adottata
        buttonFrontSide.setOnAction(e -> {
            if(!settedSide){
            buttonFrontSide.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
            //TODO controlla che true significa front side
          this.observerClient.updateUI(new GameEvent("starterSide", true));
          //metodo della gui che piazza la starter card nella board chiamando il boardViewController
            //starterCard.setSide(true);
            //TODO this.placeCard(starterCard, [39, 39]);
            settedSide = true;
            }
            else {
                this.showErrorPopUp("You have already chosen the side of the card", (Stage) buttonFrontSide.getScene().getWindow());
            }
        });



        buttonBackSide.setOnAction(e -> {
            if(!settedSide){
            buttonBackSide.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
            //TODO controlla che false significa back side
            this.observerClient.updateUI(new GameEvent("starterSide", false));
            //starterCard.setSide(false);
            //TODO this.placeCard(starterCard, [39, 39]); -> penso lo faccio solo nel client con il view model
            settedSide = true;
            }
            else {
                this.showErrorPopUp("You have already chosen the side of the card", (Stage) buttonBackSide.getScene().getWindow());
            }
        });
    }

    public void nextscene(ActionEvent actionEvent) throws IOException {
        if(settedSide) {
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            this.changeScene("/it/polimi/ingsw/scenes/BoardScene.fxml", stage);
        }
        else {
            this.showErrorPopUp("You have to choose the side of the card", (Stage) buttonBackSide.getScene().getWindow());
        }
    }
}
