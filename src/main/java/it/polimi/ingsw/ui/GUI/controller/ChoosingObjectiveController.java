package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.core.model.CardGame;
import it.polimi.ingsw.core.model.Objective;
import it.polimi.ingsw.core.model.message.response.SelectedObjMessage;
import it.polimi.ingsw.ui.GUI.GUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
    public static boolean settedObjective = false;

    public void initialize(){
        settedObjective = false;
    }

    public void setObjective(List<Objective> objectives){
        String imageObj1 = objectives.getFirst().getFrontCover();
        obj1 = objectives.getFirst();
        String imageObj2 = objectives.getLast().getFrontCover();
        obj2 = objectives.getLast();
        Image frontObj1 = new Image(imageObj1);
        Image frontObj2 = new Image(imageObj2);
        objective1.setImage(frontObj1);
        objective2.setImage(frontObj2);
    }

    public void chooseObjective(){
        //quando clicco il bottone mando update al client della scelta adottata
        buttonObjective1.setOnAction(e -> {
            if(!settedObjective){
                buttonObjective1.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                viewModel.setSecretObj(obj1);
                client.sendMessage(new SelectedObjMessage("chooseSecretObjective", obj1));

                settedObjective = true;
            }
            else {
                this.showErrorPopUp("You have already chosen the objective card", (Stage) buttonObjective1.getScene().getWindow());
            }

        });


        buttonObjective2.setOnAction(e -> {
            if(!settedObjective){
                buttonObjective2.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                viewModel.setSecretObj(obj2);
                client.sendMessage(new SelectedObjMessage("chooseSecretObjective", obj2));

                settedObjective = true;
            }
            else {
                this.showErrorPopUp("You have already chosen the objective card", (Stage) buttonObjective2.getScene().getWindow());
            }

        });
    }

    public void nextscene(ActionEvent actionEvent) throws IOException {
        if(settedObjective) {
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            this.changeScene("scenes/ChoosingStarter.fxml", stage);
        }
        else {
            this.showErrorPopUp("You have to choose one objective", (Stage) buttonObjective2.getScene().getWindow());
        }
    }


}
