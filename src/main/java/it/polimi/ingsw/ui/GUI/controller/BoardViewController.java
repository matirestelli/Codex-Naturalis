package it.polimi.ingsw.ui.GUI.controller;

import it.polimi.ingsw.ui.GUI.GUI;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
    private Button buttonDeckGBack, buttonSide;
    @FXML
    private Button buttonCard1, buttonCard2, buttonCard3;

    public String cardChose;


    public void initialize() {
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

        this.positionCard(0, "f", new Integer[]{40, 40});
        this.drawHand(new ArrayList<>());
        this.updateDeck(0, "r", 0);
    }


    public void setPlayersRecapVbox(VBox playersRecapVbox){
        //TODO : poi il for sarà fino a quanto numero max di giocatori della partita
        for(int i=0; i<4; i++){
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
            String text = "Player: " + i;
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
            if(i==1 && test==false){
                containerVBoxRight.getChildren().add(playersRow1[i]);
                containerVBoxRight.getChildren().add(playersRow2[i]);
                containerVBoxRight.getChildren().add(playersRow3[i]);
                test=true;
            }

            else if(i!=1){
                playersVBoxesRecap[i].getChildren().add(playersRow1[i]);
                playersVBoxesRecap[i].getChildren().add(playersRow2[i]);
                playersVBoxesRecap[i].getChildren().add(playersRow3[i]);
                playersRecapVbox.getChildren().add(playersVBoxesRecap[i]);
            }


        }
    }

    //dato l'id della carta,front e back e la posizione, la stampo nella board
    public void positionCard(Integer idCard, String side, Integer[] position){
        //per quando mi passeranno solo id: "/it/polimi/ingsw/gc38/images/developmentCards/"+cardName+".png
        //dovrò fare un case switch se initial, risorsa o gold per il path dell'immagine

        //le prossime righe sono test per vedere se l'immagine viene stampata con le sovrapposizioni giuste
        ImageView image = new ImageView("it/polimi/ingsw/images/front/starter/0.png");
        image.setFitHeight(70);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, 40, 40);

        image = new ImageView("it/polimi/ingsw/images/front/resource/red/0.png");
        image.setFitHeight(70);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, 39, 39);

        image = new ImageView("it/polimi/ingsw/images/front/resource/red/3.png");
        image.setFitHeight(70);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, 39, 41);

        image = new ImageView("it/polimi/ingsw/images/front/resource/red/5.png");
        image.setFitHeight(70);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, 41, 39);

        image = new ImageView("it/polimi/ingsw/images/front/resource/red/9.png");
        image.setFitHeight(70);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, 41, 41);

        image = new ImageView("it/polimi/ingsw/images/front/resource/red/3.png");
        image.setFitHeight(70);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, 38, 40);

        image = new ImageView("it/polimi/ingsw/images/front/resource/red/3.png");
        image.setFitHeight(70);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, 42, 40);

        image = new ImageView("it/polimi/ingsw/images/front/resource/red/3.png");
        image.setFitHeight(70);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, 40, 38);

        image = new ImageView("it/polimi/ingsw/images/front/resource/red/3.png");
        image.setFitHeight(70);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, 38, 42);

        image = new ImageView("it/polimi/ingsw/images/front/resource/red/3.png");
        image.setFitHeight(70);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, 40, 42);

        image = new ImageView("it/polimi/ingsw/images/front/resource/red/3.png");
        image.setFitHeight(70);
        image.setFitWidth(120);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(image, 42, 42);

        scrollPane.setHvalue(0.5);
        scrollPane.setVvalue(0.5);

    }

    //metodo che disegna la mano di carte del giocatore del turno
    //nb: le carte sono poi dei bottoni che chiamano il metodo per giocare la carta
    public void drawHand(List<Integer> hand){
        ImageView imageCard = new ImageView();
        imageCard.setFitHeight(70);
        imageCard.setFitWidth(120);
        /*
        for(int i=1; i<=hand.size(); i++){
            //algoritmo che dall'id della carta mi dice se gold/resource/starter e di che colore
            //poi setto l'immagine in base ai parametri restituiti dall'algoritmo
            imageCard = new ImageView("it/polimi/ingsw/gc38/images/front/gold/blue/5.png");
            imageCard.setFitHeight(70);
            imageCard.setFitWidth(120);
            if(i==1){
                card1ImageView.setImage(imageCard.getImage());
            }
            else if(i==2){
                card2ImageView.setImage(imageCard.getImage());
            }
            else if(i==3){
                card3ImageView.setImage(imageCard.getImage());
            }
            */
            imageCard = new ImageView("it/polimi/ingsw/images/front/gold/blue/5.png");
            card1ImageView.setImage(imageCard.getImage());
            imageCard = new ImageView("it/polimi/ingsw/images/front/resource/green/1.png");
            card2ImageView.setImage(imageCard.getImage());
            imageCard = new ImageView("it/polimi/ingsw/images/front/resource/red/2.png");
            card3ImageView.setImage(imageCard.getImage());

    }

    //quando chiamo questo metodo modifico la carta del mazzo (resource r oppure gold g)
    //in posizione: 0 -> girata (il back), 1 -> esposta 1, 2 -> esposta 2)
    public void updateDeck (Integer idCardRemoved, String type, Integer position){
        //per ora solo test:
        ImageView imageCard = new ImageView();
        imageCard.setFitHeight(70);
        imageCard.setFitWidth(120);
        imageCard = new ImageView("it/polimi/ingsw/images/back/gold/blue.png");
        deckGBackImageView.setImage(imageCard.getImage());
        deckGBackEco1.setImage(imageCard.getImage());
        deckGBackEco2.setImage(imageCard.getImage());

        imageCard = new ImageView("it/polimi/ingsw/images/back/resource/purple.png");
        deckRBackImageView.setImage(imageCard.getImage());
        deckRBackEco1.setImage(imageCard.getImage());
        deckRBackEco2.setImage(imageCard.getImage());


        imageCard = new ImageView("it/polimi/ingsw/images/front/resource/green/1.png");
        deckRFront1ImageView.setImage(imageCard.getImage());
        imageCard = new ImageView("it/polimi/ingsw/images/front/resource/red/2.png");
        deckRFront2ImageView.setImage(imageCard.getImage());
        imageCard = new ImageView("it/polimi/ingsw/images/front/gold/blue/1.png");
        deckGFront1ImageView.setImage(imageCard.getImage());
        imageCard = new ImageView("it/polimi/ingsw/images/front/gold/blue/2.png");
        deckGFront2ImageView.setImage(imageCard.getImage());

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
            //todo cambiare le immagini delle carte con quelle del back
            buttonSide.setText("view front side");
        }
        else{
            //todo cambiare le immagini delle carte con quelle del front
            buttonSide.setText("view back side");
        }
    }

    public void viewScoreboard(ActionEvent actionEvent) {
        Stage popUpStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        this.showScoreboardPopUp(popUpStage);
    }

    public void playCard(ActionEvent actionEvent) {
        Button buttonPressed = (Button) actionEvent.getTarget();
        buttonPressed.setStyle("-fx-border-color: #52e51f;\n" +
                "    -fx-effect: dropshadow(one-pass-box,  #338f13, 20, 0.8, 0, 0);");

    }

    public void setTurn(String messageTurn){

    }

    public void visualizeChat(ActionEvent actionEvent) throws IOException {
        Stage popUpStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        this.viewChat(popUpStage);
    }

}
