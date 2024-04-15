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

        // extract 2 resource cards and put them in the visible resource cards
        int numberOfVisibleCards = 2;
        for (int i = 0; i < numberOfVisibleCards; i++) {
            game.addCardToResourceCardsVisible((Card) game.getResourceDeck().extractCard());
            game.addCardToGoldCardsVisible((Card) game.getGoldDeck().extractCard());
        }

        // display all objective cards
        /* for (CardGame c : game.getObjectiveDeck().getCards()) {
            if (c instanceof Objective x) {
                System.out.println(x.getId());
                x.displayCard(view);
                System.out.println();
            }
        } */

        // ask for player nickname
        // initPlayerNickname();

        // welcome message
        view.welcomeMessage();

        // extract one card from starter cards
        CardGame extractedStarterCard = game.getStarterDeck().extractCard();
        // visualize extracted card from starter cards
        view.displayCard((Card) extractedStarterCard);
        view.displayStarterCardBack((ResourceCard) extractedStarterCard);
        // ask for side of the starter card
        extractedStarterCard.setSide(view.askForSide());
        // add the extracted card to the codex
        player.addCardToCodex((Card) extractedStarterCard);

        // TODO: delete: testing only
        // view.displayCard((ColoredCard) extractedStarterCard);
        // view.displayCard((ColoredCard) game.getResourceDeck().extractCard());
        // view.displayCard((ColoredCard) game.getGoldDeck().extractCard());

        // testing: visualizing all gold cards
        /* for (Card c : game.getGoldDeck().getCards()) {
            view.displayCard((ColoredCard) c);
            view.displayCardBack((ColoredCard) c);
        } */

        // extract the first two cards from resource cards deck and one from gold cards deck
        CardGame extractedResourceCard1 = game.getResourceDeck().extractCard();
        CardGame extractedResourceCard2 = game.getResourceDeck().extractCard();
        CardGame extractedGoldCard = game.getGoldDeck().extractCard();

        // add extracted cars to playing hand
        player.addCardToPlayingHand((Card) extractedResourceCard1);
        player.addCardToPlayingHand((Card) extractedResourceCard2);
        player.addCardToPlayingHand((Card) extractedGoldCard);

        // extract the first two cards from objective cards deck
        game.addCommonObjective((Objective) game.getObjectiveDeck().extractCard());
        game.addCommonObjective((Objective) game.getObjectiveDeck().extractCard());

        // display the extracted cards
        view.displayMessage("\nCommon objective cards: ");
        for (Objective o : game.getCommonObjectives()) {
            o.displayCard(view);
            view.displayMessage("");
        }

        List<Objective> objToChoose = new ArrayList<>();
        int numberOfObjectives = 2;
        view.displayMessage("Choose one of the following objectives: ");
        for (int i = 0; i < numberOfObjectives; i++) {
            Objective o = (Objective) game.getObjectiveDeck().extractCard();
            objToChoose.add(o);
            view.displayMessage(i + ":");
            o.displayCard(view);
            view.displayMessage("");
        }

        // define the secret objective of the player
        player.setSecretObjective(objToChoose.get(view.askForObjectiveId(numberOfObjectives)));

        // define the leftUpCorner of the card
        Coordinate leftUpCorner = new Coordinate(matrixDimension / 2 * cardWidth - 5,matrixDimension / 2 * cardHeight - 5);
        ((Card) extractedStarterCard).setCentre(leftUpCorner);
        // set the x and y matrix coordinates of the card
        ((Card) extractedStarterCard).setXYCord(matrixDimension / 2, matrixDimension / 2);
        player.getMatrix()[((Card) extractedStarterCard).getyMatrixCord()][((Card) extractedStarterCard).getxMatrixCord()] = extractedStarterCard.getId();
        view.placeCard(player.getBoard(), (ResourceCard) extractedStarterCard, leftUpCorner);

        // visualize the board
        view.displayBoard(player.getBoard());

        boolean playable;

        while (true) {
            player.calculateResources();
            
            // create list of playing hand ids
            List<Integer> playingHandIds = new ArrayList<>();
            List<Integer> playingHandIdsBack = new ArrayList<>();
            
            // visualize playing hand
            view.displayMessage("Visualizing playing hand: ");
            for (Card c : player.getPlayingHand()) {
                playable = true;
                if (c instanceof GoldCard x) {
                    for (Requirement r : x.getRequirements()) {
                        if (player.getPersonalResources().get(r.getResource()) < r.getQta()) {
                            playable = false;
                        }
                    }
                }

                if (playable) {
                    view.displayCard(c);
                    playingHandIds.add(c.getId());
                } else
                    playingHandIdsBack.add(c.getId());
                
                view.displayCardBack(c);
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

            for (Card c : player.getCodex())
                angoliDisponibili.addAll(c.trovaCarteVicine(player.getMatrix(), player.getCodex(), cardToPlay.getId(), test));

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
                view.placeCard(player.getBoard(), cardToPlay,
                        placeCardBottomLeft(player.getBoard(), targetCard, cardToPlay));
                cardToPlay.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() - 1);
            } else if (cornerSelected == 1) {
                view.placeCard(player.getBoard(), cardToPlay,
                        placeCardTopLeft(player.getBoard(), targetCard, cardToPlay));
                cardToPlay.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() - 1);
            } else if (cornerSelected == 2) {
                view.placeCard(player.getBoard(), cardToPlay,
                        placeCardTopRight(player.getBoard(), targetCard, cardToPlay));
                cardToPlay.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() + 1);
            } else if (cornerSelected == 3) {
                cardToPlay.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() + 1);
                view.placeCard(player.getBoard(), cardToPlay,
                        placeCardBottomRight(player.getBoard(), targetCard, cardToPlay));
            }

            player.getMatrix()[cardToPlay.getyMatrixCord()][cardToPlay.getxMatrixCord()] = cardToPlay.getId();

            view.displayBoard(player.getBoard());

            List<Integer> ids = new ArrayList<>();
            for (Card c : game.getResourceCardsVisible()) {
                ids.add(c.getId());
                view.displayCard(c);
            }
            for (Card c : game.getGoldCardsVisible()) {
                ids.add(c.getId());
                view.displayCard(c);
            }
            String newId = view.chooseDrawNewCard(ids);

            if (newId.equals("A")) {
                player.addCardToPlayingHand((Card) game.getResourceDeck().extractCard());
            } else if (newId.equals("B")) {
                player.addCardToPlayingHand((Card) game.getGoldDeck().extractCard());
            } else {
                int id = Integer.parseInt(newId);
                Card newCard;
                if (id < 40) {
                    newCard = game.getResourceCardsVisible().stream()
                            .filter(card -> card.getId() == Integer.parseInt(newId))
                            .findAny()
                            .get();
                    game.getResourceCardsVisible().remove(newCard);
                    game.getResourceCardsVisible().add((Card) game.getResourceDeck().extractCard());
                } else {
                    newCard = game.getGoldCardsVisible().stream()
                            .filter(card -> card.getId() == Integer.parseInt(newId))
                            .findAny()
                            .get();
                    player.addCardToPlayingHand(newCard);
                    game.getGoldCardsVisible().remove(newCard);
                    game.getGoldCardsVisible().add((Card) game.getGoldDeck().extractCard());
                }
                player.addCardToPlayingHand(newCard);
            }

            if (cardToPlay instanceof ResourceCard x) {
                player.addScore(x.getPoint());
                // player.addCardToPlayingHand((Card) game.getResourceDeck().extractCard());
            }
            else {
                Map<Resource, Integer> res = player.calculateResources();
                var x = (GoldCard) cardToPlay;
                // player.addCardToPlayingHand((Card) game.getGoldDeck().extractCard());
                if (x.isFrontSide()) {
                    player.addScore(res.get(x.getPoint().getResource()) * x.getPoint().getQta());
                }
            }

            player.calculateResources();

            view.displayPersonalResources(player.getPersonalResources());

            /* DxDiagonalObjective x = new DxDiagonalObjective();
            x.setColor(Color.BLUE);
            x.setPoints(3); */

            DownLObjective x = new DownLObjective();
            x.setColor1(Color.BLUE);
            x.setColor2(Color.GREEN);
            x.setPoints(3);

            x.CalculatePoints(player);

            System.out.println("Points: " + player.getScore());
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
