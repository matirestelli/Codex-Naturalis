package it.polimi.ingsw.gc38.controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.gc38.model.*;
import it.polimi.ingsw.gc38.view.CliView;

public class GameController {
    private final Player player;
    private final CliView view;
    private final Game game;

    private final int cardWidth;
    private final int cardHeight;
    private final int matrixDimension;

    public GameController(Player player, CliView view, Game game) {
        this.player = player;
        this.view = view;
        this.game = game;

        this.matrixDimension = 10;
        this.cardWidth = 7;
        this.cardHeight = 3;

        this.player.setMatrix(new int[matrixDimension][matrixDimension]);
        this.player.setBoard(new Cell[matrixDimension * cardHeight][matrixDimension * cardWidth]);
    }

    public void initPlayerNickname() {
        String nickname = view.askForNickname();
        player.setNickname(nickname);
    }

    public void startGame() {
        // initialize board and matrix
        player.initializeBoard(this.matrixDimension, this.cardWidth, this.cardHeight);
        player.initializeMatrix(this.matrixDimension);
        // initialize playing hand and codex
        player.initializeCardsList();

        // load from json the resource and starter cards
        game.initializeDecks();
        // shuffle the decks
        game.shuffleDecks();

        // ask for player nickname
        // initPlayerNickname();

        // welcome message
        view.welcomeMessage();

        // extract one card from starter cards
        Card extractedStarterCard = game.getStarterDeck().extractCard();
        // visualize extracted card from starter cards
        // view.displayResourceCard((ColoredCard) extractedStarterCard);


        // TODO: delete: testing only
        view.displayResourceCard((ColoredCard) extractedStarterCard);
        view.displayResourceCard((ColoredCard) game.getResourceDeck().extractCard());
        view.displayResourceCard((ColoredCard) game.getGoldDeck().extractCard());

        //view.displayStarterCardBack(extractedStarterCard);
        extractedStarterCard.setSide(view.askForSide());
        // add the extracted card to the codex
        player.addCardToCodex(extractedStarterCard);

        // extract two cards from resource cards and add to the playing hand
        player.addCardToPlayingHand(game.getResourceDeck().extractCard());
        player.addCardToPlayingHand(game.getResourceDeck().extractCard());
        // player.addCardToPlayingHand(game.getGoldDeck().extractCard());

        // define the leftUpCorner of the card
        Coordinate leftUpCorner = new Coordinate(matrixDimension / 2 * cardWidth - 5,matrixDimension / 2 * cardHeight - 5);
        extractedStarterCard.setCentre(leftUpCorner);
        // set the x and y matrix coordinates of the card
        extractedStarterCard.setXYCord(matrixDimension / 2, matrixDimension / 2);
        player.getMatrix()[extractedStarterCard.getyMatrixCord()][extractedStarterCard.getxMatrixCord()] = extractedStarterCard.getId();
        view.placeCard(player.getBoard(), (ResourceCard) extractedStarterCard, leftUpCorner);

        // visualize the board
        view.displayBoard(player.getBoard());

        while (true) {
            // create list of playing hand ids
            List<Integer> playingHandIds = new ArrayList<Integer>();
            for (Card c : player.getPlayingHand()) {
                playingHandIds.add(c.getId());
            }

            // visualize playing hand
            view.displayMessage("Visualizing playing hand: ");
            for (Card card : player.getPlayingHand()) {
                view.displayResourceCard((ResourceCard) card);
                view.displayResourceCardBack((ResourceCard) card);
            }

            // ask for which card to play
            CardCornerInput cardToPlayInput = view.askForCardToPlay(playingHandIds);
            // get card, from the resourceCards list, in the playing hand that has the id selected
            // no exception handling needed because the id is always in the list
            Card cardToPlay = player.getPlayingHand().stream()
                    .filter(card -> card.getId() == cardToPlayInput.getId())
                    .findAny()
                    .get();
            cardToPlay.setSide(cardToPlayInput.isFrontSide());

            List<Coordinate> angoliDisponibili = new ArrayList<>();
            Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

            for (Card c : player.getCodex()) {
                ResourceCard card = (ResourceCard) c;
                angoliDisponibili.addAll(card.trovaCarteVicine(player.getMatrix(), player.getCodex(), cardToPlay.getId(), test));
            }

            player.addCardToCodex(cardToPlay);
            player.removeCardFromPlayingHand(cardToPlay);

            String cardToAttachSelected = view.displayAngle(angoliDisponibili);
            String[] splitCardToPlay = cardToAttachSelected.split("\\.");
            int cardToAttachId = Integer.parseInt(splitCardToPlay[0]);
            int cornerSelected = Integer.parseInt(splitCardToPlay[1]);
            ResourceCard targetCard = (ResourceCard) player.getCodex().stream()
                    .filter(card -> card.getId() == cardToAttachId)
                    .findAny()
                    .get();

            if (test.containsKey(cardToAttachId)) {
                if (test.get(cardToAttachId).containsKey(cornerSelected)) {
                    List<Coordinate> co = test.get(cardToAttachId).get(cornerSelected);

                    for (Coordinate c : co) {
                        if (c.getX() == cardToPlay.getId() && cardToPlay.getActualCorners().containsKey(c.getY())) {
                            cardToPlay.getActualCorners().get(c.getY()).setHidden(true);
                        } else {
                            Card cardTemp = player.getCodex().stream()
                                    .filter(card -> card.getId() == c.getX())
                                    .findAny()
                                    .get();
                            if (cardTemp.getActualCorners().containsKey(c.getY())) {
                                cardTemp.getActualCorners().get(c.getY()).setHidden(true);
                                cardTemp.getActualCorners().get(c.getY()).setEmpty(true);
                            }
                        }
                    }
                }
            }

            if (cornerSelected == 0) {
                view.placeCard(player.getBoard(), (ResourceCard) cardToPlay,
                        placeCardBottomLeft(player.getBoard(), targetCard, (ResourceCard) cardToPlay));
                cardToPlay.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() - 1);
            } else if (cornerSelected == 1) {
                view.placeCard(player.getBoard(), (ResourceCard) cardToPlay,
                        placeCardTopLeft(player.getBoard(), targetCard, (ResourceCard) cardToPlay));
                cardToPlay.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() - 1);
            } else if (cornerSelected == 2) {
                view.placeCard(player.getBoard(), (ResourceCard) cardToPlay,
                        placeCardTopRight(player.getBoard(), targetCard, (ResourceCard) cardToPlay));
                cardToPlay.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() + 1);
            } else if (cornerSelected == 3) {
                cardToPlay.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() + 1);
                view.placeCard(player.getBoard(), (ResourceCard) cardToPlay,
                        placeCardBottomRight(player.getBoard(), targetCard, (ResourceCard) cardToPlay));
            }

            player.getMatrix()[cardToPlay.getyMatrixCord()][cardToPlay.getxMatrixCord()] = cardToPlay.getId();
            view.displayBoard(player.getBoard());

            player.addCardToPlayingHand(game.getResourceDeck().extractCard());

            player.calculateResources();

            view.displayPersonalResources(player.getPersonalResources());
        }
    }

    public Coordinate placeCardBottomRight(Cell[][] matrixBoard, ResourceCard card, ResourceCard cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardTopRight(Cell[][] matrixBoard, ResourceCard card, ResourceCard cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardBottomLeft(Cell[][] matrixBoard, ResourceCard card, ResourceCard cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardTopLeft(Cell[][] matrixBoard, ResourceCard card, ResourceCard cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }
}
