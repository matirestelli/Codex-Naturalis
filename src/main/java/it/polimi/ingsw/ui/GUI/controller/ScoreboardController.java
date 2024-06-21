package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.enums.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.ui.GUI.GUI ;
public class ScoreboardController extends GUI{
    @FXML
    private ImageView pawn;
    private Map<String, ViewModelPlayerState> playersStates = new HashMap<String, ViewModelPlayerState>();
    private Map<String, Integer> players = new HashMap<String, Integer>();
    private Map<String, Color> playerPawns;
    private Stage stage;
    private Scene scene;
    private double x,y;

    @FXML
    private Button closePopUpButton;
    
    private Map<String, ImageView> pawns = new HashMap<String, ImageView>();

    //array che mi dice quante pedine ci sono già in una posizione -> per nel caso IMPILARLE con le coordinate giuste
    private int[] pawnsInPosition = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0} ;

    @FXML
    private AnchorPane anchorPane;


    public void closePopUp(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }
    @FXML
    void pressed(MouseEvent event) {
        x = event.getSceneX();
        y = event.getSceneY();
    }
    @FXML void dragged(MouseEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setX(event.getScreenX() - x);
        stage.setY(event.getScreenY() - y);
    }


    public void setPawns(){
        playersStates = client.getModelView().getPlayerStates();
        playerPawns = client.getModelView().getPlayerPawns();
        
        for(String username : playerPawns.keySet()){
            switch(playerPawns.get(username)){
                case BLUE:
                    pawn = new ImageView("images/pawn/blue.png");
                    break;
                case GREEN:
                    pawn = new ImageView("images/pawn/green.png");
                    break;
                case YELLOW:
                    pawn = new ImageView("images/pawn/yellow.png");
                    break;
                case RED:
                    pawn = new ImageView("images/pawn/red.png");
                    break;
                default:
                    break;
            }
            pawn.setFitHeight(32);
            pawn.setFitWidth(102);
            pawn.setPreserveRatio(true);
            pawn.setSmooth(true);
            pawns.put(username, pawn);
            players.put(username, playersStates.get(username).getScore());
        }

        players.put(client.getModelView().getMyUsername(), client.getModelView().getMyScore());


        for(String username : players.keySet()){
            switch(players.get(username)){
                case 0:
                    if(pawnsInPosition[0] == 0){
                        pawns.get(username).setLayoutX(35);
                        pawns.get(username).setLayoutY(307);
                        pawnsInPosition[0]++;
                    }
                    else if(pawnsInPosition[0] == 1){
                        pawns.get(username).setLayoutX(37);
                        pawns.get(username).setLayoutY(304);
                        pawnsInPosition[0]++;
                    }
                    else if(pawnsInPosition[0] == 2){
                        pawns.get(username).setLayoutX(39);
                        pawns.get(username).setLayoutY(301);
                        pawnsInPosition[0]++;
                    }
                    else if(pawnsInPosition[0] == 3){
                        pawns.get(username).setLayoutX(41);
                        pawns.get(username).setLayoutY(298);
                        pawnsInPosition[0]++;
                    }
                    break;

                case 1:
                     if(pawnsInPosition[1] == 0){
                        pawns.get(username).setLayoutX(75);
                        pawns.get(username).setLayoutY(307);
                        pawnsInPosition[1]++;
                    }
                    else if(pawnsInPosition[1] == 1){
                        pawns.get(username).setLayoutX(77);
                        pawns.get(username).setLayoutY(304);
                        pawnsInPosition[1]++;
                    }
                    else if(pawnsInPosition[1] == 2){
                        pawns.get(username).setLayoutX(79);
                        pawns.get(username).setLayoutY(301);
                        pawnsInPosition[1]++;
                    }
                    else if(pawnsInPosition[1] == 3){
                        pawns.get(username).setLayoutX(81);
                        pawns.get(username).setLayoutY(298);
                        pawnsInPosition[1]++;
                    }
                    break;

                case 2:
                     if(pawnsInPosition[2] == 0){
                        pawns.get(username).setLayoutX(115);
                        pawns.get(username).setLayoutY(307);
                        pawnsInPosition[2]++;
                    }
                    else if(pawnsInPosition[2] == 1){
                        pawns.get(username).setLayoutX(117);
                        pawns.get(username).setLayoutY(304);
                        pawnsInPosition[2]++;
                    }
                    else if(pawnsInPosition[2] == 2){
                        pawns.get(username).setLayoutX(119);
                        pawns.get(username).setLayoutY(301);
                        pawnsInPosition[2]++;
                    }
                    else if(pawnsInPosition[2] == 3){
                        pawns.get(username).setLayoutX(121);
                        pawns.get(username).setLayoutY(298);
                        pawnsInPosition[2]++;
                    }
                    break;

                case 3:
                    if(pawnsInPosition[3] == 0){
                        pawns.get(username).setLayoutX(135);
                        pawns.get(username).setLayoutY(270);
                        pawnsInPosition[3]++;
                    }
                    else if(pawnsInPosition[3] == 1){
                        pawns.get(username).setLayoutX(137);
                        pawns.get(username).setLayoutY(267);
                        pawnsInPosition[3]++;
                    }
                    else if(pawnsInPosition[3] == 2){
                        pawns.get(username).setLayoutX(139);
                        pawns.get(username).setLayoutY(264);
                        pawnsInPosition[3]++;
                    }
                    else if(pawnsInPosition[3] == 3){
                        pawns.get(username).setLayoutX(141);
                        pawns.get(username).setLayoutY(261);
                        pawnsInPosition[3]++;
                    }
                    break;

                case 4:
                    if(pawnsInPosition[4] == 0){
                        pawns.get(username).setLayoutX(95);
                        pawns.get(username).setLayoutY(270);
                        pawnsInPosition[4]++;
                    }
                    else if(pawnsInPosition[4] == 1){
                        pawns.get(username).setLayoutX(97);
                        pawns.get(username).setLayoutY(267);
                        pawnsInPosition[4]++;
                    }
                    else if(pawnsInPosition[4] == 2){
                        pawns.get(username).setLayoutX(99);
                        pawns.get(username).setLayoutY(264);
                        pawnsInPosition[4]++;
                    }
                    else if(pawnsInPosition[4] == 3){
                        pawns.get(username).setLayoutX(101);
                        pawns.get(username).setLayoutY(261);
                        pawnsInPosition[4]++;
                    }
                    break;

                case 5:
                    if(pawnsInPosition[5] == 0){
                        pawns.get(username).setLayoutX(55);
                        pawns.get(username).setLayoutY(270);
                        pawnsInPosition[5]++;
                    }
                    else if(pawnsInPosition[5] == 1){
                        pawns.get(username).setLayoutX(57);
                        pawns.get(username).setLayoutY(267);
                        pawnsInPosition[5]++;
                    }
                    else if(pawnsInPosition[5] == 2){
                        pawns.get(username).setLayoutX(59);
                        pawns.get(username).setLayoutY(264);
                        pawnsInPosition[5]++;
                    }
                    else if(pawnsInPosition[5] == 3){
                        pawns.get(username).setLayoutX(61);
                        pawns.get(username).setLayoutY(261);
                        pawnsInPosition[5]++;
                    }
                    break;

                case 6:
                    if(pawnsInPosition[6] == 0){
                        pawns.get(username).setLayoutX(15);
                        pawns.get(username).setLayoutY(270);
                        pawnsInPosition[6]++;
                    }
                    else if(pawnsInPosition[6] == 1){
                        pawns.get(username).setLayoutX(17);
                        pawns.get(username).setLayoutY(267);
                        pawnsInPosition[6]++;
                    }
                    else if(pawnsInPosition[6] == 2){
                        pawns.get(username).setLayoutX(19);
                        pawns.get(username).setLayoutY(264);
                        pawnsInPosition[6]++;
                    }
                    else if(pawnsInPosition[6] == 3){
                        pawns.get(username).setLayoutX(21);
                        pawns.get(username).setLayoutY(261);
                        pawnsInPosition[6]++;
                    }
                    break;

                case 7:
                    if(pawnsInPosition[7] == 0){
                        pawns.get(username).setLayoutX(15);
                        pawns.get(username).setLayoutY(233);
                        pawnsInPosition[7]++;
                    }
                    else if(pawnsInPosition[7] == 1){
                        pawns.get(username).setLayoutX(17);
                        pawns.get(username).setLayoutY(230);
                        pawnsInPosition[7]++;
                    }
                    else if(pawnsInPosition[7] == 2){
                        pawns.get(username).setLayoutX(19);
                        pawns.get(username).setLayoutY(227);
                        pawnsInPosition[7]++;
                    }
                    else if(pawnsInPosition[7] == 3){
                        pawns.get(username).setLayoutX(21);
                        pawns.get(username).setLayoutY(224);
                        pawnsInPosition[7]++;
                    }


                    break;

                case 8:
                    if(pawnsInPosition[8] == 0){
                        pawns.get(username).setLayoutX(55);
                        pawns.get(username).setLayoutY(233);
                        pawnsInPosition[8]++;
                    }
                    else if(pawnsInPosition[8] == 1){
                        pawns.get(username).setLayoutX(57);
                        pawns.get(username).setLayoutY(230);
                        pawnsInPosition[8]++;
                    }
                    else if(pawnsInPosition[8] == 2){
                        pawns.get(username).setLayoutX(59);
                        pawns.get(username).setLayoutY(227);
                        pawnsInPosition[8]++;
                    }
                    else if(pawnsInPosition[8] == 3){
                        pawns.get(username).setLayoutX(61);
                        pawns.get(username).setLayoutY(224);
                        pawnsInPosition[8]++;
                    }
                    break;

                case 9:

                    if(pawnsInPosition[9] == 0){
                        pawns.get(username).setLayoutX(95);
                        pawns.get(username).setLayoutY(233);
                        pawnsInPosition[9]++;
                    }
                    else if(pawnsInPosition[9] == 1){
                        pawns.get(username).setLayoutX(97);
                        pawns.get(username).setLayoutY(230);
                        pawnsInPosition[9]++;
                    }
                    else if(pawnsInPosition[9] == 2){
                        pawns.get(username).setLayoutX(99);
                        pawns.get(username).setLayoutY(227);
                        pawnsInPosition[9]++;
                    }
                    else if(pawnsInPosition[9] == 3){
                        pawns.get(username).setLayoutX(101);
                        pawns.get(username).setLayoutY(224);
                        pawnsInPosition[9]++;
                    }
                    break;

                case 10:

                    if(pawnsInPosition[10] == 0){
                        pawns.get(username).setLayoutX(135);
                        pawns.get(username).setLayoutY(233);
                        pawnsInPosition[10]++;
                    }
                    else if(pawnsInPosition[10] == 1){
                        pawns.get(username).setLayoutX(137);
                        pawns.get(username).setLayoutY(230);
                        pawnsInPosition[10]++;
                    }
                    else if(pawnsInPosition[10] == 2){
                        pawns.get(username).setLayoutX(139);
                        pawns.get(username).setLayoutY(227);
                        pawnsInPosition[10]++;
                    }
                    else if(pawnsInPosition[10] == 3){
                        pawns.get(username).setLayoutX(141);
                        pawns.get(username).setLayoutY(224);
                        pawnsInPosition[10]++;
                    }
                    break;

                case 11:
                    if(pawnsInPosition[11] == 0){
                        pawns.get(username).setLayoutX(135);
                        pawns.get(username).setLayoutY(196);
                        pawnsInPosition[11]++;
                    }
                    else if(pawnsInPosition[11] == 1){
                        pawns.get(username).setLayoutX(137);
                        pawns.get(username).setLayoutY(193);
                        pawnsInPosition[11]++;
                    }
                    else if(pawnsInPosition[11] == 2){
                        pawns.get(username).setLayoutX(139);
                        pawns.get(username).setLayoutY(190);
                        pawnsInPosition[11]++;
                    }
                    else if(pawnsInPosition[11] == 3){
                        pawns.get(username).setLayoutX(141);
                        pawns.get(username).setLayoutY(187);
                        pawnsInPosition[11]++;
                    }

                    break;

                case 12:
                     if(pawnsInPosition[12] == 0){
                        pawns.get(username).setLayoutX(95);
                        pawns.get(username).setLayoutY(196);
                        pawnsInPosition[12]++;
                    }
                    else if(pawnsInPosition[12] == 1){
                        pawns.get(username).setLayoutX(97);
                        pawns.get(username).setLayoutY(193);
                        pawnsInPosition[12]++;
                    }
                    else if(pawnsInPosition[12] == 2){
                        pawns.get(username).setLayoutX(99);
                        pawns.get(username).setLayoutY(190);
                        pawnsInPosition[12]++;
                    }
                    else if(pawnsInPosition[12] == 3){
                        pawns.get(username).setLayoutX(101);
                        pawns.get(username).setLayoutY(187);
                        pawnsInPosition[12]++;
                    }
                    break;

                case 13:
                     if(pawnsInPosition[13] == 0){
                        pawns.get(username).setLayoutX(55);
                        pawns.get(username).setLayoutY(196);
                        pawnsInPosition[13]++;
                    }
                    else if(pawnsInPosition[13] == 1){
                        pawns.get(username).setLayoutX(57);
                        pawns.get(username).setLayoutY(193);
                        pawnsInPosition[13]++;
                    }
                    else if(pawnsInPosition[13] == 2){
                        pawns.get(username).setLayoutX(59);
                        pawns.get(username).setLayoutY(190);
                        pawnsInPosition[13]++;
                    }
                    else if(pawnsInPosition[13] == 3){
                        pawns.get(username).setLayoutX(61);
                        pawns.get(username).setLayoutY(187);
                        pawnsInPosition[13]++;
                    }
                    break;

                case 14:
                     if(pawnsInPosition[14] == 0){
                        pawns.get(username).setLayoutX(15);
                        pawns.get(username).setLayoutY(196);
                        pawnsInPosition[14]++;
                    }
                    else if(pawnsInPosition[14] == 1){
                        pawns.get(username).setLayoutX(17);
                        pawns.get(username).setLayoutY(193);
                        pawnsInPosition[14]++;
                    }
                    else if(pawnsInPosition[14] == 2){
                        pawns.get(username).setLayoutX(19);
                        pawns.get(username).setLayoutY(190);
                        pawnsInPosition[14]++;
                    }
                    else if(pawnsInPosition[14] == 3){
                        pawns.get(username).setLayoutX(21);
                        pawns.get(username).setLayoutY(187);
                        pawnsInPosition[14]++;
                    }
                    break;

                case 15:
                     if(pawnsInPosition[15] == 0){
                        pawns.get(username).setLayoutX(15);
                        pawns.get(username).setLayoutY(159);
                        pawnsInPosition[15]++;
                    }
                    else if(pawnsInPosition[15] == 1){
                        pawns.get(username).setLayoutX(17);
                        pawns.get(username).setLayoutY(156);
                        pawnsInPosition[15]++;
                    }
                    else if(pawnsInPosition[15] == 2){
                        pawns.get(username).setLayoutX(19);
                        pawns.get(username).setLayoutY(153);
                        pawnsInPosition[15]++;
                    }
                    else if(pawnsInPosition[15] == 3){
                        pawns.get(username).setLayoutX(21);
                        pawns.get(username).setLayoutY(150);
                        pawnsInPosition[15]++;
                    }
                    break;

                case 16:
                    if(pawnsInPosition[16] == 0){
                        pawns.get(username).setLayoutX(55);
                        pawns.get(username).setLayoutY(159);
                        pawnsInPosition[16]++;
                    }
                    else if(pawnsInPosition[16] == 1){
                        pawns.get(username).setLayoutX(57);
                        pawns.get(username).setLayoutY(156);
                        pawnsInPosition[15]++;
                    }
                    else if(pawnsInPosition[16] == 2){
                        pawns.get(username).setLayoutX(59);
                        pawns.get(username).setLayoutY(153);
                        pawnsInPosition[16]++;
                    }
                    else if(pawnsInPosition[16] == 3){
                        pawns.get(username).setLayoutX(61);
                        pawns.get(username).setLayoutY(150);
                        pawnsInPosition[16]++;
                    }
                    break;

                case 17:
                    if(pawnsInPosition[17] == 0){
                        pawns.get(username).setLayoutX(95);
                        pawns.get(username).setLayoutY(159);
                        pawnsInPosition[17]++;
                    }
                    else if(pawnsInPosition[17] == 1){
                        pawns.get(username).setLayoutX(97);
                        pawns.get(username).setLayoutY(156);
                        pawnsInPosition[17]++;
                    }
                    else if(pawnsInPosition[17] == 2){
                        pawns.get(username).setLayoutX(99);
                        pawns.get(username).setLayoutY(153);
                        pawnsInPosition[17]++;
                    }
                    else if(pawnsInPosition[17] == 3){
                        pawns.get(username).setLayoutX(101);
                        pawns.get(username).setLayoutY(150);
                        pawnsInPosition[17]++;
                    }
                    break;

                case 18:
                    if(pawnsInPosition[18] == 0){
                        pawns.get(username).setLayoutX(135);
                        pawns.get(username).setLayoutY(159);
                        pawnsInPosition[18]++;
                    }
                    else if(pawnsInPosition[18] == 1){
                        pawns.get(username).setLayoutX(137);
                        pawns.get(username).setLayoutY(156);
                        pawnsInPosition[18]++;
                    }
                    else if(pawnsInPosition[18] == 2){
                        pawns.get(username).setLayoutX(139);
                        pawns.get(username).setLayoutY(153);
                        pawnsInPosition[18]++;
                    }
                    else if(pawnsInPosition[18] == 3){
                        pawns.get(username).setLayoutX(141);
                        pawns.get(username).setLayoutY(150);
                        pawnsInPosition[18]++;
                    }
                    break;

                case 19:
                    if(pawnsInPosition[19] == 0){
                        pawns.get(username).setLayoutX(135);
                        pawns.get(username).setLayoutY(122);
                        pawnsInPosition[15]++;
                    }
                    else if(pawnsInPosition[19] == 1){
                        pawns.get(username).setLayoutX(137);
                        pawns.get(username).setLayoutY(119);
                        pawnsInPosition[19]++;
                    }
                    else if(pawnsInPosition[19] == 2){
                        pawns.get(username).setLayoutX(139);
                        pawns.get(username).setLayoutY(116);
                        pawnsInPosition[19]++;
                    }
                    else if(pawnsInPosition[19] == 3){
                        pawns.get(username).setLayoutX(141);
                        pawns.get(username).setLayoutY(113);
                        pawnsInPosition[19]++;
                    }
                    break;

                case 20:
                    if(pawnsInPosition[20] == 0){
                        pawns.get(username).setLayoutX(75);
                        pawns.get(username).setLayoutY(106);
                        pawnsInPosition[20]++;
                    }
                    else if(pawnsInPosition[20] == 1){
                        pawns.get(username).setLayoutX(77);
                        pawns.get(username).setLayoutY(103);
                        pawnsInPosition[20]++;
                    }
                    else if(pawnsInPosition[20] == 2){
                        pawns.get(username).setLayoutX(79);
                        pawns.get(username).setLayoutY(100);
                        pawnsInPosition[20]++;
                    }
                    else if(pawnsInPosition[20] == 3){
                        pawns.get(username).setLayoutX(81);
                        pawns.get(username).setLayoutY(97);
                        pawnsInPosition[20]++;
                    }
                    break;

                case 21:
                    if(pawnsInPosition[21] == 0){
                        pawns.get(username).setLayoutX(15);
                        pawns.get(username).setLayoutY(122);
                        pawnsInPosition[21]++;
                    }
                    else if(pawnsInPosition[21] == 1){
                        pawns.get(username).setLayoutX(17);
                        pawns.get(username).setLayoutY(119);
                        pawnsInPosition[21]++;
                    }
                    else if(pawnsInPosition[21] == 2){
                        pawns.get(username).setLayoutX(19);
                        pawns.get(username).setLayoutY(116);
                        pawnsInPosition[21]++;
                    }
                    else if(pawnsInPosition[21] == 3){
                        pawns.get(username).setLayoutX(21);
                        pawns.get(username).setLayoutY(113);
                        pawnsInPosition[21]++;
                    }
                    break;

                case 22:
                     if(pawnsInPosition[22] == 0){
                        pawns.get(username).setLayoutX(15);
                        pawns.get(username).setLayoutY(75);
                        pawnsInPosition[22]++;
                    }
                    else if(pawnsInPosition[22] == 1){
                        pawns.get(username).setLayoutX(17);
                        pawns.get(username).setLayoutY(72);
                        pawnsInPosition[22]++;
                    }
                    else if(pawnsInPosition[22] == 2){
                        pawns.get(username).setLayoutX(19);
                        pawns.get(username).setLayoutY(69);
                        pawnsInPosition[22]++;
                    }
                    else if(pawnsInPosition[22] == 3){
                        pawns.get(username).setLayoutX(21);
                        pawns.get(username).setLayoutY(66);
                        pawnsInPosition[22]++;
                    }
                    break;

                case 23:
                    if(pawnsInPosition[23] == 0){
                        pawns.get(username).setLayoutX(15);
                        pawns.get(username).setLayoutY(38);
                        pawnsInPosition[23]++;
                    }
                    else if(pawnsInPosition[23] == 1){
                        pawns.get(username).setLayoutX(17);
                        pawns.get(username).setLayoutY(35);
                        pawnsInPosition[23]++;
                    }
                    else if(pawnsInPosition[23] == 2){
                        pawns.get(username).setLayoutX(19);
                        pawns.get(username).setLayoutY(32);
                        pawnsInPosition[23]++;
                    }
                    else if(pawnsInPosition[23] == 3){
                        pawns.get(username).setLayoutX(21);
                        pawns.get(username).setLayoutY(29);
                        pawnsInPosition[23]++;
                    }
                    break;

                case 24:
                      if(pawnsInPosition[24] == 0){
                        pawns.get(username).setLayoutX(35);
                        pawns.get(username).setLayoutY(18);
                        pawnsInPosition[24]++;
                    }
                    else if(pawnsInPosition[24] == 1){
                        pawns.get(username).setLayoutX(37);
                        pawns.get(username).setLayoutY(15);
                        pawnsInPosition[24]++;
                    }
                    else if(pawnsInPosition[24] == 2){
                        pawns.get(username).setLayoutX(39);
                        pawns.get(username).setLayoutY(12);
                        pawnsInPosition[24]++;
                    }
                    else if(pawnsInPosition[24] == 3){
                        pawns.get(username).setLayoutX(41);
                        pawns.get(username).setLayoutY(9);
                        pawnsInPosition[24]++;
                    }
                    break;

                case 25:
                     if(pawnsInPosition[25] == 0){
                        pawns.get(username).setLayoutX(75);
                        pawns.get(username).setLayoutY(12);
                        pawnsInPosition[25]++;
                    }
                    else if(pawnsInPosition[25] == 1){
                        pawns.get(username).setLayoutX(77);
                        pawns.get(username).setLayoutY(9);
                        pawnsInPosition[25]++;
                    }
                    else if(pawnsInPosition[25] == 2){
                        pawns.get(username).setLayoutX(79);
                        pawns.get(username).setLayoutY(6);
                        pawnsInPosition[25]++;
                    }
                    else if(pawnsInPosition[25] == 3){
                        pawns.get(username).setLayoutX(81);
                        pawns.get(username).setLayoutY(3);
                        pawnsInPosition[25]++;
                    }
                    break;

                case 26:
                      if(pawnsInPosition[26] == 0){
                        pawns.get(username).setLayoutX(112);
                        pawns.get(username).setLayoutY(18);
                        pawnsInPosition[26]++;
                    }
                    else if(pawnsInPosition[26] == 1){
                        pawns.get(username).setLayoutX(114);
                        pawns.get(username).setLayoutY(15);
                        pawnsInPosition[26]++;
                    }
                    else if(pawnsInPosition[26] == 2){
                        pawns.get(username).setLayoutX(116);
                        pawns.get(username).setLayoutY(12);
                        pawnsInPosition[26]++;
                    }
                    else if(pawnsInPosition[26] == 3){
                        pawns.get(username).setLayoutX(118);
                        pawns.get(username).setLayoutY(9);
                        pawnsInPosition[26]++;
                    }
                    break;

                case 27:
                        if(pawnsInPosition[27] == 0){
                            pawns.get(username).setLayoutX(135);
                            pawns.get(username).setLayoutY(38);
                            pawnsInPosition[27]++;
                        }
                        else if(pawnsInPosition[27] == 1){
                            pawns.get(username).setLayoutX(137);
                            pawns.get(username).setLayoutY(35);
                            pawnsInPosition[27]++;
                        }
                        else if(pawnsInPosition[27] == 2){
                            pawns.get(username).setLayoutX(139);
                            pawns.get(username).setLayoutY(32);
                            pawnsInPosition[27]++;
                        }
                        else if(pawnsInPosition[27] == 3){
                            pawns.get(username).setLayoutX(141);
                            pawns.get(username).setLayoutY(29);
                            pawnsInPosition[27]++;
                        }
                    break;

                case 28:
                    if(pawnsInPosition[28] == 0){
                        pawns.get(username).setLayoutX(135);
                        pawns.get(username).setLayoutY(75);
                        pawnsInPosition[28]++;
                    }
                    else if(pawnsInPosition[28] == 1){
                        pawns.get(username).setLayoutX(137);
                        pawns.get(username).setLayoutY(72);
                        pawnsInPosition[28]++;
                    }
                    else if(pawnsInPosition[28] == 2){
                        pawns.get(username).setLayoutX(139);
                        pawns.get(username).setLayoutY(69);
                        pawnsInPosition[28]++;
                    }
                    else if(pawnsInPosition[28] == 3){
                        pawns.get(username).setLayoutX(141);
                        pawns.get(username).setLayoutY(66);
                        pawnsInPosition[28]++;
                    }
                    break;

                case 29:
                    if(pawnsInPosition[29] == 0){
                        pawns.get(username).setLayoutX(75);
                        pawns.get(username).setLayoutY(57);
                        pawnsInPosition[29]++;
                    }
                    else if(pawnsInPosition[29] == 1){
                        pawns.get(username).setLayoutX(77);
                        pawns.get(username).setLayoutY(54);
                        pawnsInPosition[29]++;
                    }
                    else if(pawnsInPosition[29] == 2){
                        pawns.get(username).setLayoutX(79);
                        pawns.get(username).setLayoutY(51);
                        pawnsInPosition[29]++;
                    }
                    else if(pawnsInPosition[29] == 3){
                        pawns.get(username).setLayoutX(81);
                        pawns.get(username).setLayoutY(49);
                        pawnsInPosition[29]++;
                    }
                    break;


                default:
                    break;
            }
                anchorPane.getChildren().add(pawns.get(username));


        }
    }





}
