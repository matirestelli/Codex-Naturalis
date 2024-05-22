package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.core.model.CardGame;
import it.polimi.ingsw.core.model.GameEvent;
import it.polimi.ingsw.core.model.Objective;
import it.polimi.ingsw.ui.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ChoosingObjectiveController extends GUI {

    private Stage stage;
    @FXML
    private Button buttonObjective1, buttonObjective2 ;
    //id della carta obiettivo selezionata
    private int objectiveSelected = 0;
    @FXML
    private ImageView objective1, objective2;
    private Objective obj1, obj2;

    public void setObjective(List<Objective> objectives){
        String imageObj1 = objectives.getFirst().getFrontCover();
        String imageObj2 = objectives.getLast().getFrontCover();
        Image front = new Image(imageObj1);
        Image back = new Image(imageObj2);
        objective1.setImage(front);
        objective2.setImage(back);
    }

    public void chooseObjective(){
        //quando clicco il bottone mando update al client della scelta adottata
        buttonObjective1.setOnAction(e -> {
            buttonObjective1.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
            //TODO controlla che true significa front side
            this.observerClient.updateUI(new GameEvent("choseObjective", obj1));
            //metodo della gui che piazza la carta obiettivo segreto nella board chiamando il boardViewController
            //o forse non da fare perchè lo metto nel view model e lo prende lui poi quando crea la board
            //TODO
            //cambio scena passando alla boardView
        });
        buttonObjective2.setOnAction(e -> {
            buttonObjective2.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
            //TODO controlla che false significa back side
            this.observerClient.updateUI(new GameEvent("choseObjective", obj2));
            //metodo della gui che piazza la carta obiettivo segreto nella board chiamando il boardViewController
            //o forse non da fare perchè lo metto nel view model e lo prende lui poi quando crea la board
            //TODO
            //cambio scena passando alla boardView
        });
    }


}
