package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.model.message.response.AngleSelectedMessage;
import it.polimi.ingsw.core.model.message.response.CardSelectedMessage;
import it.polimi.ingsw.core.model.message.response.SelectedDrewCard;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.ui.GUI.GUI;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

import java.io.IOException;
import java.util.*;


public class BoardViewController extends GUI {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    ImageView iconaScreen;
    @FXML
    BorderPane boardPane;

    @FXML
    VBox playersRecapVbox;

    @FXML
    private GridPane gridPane;

    private Font myFont = Font.font("Poor Richard", 15);

    private boolean test= false;

    @FXML
    private VBox containerVBoxRight;
    @FXML
    private VBox[] playersVBoxesRecap = new VBox[4];
    @FXML
    private HBox[] playersRow1 = new HBox[4];
    @FXML
    private HBox[] playersRow2 = new HBox[4];
    @FXML
    private HBox[] playersRow3 = new HBox[4];
    @FXML
    private HBox[] hBoxesFungi = new HBox[4];
    @FXML
    private HBox[] hBoxesInsect = new HBox[4];
    @FXML
    private HBox[] hBoxesAnimals = new HBox[4];
    @FXML
    private HBox[] hBoxesPlant = new HBox[4];
    @FXML
    private HBox[] hBoxesQuill = new HBox[4];
    @FXML
    private HBox[] hBoxesManuscript = new HBox[4];
    @FXML
    private HBox[] hBoxesInkwell = new HBox[4];
    @FXML
    private Label[] numFungi = new Label[4];
    @FXML
    private Label[] numInsect = new Label[4];
    @FXML
    private Label[] numAnimals = new Label[4];
    @FXML
    private Label[] numPlant = new Label[4];
    @FXML
    private Label[] numQuill = new Label[4];
    @FXML
    private Label[] numManuscript = new Label[4];
    @FXML
    private Label[] numInkwell = new Label[4];
    @FXML
    private Label[] playersPoints = new Label[4];
    @FXML
    private Image[] playersPawn = new Image[4];
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private ImageView card1ImageView, card2ImageView, card3ImageView;
    @FXML
    private ImageView obj1ImageView;
    @FXML
    private ImageView objSecretImageView;
    @FXML
    private ImageView obj2ImageView;
    @FXML
    private ImageView deckRBackImageView, deckRFront1ImageView, deckRFront2ImageView, deckRBackEco1, deckRBackEco2;
    @FXML
    private ImageView deckGBackImageView, deckGFront1ImageView, deckGFront2ImageView, deckGBackEco1, deckGBackEco2;
    @FXML
    private Button buttonDeckGBack, buttonDeckRBack, buttonDeckRFront1, buttonDeckRFront2, buttonDeckGFront1, buttonDeckGFront2;
    @FXML
    private Button buttonSide;
    @FXML
    private Button buttonCard1, buttonCard2, buttonCard3;

    @FXML
    private Button[] handButtons = {buttonCard1, buttonCard2, buttonCard3};
    @FXML
    private Button[] deckVisibleButtons ={ buttonDeckRFront1, buttonDeckRFront2, buttonDeckGFront1, buttonDeckGFront2};
    @FXML
    private Button[] deckButtons ={buttonDeckRBack, buttonDeckGBack, buttonDeckRFront1, buttonDeckRFront2, buttonDeckGFront1, buttonDeckGFront2};
    @FXML
    private ImageView[] handImages = {card1ImageView, card2ImageView, card3ImageView};
    @FXML
    private Label labelTurn;

    private static Boolean cardSelected = false;
    private static Boolean cardDrawn = false;
    private static Boolean cardPlaced = false;
    //side of the card in the hand
    private static Boolean side = true;
    private static String buttonCardSelectedId;
    private static String angleChosen;

    //useful to save temporarly the card i want to position because i need it in an another method
    //it contains also the side choosed
    private static CardGame cardToPlace;
    //where i want to position the card
    private static Integer[] positionToPlace;
    //usefull to save temporary the path of the card image
    private String cardFront;
    //usefull to save the image of the card that the player whant to play, is back or front side because I take it from its button selected
    private static CardGame cardToPlay;

