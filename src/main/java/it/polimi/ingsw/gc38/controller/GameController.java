package it.polimi.ingsw.gc38.controller;

import java.util.*;

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
        view.displayResourceCard((ColoredCard) extractedStarterCard);
        view.displayStarterCardBack((ResourceCard) extractedStarterCard);
        // ask for side of the starter card
        extractedStarterCard.setSide(view.askForSide());
        // add the extracted card to the codex
        player.addCardToCodex(extractedStarterCard);

        // TODO: delete: testing only
        // view.displayResourceCard((ColoredCard) extractedStarterCard);
        // view.displayResourceCard((ColoredCard) game.getResourceDeck().extractCard());
        // view.displayResourceCard((ColoredCard) game.getGoldDeck().extractCard());

        // testting: visualizing all gold cards
        /* for (Card c : game.getGoldDeck().getCards()) {
            view.displayResourceCard((ColoredCard) c);
            view.displayResourceCardBack((ColoredCard) c);
        } */

        // extract the first two cards from resource cards and one from gold cards
        Card extractedResourceCard1 = game.getResourceDeck().extractCard();
        Card extractedResourceCard2 = game.getResourceDeck().extractCard();
        Card extractedGoldCard = game.getGoldDeck().extractCard();
        // add extracted cars to playing hand
        player.addCardToPlayingHand(extractedResourceCard1);
        player.addCardToPlayingHand(extractedResourceCard2);
        player.addCardToPlayingHand(extractedGoldCard);
        // remove extracted cards from the decks, the cards are not used anymore
        game.getResourceDeck().removeCard(extractedResourceCard1);
        game.getResourceDeck().removeCard(extractedResourceCard2);
        game.getGoldDeck().removeCard(extractedGoldCard);

        // define the leftUpCorner of the card
        Coordinate leftUpCorner = new Coordinate(matrixDimension / 2 * cardWidth - 5,matrixDimension / 2 * cardHeight - 5);
        extractedStarterCard.setCentre(leftUpCorner);
        // set the x and y matrix coordinates of the card
        extractedStarterCard.setXYCord(matrixDimension / 2, matrixDimension / 2);
        player.getMatrix()[extractedStarterCard.getyMatrixCord()][extractedStarterCard.getxMatrixCord()] = extractedStarterCard.getId();
        view.placeCard(player.getBoard(), (ResourceCard) extractedStarterCard, leftUpCorner);

        // visualize the board
        view.displayBoard(player.getBoard());

        boolean playable;

        while (true) {
            // create list of playing hand ids
            List<Integer> playingHandIds = new ArrayList<Integer>();
            List<Integer> playingHandIdsBack = new ArrayList<Integer>();
            player.calculateResources();
            // visualize playing hand
            view.displayMessage("Visualizing playing hand: ");
            for (Card c : player.getPlayingHand()) {
                playable = true;
                if (c instanceof GoldCard) {
                    GoldCard x = (GoldCard) c;
                    for (Requirement r : x.getRequirements()) {
                        if (player.getPersonalResources().get(r.getResource()) < r.getQta()) {
                            playable = false;
                        }
                    }
                }

                if (playable) {
                    view.displayResourceCard((ColoredCard) c);
                    playingHandIds.add(c.getId());
                } else {
                    playingHandIdsBack.add(c.getId());
                }
                view.displayResourceCardBack((ColoredCard) c);
            }

            // ask for which card to play
            CardCornerInput cardToPlayInput = view.askForCardToPlay(playingHandIds, playingHandIdsBack);
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
                angoliDisponibili.addAll(c.trovaCarteVicine(player.getMatrix(), player.getCodex(), cardToPlay.getId(), test));
            }

            player.addCardToCodex(cardToPlay);
            player.removeCardFromPlayingHand(cardToPlay);

            String cardToAttachSelected = view.displayAngle(angoliDisponibili);
            String[] splitCardToPlay = cardToAttachSelected.split("\\.");
            int cardToAttachId = Integer.parseInt(splitCardToPlay[0]);
            int cornerSelected = Integer.parseInt(splitCardToPlay[1]);
            Card targetCard = player.getCodex().stream()
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
                view.placeCard(player.getBoard(), (ColoredCard) cardToPlay,
                        placeCardBottomLeft(player.getBoard(), targetCard, cardToPlay));
                cardToPlay.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() - 1);
            } else if (cornerSelected == 1) {
                view.placeCard(player.getBoard(), (ColoredCard) cardToPlay,
                        placeCardTopLeft(player.getBoard(), targetCard, cardToPlay));
                cardToPlay.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() - 1);
            } else if (cornerSelected == 2) {
                view.placeCard(player.getBoard(), (ColoredCard) cardToPlay,
                        placeCardTopRight(player.getBoard(), targetCard, cardToPlay));
                cardToPlay.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() + 1);
            } else if (cornerSelected == 3) {
                cardToPlay.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() + 1);
                view.placeCard(player.getBoard(), (ColoredCard) cardToPlay,
                        placeCardBottomRight(player.getBoard(), targetCard, cardToPlay));
            }

            player.getMatrix()[cardToPlay.getyMatrixCord()][cardToPlay.getxMatrixCord()] = cardToPlay.getId();

            view.displayBoard(player.getBoard());

            if (cardToPlay instanceof ResourceCard) {
                game.getResourceDeck().removeCard(cardToPlay);
                Card x = game.getResourceDeck().extractCard();
                player.addCardToPlayingHand(x);
            } else {
                game.getGoldDeck().removeCard(cardToPlay);
                Card x = game.getGoldDeck().extractCard();
                player.addCardToPlayingHand(x);
            }

            player.calculateResources();

            view.displayPersonalResources(player.getPersonalResources());
        }
    }

    public Coordinate placeCardBottomRight(Cell[][] matrixBoard, Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardTopRight(Cell[][] matrixBoard, Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardBottomLeft(Cell[][] matrixBoard, Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardTopLeft(Cell[][] matrixBoard, Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }
}