    public void setUpBoard() {
        VBox container = new VBox();
        container.setMaxHeight(600);
        container.setMaxWidth(200);
        container.setMinHeight(600);
        container.setMinWidth(200);
        container.alignmentProperty().set(Pos.CENTER);
        playersRecapVbox = new VBox();
        playersRecapVbox.setMaxHeight(400);
        playersRecapVbox.setMaxWidth(200);
        playersRecapVbox.setMinHeight(400);
        playersRecapVbox.setMinWidth(200);
        playersRecapVbox.getStyleClass().add("containerPoints");
        //playersRecapVbox.styleProperty().set("-fx-background-color: #E3DBB5");
        playersRecapVbox.alignmentProperty().set(Pos.CENTER);
        setPlayersRecapVbox(playersRecapVbox);
        container.getChildren().add(playersRecapVbox);
        boardPane.setLeft(container);

        //I load the first hand of the player
        //TODO ELIMINATE WHEN WAIT FOR THE USERNAME AT THE BEGINNING IMPLEMENTED
        this.updateHand(this.viewModel.getMyHand());
        cardToPlace = this.viewModel.getMyCodex().get(0);

        //I load the starter card choosed by the player
        this.placeCard(cardToPlace, new Integer[]{40, 40});

        //I load the objectives of the player
        String objSecretCover = this.viewModel.getSecretObj().getFrontCover();
        String obj1Cover = this.viewModel.getCommonObj().get(0).getFrontCover();
        String obj2Cover = this.viewModel.getCommonObj().get(1).getFrontCover();
        objSecretImageView.setImage(new Image(objSecretCover));
        obj1ImageView.setImage(new Image(obj1Cover));
        obj2ImageView.setImage(new Image(obj2Cover));

        //loading the initial decks
        //TODO ELIMINATE WHEN WAIT FOR THE USERNAME AT THE BEGINNING IMPLEMENTED
        List<Card> initialDecks = new ArrayList<>();
        initialDecks.addAll(this.viewModel.getResourceCardsVisible());
        initialDecks.addAll(this.viewModel.getGoldCardsVisible());
        initialDecks.add(this.viewModel.getDeckRBack());
        initialDecks.add(this.viewModel.getDeckGBack());
        this.updateDecks(initialDecks);
    }


    public void setPlayersRecapVbox(VBox playersRecapVbox){
        //NB: the order in the array of labels is the same of the order of the players in the game
        //TODO : poi il for sarà fino a quanto numero max di giocatori della partita
       // for(int i=0; i<4; i++){
        for (int i=0; i<viewModel.getPlayers().size(); i++){
            numAnimals[i] = (new Label("0 "));
            numAnimals[i].setFont(myFont);
            numInsect[i] = (new Label("0 "));
            numInsect[i].setFont(myFont);
            numFungi[i] = (new Label("0 "));
            numFungi[i].setFont(myFont);
            numPlant[i] = (new Label("0 "));
            numPlant[i].setFont(myFont);
            numQuill[i] = (new Label("0 "));
            numQuill[i].setFont(myFont);
            numManuscript[i] = (new Label("0 "));
            numManuscript[i].setFont(myFont);
            numInkwell[i] = (new Label("0 "));
            numInkwell[i].setFont(myFont);
            playersPoints[i] = (new Label(" pt. : 0"));
            playersPoints[i].setFont(myFont);

            playersVBoxesRecap[i] = (new VBox());
            playersVBoxesRecap[i].alignmentProperty().set(Pos.CENTER);
            playersVBoxesRecap[i].setPrefHeight(130);
            playersVBoxesRecap[i].setPrefWidth(200);

            playersRow1[i] = (new HBox());
            playersRow1[i].alignmentProperty().set(Pos.CENTER);
            playersRow1[i].setPrefHeight(43);
            playersRow1[i].setPrefWidth(200);
            String text = "Player: " + viewModel.getPlayers().get(i);
            playersRow1[i].getChildren().add(new Label(text));
            playersRow1[i].getChildren().add(playersPoints[i]);

            playersRow2[i] = (new HBox() );
            playersRow2[i].alignmentProperty().set(Pos.CENTER);
            playersRow2[i].setPrefHeight(43);
            playersRow2[i].setPrefWidth(200);

            hBoxesAnimals[i] = ( new HBox());
            ImageView imageView = new ImageView("/it/polimi/ingsw/images/resources/animal.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesAnimals[i].getChildren().add(imageView);
            hBoxesAnimals[i].getChildren().add(numAnimals[i]);

            hBoxesInsect[i] = (new HBox() );
            imageView = new ImageView("/it/polimi/ingsw/images/resources/insect.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesInsect[i].getChildren().add(imageView);
            hBoxesInsect[i].getChildren().add(numInsect[i]);

            hBoxesFungi[i] = (new HBox() );
            imageView = new ImageView("/it/polimi/ingsw/images/resources/fungi.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesFungi[i].getChildren().add(imageView);
            hBoxesFungi[i].getChildren().add(numFungi[i]);

            hBoxesPlant[i] = ( new HBox());
            imageView = new ImageView("/it/polimi/ingsw/images/resources/plant.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesPlant[i].getChildren().add(imageView);
            hBoxesPlant[i].getChildren().add(numPlant[i]);

            playersRow2[i].getChildren().add(hBoxesAnimals[i]);
            playersRow2[i].getChildren().add(hBoxesInsect[i]);
            playersRow2[i].getChildren().add(hBoxesFungi[i]);
            playersRow2[i].getChildren().add(hBoxesPlant[i]);

            playersRow3[i] = ( new HBox());
            playersRow3[i].alignmentProperty().set(Pos.CENTER);
            playersRow3[i].setPrefHeight(43);
            playersRow3[i].setPrefWidth(200);

            hBoxesQuill[i] = ( new HBox());
            imageView = new ImageView("/it/polimi/ingsw/images/resources/quill.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesQuill[i].getChildren().add(imageView);
            hBoxesQuill[i].getChildren().add(numQuill[i]);

            hBoxesManuscript[i] = (new HBox());
            imageView = new ImageView("/it/polimi/ingsw/images/resources/manuscript.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesManuscript[i].getChildren().add(imageView);
            hBoxesManuscript[i].getChildren().add(numManuscript[i]);

            hBoxesInkwell[i] = (new HBox());
            imageView = new ImageView("/it/polimi/ingsw/images/resources/inkwell.png");
            imageView.setFitHeight(30);
            imageView.setFitWidth(25);
            hBoxesInkwell[i].getChildren().add(imageView);
            hBoxesInkwell[i].getChildren().add(numInkwell[i]);

            playersRow3[i].getChildren().add(hBoxesQuill[i]);
            playersRow3[i].getChildren().add(hBoxesManuscript[i]);
            playersRow3[i].getChildren().add(hBoxesInkwell[i]);

            //poi sarà se la i è uguale al numero ID del giocatore di cui è la board (client)
            //ho dovuto mettere un booleano perchè non so come mai ma entrava nell'if due volte
            if(viewModel.getPlayers().get(i).equals(viewModel.getMyUsername() ) && test==false){
                containerVBoxRight.getChildren().add(playersRow1[i]);
                containerVBoxRight.getChildren().add(playersRow2[i]);
                containerVBoxRight.getChildren().add(playersRow3[i]);
                test=true;
            }

            else if(! viewModel.getPlayers().get(i).equals(viewModel.getMyUsername() )){
                playersVBoxesRecap[i].getChildren().add(playersRow1[i]);
                playersVBoxesRecap[i].getChildren().add(playersRow2[i]);
                playersVBoxesRecap[i].getChildren().add(playersRow3[i]);
                playersRecapVbox.getChildren().add(playersVBoxesRecap[i]);
            }


        }
    }

    //dato l'id della carta,front e back e la posizione, la stampo nella board
    public void placeCard(CardGame cardToPlace, Integer[] position){
        if(cardToPlace.isFrontSide()){
            cardFront = cardToPlace.getFrontCover();
        }
        else{
            cardFront = cardToPlace.getBackCover();
        }
        ImageView image = new ImageView(new Image(cardFront));
        image.setFitHeight(83);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, position[0], position[1]);


        scrollPane.setHvalue(0.5);
        scrollPane.setVvalue(0.5);

    }


    //quando chiamo questo metodo modifico la carta del mazzo (resource r oppure gold g)
    //in posizione: 0 -> girata (il back), 1 -> esposta 1, 2 -> esposta 2)
    public void updateDecks (List<Card> updatedDecks){
        //TODO CAPIRE COME PARAMETRIZZARE MEGLIO LA COSA
        String imageCardRBack = updatedDecks.get(5).getBackCover();
        deckGBackImageView.setImage(new Image(imageCardRBack));
        deckGBackEco1.setImage(new Image(imageCardRBack));
        deckGBackEco2.setImage(new Image(imageCardRBack));
        buttonDeckGBack.setUserData(updatedDecks.get(5).getId());
        buttonDeckGBack.getStyleClass().add("buttonCard");


        String imageCardGBack = updatedDecks.get(4).getBackCover();
        deckRBackImageView.setImage(new Image(imageCardGBack));
        deckRBackEco1.setImage(new Image(imageCardGBack));
        deckRBackEco2.setImage(new Image(imageCardGBack));
        buttonDeckRBack.setUserData(updatedDecks.get(4).getId());
        buttonDeckRBack.getStyleClass().add("buttonCard");

        String imageCardRFront1 = updatedDecks.get(0).getFrontCover();
        deckRFront1ImageView.setImage(new Image(imageCardRFront1));
        buttonDeckRFront1.setUserData(updatedDecks.get(0).getId());
        buttonDeckRFront1.getStyleClass().add("buttonCard");

        String imageCardRFront2 = updatedDecks.get(1).getFrontCover();
        deckRFront2ImageView.setImage(new Image(imageCardRFront2));
        buttonDeckRFront2.setUserData(updatedDecks.get(1).getId());
        buttonDeckRFront2.getStyleClass().add("buttonCard");

        String imageCardGFront1 = updatedDecks.get(2).getFrontCover();
        deckGFront1ImageView.setImage(new Image(imageCardGFront1));
        buttonDeckGFront1.setUserData(updatedDecks.get(2).getId());
        buttonDeckGFront1.getStyleClass().add("buttonCard");

        String imageCardGFront2 = updatedDecks.get(3).getFrontCover();
        deckGFront2ImageView.setImage(new Image(imageCardGFront2));
        buttonDeckGFront2.setUserData(updatedDecks.get(3).getId());
        buttonDeckGFront2.getStyleClass().add("buttonCard");

        //TODO ASK perchè non funziona
        /*
        for(Button b: deckButtons){
            b.setOnAction(e -> {
                this.showErrorPopUp("You can't draw a card now", (Stage) b.getScene().getWindow());
            });
            b.setOnMouseEntered(e -> {
                b.setStyle("-fx-border-color: #e51f1f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
            });
            b.setOnMouseExited(e -> {
                b.setStyle("-fx-border-color: none;\n" +
                        "-fx-effect: none;");
            });
        }

         */
        buttonDeckGBack.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckGBack.getScene().getWindow());
        });
        buttonDeckGBack.setOnMouseEntered(e -> {
            buttonDeckGBack.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckGBack.setOnMouseExited(e -> {
            buttonDeckGBack.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckRBack.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckRBack.getScene().getWindow());
        });
        buttonDeckRBack.setOnMouseEntered(e -> {
            buttonDeckRBack.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckRBack.setOnMouseExited(e -> {
            buttonDeckRBack.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckRFront1.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckRFront1.getScene().getWindow());
        });
        buttonDeckRFront1.setOnMouseEntered(e -> {
            buttonDeckRFront1.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckRFront1.setOnMouseExited(e -> {
            buttonDeckRFront1.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckRFront2.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckRFront2.getScene().getWindow());
        });
        buttonDeckRFront2.setOnMouseEntered(e -> {
            buttonDeckRFront2.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckRFront2.setOnMouseExited(e -> {
            buttonDeckRFront2.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckGFront1.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckGFront1.getScene().getWindow());
        });
        buttonDeckGFront1.setOnMouseEntered(e -> {
            buttonDeckGFront1.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckGFront1.setOnMouseExited(e -> {
            buttonDeckGFront1.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckGFront2.setOnAction(e -> {
            this.showErrorPopUp("You can't draw a card now", (Stage) buttonDeckRFront2.getScene().getWindow());
        });
        buttonDeckGFront2.setOnMouseEntered(e -> {
            buttonDeckGFront2.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonDeckGFront2.setOnMouseExited(e -> {
            buttonDeckGFront2.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });


    }

    public void setFullScreen(ActionEvent actionEvent) {
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
            iconaScreen.setImage(new Image("/it/polimi/ingsw/gc38/icons/iconaFullScreen.png"));
        } else {
            stage.setFullScreen(true);
            iconaScreen.setImage(new Image("/it/polimi/ingsw/gc38/icons/iconaMinimizeScreen.png"));
        }
    }


    public void setSide(ActionEvent actionEvent) {
        if(buttonSide.getText().equals("view back side")){
            /*
            for(int i=0; i<3; i++){
                String imageCard = viewModel.getMyHand().get(i).getBackCover();
                viewModel.getMyHand().get(i).setSide(false);
                handImages[i].setImage(new Image(imageCard));
            }*/

            String imageCard1 = viewModel.getMyHand().get(0).getBackCover();
            String imageCard2 = viewModel.getMyHand().get(1).getBackCover();
            String imageCard3 = viewModel.getMyHand().get(2).getBackCover();
            viewModel.getMyHand().get(0).setSide(false);
            viewModel.getMyHand().get(1).setSide(false);
            viewModel.getMyHand().get(2).setSide(false);
            this.side = false;
            card1ImageView.setImage(new Image(imageCard1));
            card2ImageView.setImage(new Image(imageCard2));
            card3ImageView.setImage(new Image(imageCard3));

            buttonSide.setText("view front side");
        }
        else{
            /*
            for(int i=0; i<3; i++){
                String imageCard = viewModel.getMyHand().get(i).getFrontCover();
                viewModel.getMyHand().get(i).setSide(true);
                handImages[i].setImage(new Image(imageCard));
            }
            */
            String imageCard1 = viewModel.getMyHand().get(0).getFrontCover();
            String imageCard2 = viewModel.getMyHand().get(1).getFrontCover();
            String imageCard3 = viewModel.getMyHand().get(2).getFrontCover();
            //TODO ASK IF THIS ITS OKAY
            viewModel.getMyHand().get(0).setSide(true);
            viewModel.getMyHand().get(1).setSide(true);
            viewModel.getMyHand().get(2).setSide(true);
            this.side = true;
            card1ImageView.setImage(new Image(imageCard1));
            card2ImageView.setImage(new Image(imageCard2));
            card3ImageView.setImage(new Image(imageCard3));

            buttonSide.setText("view back side");
        }
    }

    public void viewScoreboard(ActionEvent actionEvent) {
        Stage popUpStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        this.showScoreboardPopUp(popUpStage);
    }


    public void setTurn(String messageTurn){

    }

    public void visualizeChat(ActionEvent actionEvent) throws IOException {
        Stage popUpStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        this.viewChat(popUpStage);
    }

    public void updateHand(List<Card> hand) {
        //todo ask perchè mi da problemi l'array
       /* for(int i=0; i<3; i++){
            String imageCard = hand.get(i).getFrontCover();
            handImages[i].setImage(new Image(imageCard));
            handButtons[i].setUserData(hand.get(i).getId());
        }
        for(Button b: handButtons){
            b.setOnAction(e -> {
                this.showErrorPopUp("You can't draw a card now", (Stage) b.getScene().getWindow());
            });
            b.setOnMouseEntered(e -> {
                b.setStyle("-fx-border-color: #e51f1f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
            });
            b.setOnMouseExited(e -> {
                b.setStyle("-fx-border-color: none;\n" +
                        "-fx-effect: none;");
            });
        }

        */

        String imageCard1 = hand.get(0).getFrontCover();
        card1ImageView.setImage(new Image(imageCard1));
        //I set in the button the id con the card it is referred to
        buttonCard1.setUserData(hand.get(0).getId());
        buttonCard1.getStyleClass().add("buttonCard");
        buttonCard1.setOnAction(e -> {
            this.showErrorPopUp("You can't play the card now, it's not your turn", (Stage) buttonCard1.getScene().getWindow());
        });
        buttonCard1.setOnMouseEntered(e -> {
            buttonCard1.setStyle("-fx-border-color: #e51f1f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonCard1.setOnMouseExited(e -> {
            buttonCard1.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        String imageCard2 = hand.get(1).getFrontCover();
        card2ImageView.setImage(new Image(imageCard2));
        buttonCard2.setUserData(hand.get(1).getId());
        buttonCard2.getStyleClass().add("buttonCard");
        buttonCard2.setOnAction(e -> {
            this.showErrorPopUp("You can't play the card now, it's not your turn", (Stage) buttonCard2.getScene().getWindow());
        });
        buttonCard2.setOnMouseEntered(e -> {
            buttonCard2.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonCard2.setOnMouseExited(e -> {
            buttonCard2.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        String imageCard3 = hand.get(2).getFrontCover();
        card3ImageView.setImage(new Image(imageCard3));
        buttonCard3.setUserData(hand.get(2).getId());
        buttonCard3.getStyleClass().add("buttonCard");
        buttonCard3.setOnAction(e -> {
            this.showErrorPopUp("You can't play the card now, it's not your turn", (Stage) buttonCard3.getScene().getWindow());
        });
        buttonCard3.setOnMouseEntered(e -> {
            buttonCard3.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });
        buttonCard3.setOnMouseExited(e -> {
            buttonCard3.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });
    }

    public void onButtonPressed() {

    }


    public void selectCardToPlay(PlayableCardIds cardsOnlyBack){
        this.cardSelected = false;
        this.message("IT'S YOUR TURN!\nSelect the card you want to play");
        //quando clicco il bottone mando update al client della scelta adottata
        //TODO ask perchè non funziona
        /*
        for(Button b: handButtons){
            b.setOnAction(e -> {
                if(!cardSelected){
                    b.setStyle("-fx-border-color: #52e51f;\n" +
                            "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                    CardSelection cs = new CardSelection((int)b.getUserData(), this.side);
                    buttonCardSelectedId = b.getId();
                    this.observerClient.updateUI(new GameEvent("cardToPlaySelected", cs));
                    cardSelected = true;
                }
                else {
                    this.showErrorPopUp("You have already chosen the card to play", (Stage) b.getScene().getWindow());
                }

            });
        }
        if(buttonCard1.getId().equals(buttonCardSelectedId)){
            cardToPlay = this.viewModel.getMyHand().get(0);
        }
        else if(buttonCard2.getId().equals(buttonCardSelectedId)){
            cardToPlay = this.viewModel.getMyHand().get(1);
        }
        else if(buttonCard3.getId().equals(buttonCardSelectedId)){
            cardToPlay = this.viewModel.getMyHand().get(2);
        }

         */

        buttonCard1.setOnAction(e -> {
            Boolean playableFront = true;
            if(cardsOnlyBack.getPlayingHandIdsBack().contains((int)buttonCard1.getUserData())){
                playableFront = false;
            }
            if(!cardSelected){
                buttonCard1.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                if(!playableFront && this.side){
                    this.showErrorPopUp("You can't play this card front, only its back", (Stage) buttonCard1.getScene().getWindow());
                }
                else{
                    CardSelection cs = new CardSelection((int)buttonCard1.getUserData(), this.side);
                    cardToPlay = this.viewModel.getMyHand().get(0);
                    buttonCardSelectedId = buttonCard1.getId();
                    client.sendMessage(new CardSelectedMessage("cardSelection", cs));
                    cardSelected = true;
                }
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonCard1.getScene().getWindow());
            }

        });
        buttonCard1.setOnMouseEntered(e -> {
            buttonCard1.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonCard1.setOnMouseExited(e -> {
            buttonCard1.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });


        buttonCard2.setOnAction(e -> {
            Boolean playableFront = true;
            if(cardsOnlyBack.getPlayingHandIdsBack().contains((int)buttonCard2.getUserData())){
                playableFront = false;
            }
            if(!cardSelected){
                buttonCard2.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                if(!playableFront && this.side){
                    this.showErrorPopUp("You can't play this card front, only its back", (Stage) buttonCard1.getScene().getWindow());
                }
                else{
                    CardSelection cs = new CardSelection((int)buttonCard2.getUserData(),this.side );
                    cardToPlay = this.viewModel.getMyHand().get(1);
                    buttonCardSelectedId = buttonCard2.getId();
                    client.sendMessage(new CardSelectedMessage("cardSelection", cs));
                    cardSelected = true;
                }
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonCard2.getScene().getWindow());
            }

        });
        buttonCard2.setOnMouseEntered(e -> {
            buttonCard2.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonCard2.setOnMouseExited(e -> {
            buttonCard2.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        buttonCard3.setOnAction(e -> {
            Boolean playableFront = true;
            if(cardsOnlyBack.getPlayingHandIdsBack().contains((int)buttonCard3.getUserData())){
                playableFront = false;
            }
            if(!cardSelected){
                if(!playableFront && this.side){
                    this.showErrorPopUp("You can't play this card front, only its back", (Stage) buttonCard1.getScene().getWindow());
                }
                else{
                    buttonCard3.setStyle("-fx-border-color: #52e51f;\n" +
                            "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                    CardSelection cs = new CardSelection((int)buttonCard3.getUserData(), this.side);
                    cardToPlay = this.viewModel.getMyHand().get(2);
                    buttonCardSelectedId = buttonCard3.getId();
                    client.sendMessage(new CardSelectedMessage("cardSelection", cs));
                    cardSelected = true;
                }
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonCard3.getScene().getWindow());
            }
        });
        buttonCard3.setOnMouseEntered(e -> {
            buttonCard3.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonCard3.setOnMouseExited(e -> {
            buttonCard3.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });


    }

    public void askForAngle(List<Coordinate> angles){
        for(Coordinate c: angles){
            System.out.printf("Card: %d, Angle: %d\n", c.getX(), c.getY());
        }
        this.cardPlaced = false;
        this.message("Select where you want \n to play the card");

        /*TODO ask perchè non va
        for(Button b: handButtons){
            if(b.getId().equals(buttonCardSelectedId)){
                b.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
            }
            b.setOnAction(e -> {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) b.getScene().getWindow());
            });
            b.setOnMouseEntered(e -> {
                b.setStyle("-fx-border-color: #e51f1f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
            });
            b.setOnMouseExited(e -> {
                b.setStyle("-fx-border-color: none;\n" +
                        "-fx-effect: none;");
            });
        }

         */
        //TODO ASK WHY IT DOESN'T WORK
        if(buttonCard1.getId().equals(buttonCardSelectedId)){
            buttonCard1.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        }
        else if(buttonCard2.getId().equals(buttonCardSelectedId)){
            buttonCard2.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        }
        else if(buttonCard3.getId().equals(buttonCardSelectedId)){
            buttonCard3.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        }
        buttonCard1.setOnAction(e -> {
            this.showErrorPopUp("You already selected a card to play", (Stage) buttonCard1.getScene().getWindow());
            });

        //TODO guarda se è questo che non ti fa cambiare il colore

        buttonCard1.setOnMouseEntered(e -> {
            buttonCard1.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });

        buttonCard1.setOnMouseExited(e -> {
            buttonCard1.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });


        buttonCard2.setOnAction(e -> {
            this.showErrorPopUp("You already selected a card to play", (Stage) buttonCard2.getScene().getWindow());
        });


        buttonCard2.setOnMouseEntered(e -> {
            buttonCard2.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });

        buttonCard2.setOnMouseExited(e -> {
            buttonCard2.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });


        buttonCard3.setOnAction(e -> {
            this.showErrorPopUp("You already selected a card to play", (Stage) buttonCard3.getScene().getWindow());
        });


        buttonCard3.setOnMouseEntered(e -> {
            buttonCard3.setStyle("-fx-border-color: #e51f1f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #9d1717, 20, 0.8, 0, 0);");
        });

        buttonCard3.setOnMouseExited(e -> {
            buttonCard3.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        List<Integer[]> tempButtons = new ArrayList<>();
       for(Coordinate c : angles){
           int cardToAttach = c.getX();
           int angle = c.getY();
           int[][] matrix = viewModel.getMyMatrix();
          //TODO FIND A MORE EASY WAY TO SEARCH IN THE MATRIX
           for(int i=0; i< matrix.length; i++){
               for(int j=0; j<matrix.length; j++){
                   if(matrix[i][j] == cardToAttach){
                       positionToPlace = new Integer[]{i,j};
                   }
               }
           }
           Button placeHere = new Button();
           placeHere.getStyleClass().add("buttonCard");
           placeHere.setStyle("-fx-background-color: rgba(215,222,9,0.3);\n" +
                   "-fx-opacity: 0.5;");
           placeHere.setText(cardToAttach+"."+angle);
           switch (angle){
               case 0:
                   positionToPlace[0] = positionToPlace[0]-1;
                   positionToPlace[1] = positionToPlace[1]+1;
                   gridPane.add(placeHere, positionToPlace[0], positionToPlace[1]);
                   break;
               case 1:
                     positionToPlace[0] = positionToPlace[0]-1;
                     positionToPlace[1] = positionToPlace[1]-1;
                    gridPane.add(placeHere, positionToPlace[0], positionToPlace[1]);
                   break;
               case 2:
                   positionToPlace[0] = positionToPlace[0]+1;
                     positionToPlace[1] = positionToPlace[1]-1;
                     gridPane.add(placeHere, positionToPlace[0], positionToPlace[1]);
                   break;
               case 3:
                     positionToPlace[0] = positionToPlace[0]+1;
                     positionToPlace[1] = positionToPlace[1]+1;
                   gridPane.add(placeHere, positionToPlace[0], positionToPlace[1]);
                   break;
           }
           tempButtons.add(positionToPlace);
           placeHere.setUserData(positionToPlace);
              placeHere.setOnAction(e -> {
                if(!cardPlaced){
                     cardPlaced = true;
                     angleChosen = placeHere.getText();
                     client.sendMessage(new AngleSelectedMessage("angleSelection", new CardToAttachSelected(angleChosen)));
                    this.matrixUpdated(tempButtons, (Integer[]) placeHere.getUserData());
                }
                else{
                     this.showErrorPopUp("You have already chosen the position to place the card", (Stage) placeHere.getScene().getWindow());
                }
              });
       }
    }

    public void matrixUpdated(List<Integer[]> tempButtons, Integer[] positionToPlaceCard){
        for(Integer[] i : tempButtons){
            System.out.println("\nbutton removed from position: "+i[0]+", "+i[1]);
            gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node).equals(i[1]) && GridPane.getColumnIndex(node).equals(i[0]));
        }
        System.out.printf("Card placed in position: %d, %d", positionToPlaceCard[0], positionToPlaceCard[1]);
        System.out.printf("Card id to place: %d", cardToPlay.getId());
        placeCard(cardToPlay, positionToPlaceCard);
        //TODO ask if its okay here
        this.viewModel.getMyMatrix()[positionToPlaceCard[0]][positionToPlaceCard[1]] = cardToPlay.getId();

        if(buttonCard1.getId().equals(buttonCardSelectedId)){
            card1ImageView.setImage(null);
        }
        else if(buttonCard2.getId().equals(buttonCardSelectedId)){
            card2ImageView.setImage(null);
        }
        else if(buttonCard3.getId().equals(buttonCardSelectedId)){
            card3ImageView.setImage(null);
        }
    }

    public void drawFromDecks() {
        this.cardDrawn = false;
        this.message("Select the card to draw");
        //quando clicco il bottone mando update al client della scelta adottata
        buttonDeckGBack.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckGBack.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                client.sendMessage(new SelectedDrewCard("drawCard", "B"));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonCard1.getScene().getWindow());
            }

        });
        buttonDeckGBack.setOnMouseEntered(e -> {
            buttonDeckGBack.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckGBack.setOnMouseExited(e -> {
            buttonDeckGBack.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });
        buttonDeckRBack.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckRBack.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                client.sendMessage(new SelectedDrewCard("drawCard", "A"));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonCard1.getScene().getWindow());
            }
        });
        buttonDeckRBack.setOnMouseEntered(e -> {
            buttonDeckRBack.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckRBack.setOnMouseExited(e -> {
            buttonDeckRBack.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        //TODO ask perchè non va
        /*
        for(Button b: deckVisibleButtons){
            b.setOnAction(e -> {
                if(!cardDrawn){
                    b.setStyle("-fx-border-color: #52e51f;\n" +
                            "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                    this.observerClient.updateUI(new GameEvent("whereToDrawSelected", (String)b.getUserData()));
                }
                else {
                    this.showErrorPopUp("You have already chosen the card to play", (Stage) b.getScene().getWindow());
                }
            });
        }

         */
        buttonDeckRFront1.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckRFront1.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                String temp = ""+buttonDeckRFront1.getUserData();
                client.sendMessage(new SelectedDrewCard("drawCard", temp));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonDeckRFront1.getScene().getWindow());
            }
        });
        buttonDeckRFront1.setOnMouseEntered(e -> {
            buttonDeckRFront1.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckRFront1.setOnMouseExited(e -> {
            buttonDeckRFront1.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        buttonDeckRFront2.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckRFront2.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                String temp = ""+buttonDeckRFront2.getUserData();
                client.sendMessage(new SelectedDrewCard("drawCard", temp));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonDeckRFront2.getScene().getWindow());
            }
        });
        buttonDeckRFront2.setOnMouseEntered(e -> {
            buttonDeckRFront2.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckRFront2.setOnMouseExited(e -> {
            buttonDeckRFront2.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        buttonDeckGFront1.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckGFront1.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                String temp = ""+buttonDeckGFront1.getUserData();
                client.sendMessage(new SelectedDrewCard("drawCard", temp));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonDeckGFront1.getScene().getWindow());
            }
        });
        buttonDeckGFront1.setOnMouseEntered(e -> {
            buttonDeckGFront1.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckGFront1.setOnMouseExited(e -> {
            buttonDeckGFront1.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });

        buttonDeckGFront2.setOnAction(e -> {
            if(!cardDrawn){
                buttonDeckGFront2.setStyle("-fx-border-color: #52e51f;\n" +
                        "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
                cardDrawn = true;
                String temp = ""+buttonDeckGFront2.getUserData();
                client.sendMessage(new SelectedDrewCard("drawCard", temp));
            }
            else {
                this.showErrorPopUp("You have already chosen the card to play", (Stage) buttonDeckGFront2.getScene().getWindow());
            }
        });
        buttonDeckGFront2.setOnMouseEntered(e -> {
            buttonDeckGFront2.setStyle("-fx-border-color: #52e51f;\n" +
                    "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");
        });
        buttonDeckGFront2.setOnMouseExited(e -> {
            buttonDeckGFront2.setStyle("-fx-border-color: none;\n" +
                    "-fx-effect: none;");
        });
    }


    public void message(String message){
       labelTurn.setText(message);
    }


    public void updatePlayerstate(String userTarget) {
        Integer numberOrder = viewModel.getPlayerOrder().get(userTarget);
        Map<Resource,Integer> resources = viewModel.getPlayerStates().get(userTarget).getPersonalResources();
        int score = viewModel.getPlayerStates().get(userTarget).getScore();
        numAnimals[numberOrder].setText(resources.get(Resource.ANIMAL)+"");
        numInsect[numberOrder].setText(resources.get(Resource.INSECT)+"");
        numFungi[numberOrder].setText(resources.get(Resource.FUNGI)+"");
        numPlant[numberOrder].setText(resources.get(Resource.PLANT)+"");
        numQuill[numberOrder].setText(resources.get(Resource.QUILL)+"");
        numManuscript[numberOrder].setText(resources.get(Resource.MANUSCRIPT)+"");
        //todo ask if noun or inkwell
        numInkwell[numberOrder].setText(resources.get(Resource.NOUN)+"");
        playersPoints[numberOrder].setText("pt:"+score);
    }

    public void updateMyPlayerstate(){
        Integer numberOrder = viewModel.getPlayerOrder().get(viewModel.getMyUsername()) ;
        Map<Resource,Integer> resources = viewModel.getMyResources();
        int myScore = viewModel.getMyScore();
        numAnimals[numberOrder].setText(resources.get(Resource.ANIMAL)+"");
        numInsect[numberOrder].setText(resources.get(Resource.INSECT)+"");
        numFungi[numberOrder].setText(resources.get(Resource.FUNGI)+"");
        numPlant[numberOrder].setText(resources.get(Resource.PLANT)+"");
        numQuill[numberOrder].setText(resources.get(Resource.QUILL)+"");
        numManuscript[numberOrder].setText(resources.get(Resource.MANUSCRIPT)+"");
        //todo ask if noun or inkwell
        numInkwell[numberOrder].setText(resources.get(Resource.NOUN)+"");
        playersPoints[numberOrder].setText("pt:"+myScore);
    }


}


