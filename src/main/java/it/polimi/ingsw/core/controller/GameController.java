package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.clientmodel.Cell;
import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.observers.GameObserver;
import it.polimi.ingsw.view.CliView;
import it.polimi.ingsw.core.model.enums.*;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class GameController implements Serializable {
    private GameState gameState;
    private int cardWidth;
    private int quorum;
    private int cardHeight;
    private transient List<GameObserver> observers;
    private int currentPlayerIndex;
    private int matrixDimension;

    public GameController(GameState gameState) {
        this.gameState = gameState;
        this.observers = new ArrayList<>();
        this.currentPlayerIndex = 0;

        this.matrixDimension = 10;
        this.cardWidth = 7;
        this.cardHeight = 3;

    }

    public void startGame() throws RemoteException {
        System.out.println("Game started [from GameController]");
        quorum=0;
        gameState.initializeBoard(this.matrixDimension, this.cardWidth, this.cardHeight);
        gameState.initializeMatrix(this.matrixDimension);
        // initialize playing hand and codex
        gameState.loadDecks();
        gameState.shuffleDecks();
        gameState.assignStarterCardToPlayers();
        for (GameObserver observer : observers) {
            observer.update(new GameEvent("loadedStarterDeck", gameState.getStarterDeck()));
        }
        gameState.assignFirstHandToPlayers();
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update(new GameEvent("updateHand", gameState.getPlayerState(i).getHand()));
        }
        gameState.addCommonObjective((Objective)gameState.getObjectiveDeck().drawCard());
        gameState.addCommonObjective((Objective)gameState.getObjectiveDeck().drawCard());
        for(int i = 0; i < observers.size(); i++) {
            List<CardGame> secretChoose = new ArrayList();
            secretChoose.add(gameState.getObjectiveDeck().drawCard());
            secretChoose.add(gameState.getObjectiveDeck().drawCard());
            //observers.get(i).update(new GameEvent("printObjective", secretChoose));
            observers.get(i).update(new GameEvent("chooseObjective", secretChoose));
        }
    }

    public void chooseObjective(String username, SecreteObjectiveCard card) throws RemoteException {
        int playerId = gameState.getPlayerId(username);
        PlayerState player = gameState.getPlayerState(playerId);
        List<CardGame> deck = gameState.getObjectiveDeckCopy();
        for (CardGame obj : deck) {
            if (obj.getId() == card.getId()) {
                player.setSecretObj((Objective) obj);
                System.out.println("inserito secret: " + obj);
                quorum++;
                break;
            }
        }
        if(quorum == observers.size()) {
            quorum = 0;
            showObjectives();
        } else if(quorum == 0){
            throw new IllegalArgumentException("Invalid card id");
        }
    }

    public void showObjectives() throws RemoteException {
        Map<Integer, List<CardGame>> totalObjective = new HashMap<>();
        for(int i=0; i<observers.size(); i++){
            List<CardGame> objectives= new ArrayList<>();
            objectives.add(gameState.getPlayerState(i).getSecretObj());
            System.out.println("Stampa: " + gameState.getPlayerState(i).getSecretObj());
            objectives.add(gameState.getCommonObjective(0));
            objectives.add(gameState.getCommonObjective(1));
            totalObjective.put(i, objectives);
            observers.get(i).update(new GameEvent("loadedObjective", objectives));
        }

        // ask front or back for starter card
        //gameState.placeStarter(isFront);
        /*
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update(new GameEvent("assignedStarterCard", gameState.getPlayerState(i).getStarterCard()));
        }
        */
        notifyCurrentPlayerTurn();
    }

    public void playerSelectsCard(String username, CardSelection cardSelection) throws RemoteException {
        int playerId = gameState.getPlayerId(username);
        System.out.println("Stampando: " + playerId + " | " + currentPlayerIndex);
        // adding exception notyourturn
        if (playerId == currentPlayerIndex) {
            int cardId = cardSelection.getId();
            // get card from player hand by id
            Card card = gameState.getPlayerState(playerId).getCardFromHand(cardId);

            PlayerState player = gameState.getPlayerState(playerId);

            List<Coordinate> angoliDisponibili = new ArrayList<>();
            Map<Integer, Map<Integer, List<Coordinate>>> test = new HashMap<>();

            for (Card c : player.getCodex()) {
                angoliDisponibili.addAll(c.findFreeAngles(gameState.getPlayerState(playerId).getMatrix(), player.getCodex(), card.getId(), test));
            }

            gameState.getPlayerState(playerId).removeCardFromHand(card);
            // add card to player codex
            gameState.getPlayerState(playerId).addCardToCodex(card);

            String cardToAttachSelected = null;//view.displayAngle(angoliDisponibili);
            String[] splitCardToPlay = cardToAttachSelected.split("\\.");
            int cardToAttachId = Integer.parseInt(splitCardToPlay[0]);
            int cornerSelected = Integer.parseInt(splitCardToPlay[1]);
            Card targetCard = player.getCodex().stream()
                    .filter(card1 -> card1.getId() == cardToAttachId)
                    .findAny()
                    .get();

            if (test.containsKey(cardToAttachId)) {
                if (test.get(cardToAttachId).containsKey(cornerSelected)) {
                    List<Coordinate> co = test.get(cardToAttachId).get(cornerSelected);

                    for (Coordinate c : co) {
                        if (c.getX() == card.getId() && card.getActualCorners().containsKey(c.getY())) {
                            card.getActualCorners().get(c.getY()).setHidden(true);
                        } else {
                            Card cardTemp = player.getCodex().stream()
                                    .filter(card1 -> card1.getId() == c.getX())
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
                //view.placeCard(player.getBoard(), card,
                     //   placeCardBottomLeft(player.getBoard(), targetCard, card));
                card.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() - 1);
            } else if (cornerSelected == 1) {
                //view.placeCard(player.getBoard(), card,
                      //  placeCardTopLeft(player.getBoard(), targetCard, card));
                card.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() - 1);
            } else if (cornerSelected == 2) {
                //view.placeCard(player.getBoard(), card,
                       // placeCardTopRight(player.getBoard(), targetCard, card));
                card.setXYCord(targetCard.getyMatrixCord() - 1, targetCard.getxMatrixCord() + 1);
            } else if (cornerSelected == 3) {
                card.setXYCord(targetCard.getyMatrixCord() + 1, targetCard.getxMatrixCord() + 1);
                //view.placeCard(player.getBoard(), card,
                      //  placeCardBottomRight(player.getBoard(), targetCard, card));
            }

            player.getMatrix()[card.getyMatrixCord()][card.getxMatrixCord()] = card.getId();

            //view.displayBoard(player.getBoard());

            List<Integer> ids = new ArrayList<>();
            for (Card c : gameState.getResourceCardsVisible()) {
                ids.add(c.getId());
                //view.displayCard(c);
            }
            for (Card c : gameState.getGoldCardsVisible()) {
                ids.add(c.getId());
                //view.displayCard(c);
            }
            String newId = null;//view.chooseDrawNewCard(ids);

            if (newId.equals("A")) {
                player.addCardToHand((Card) gameState.getResourceDeck().drawCard());
            } else if (newId.equals("B")) {
                player.addCardToHand((Card) gameState.getGoldDeck().drawCard());
            } else {
                int id = Integer.parseInt(newId);
                Card newCard;
                if (id < 40) {
                    newCard = gameState.getResourceCardsVisible().stream()
                            .filter(card1 -> card1.getId() == Integer.parseInt(newId))
                            .findAny()
                            .get();
                    gameState.getResourceCardsVisible().remove(newCard);
                    gameState.getResourceCardsVisible().add((Card) gameState.getResourceDeck().drawCard());
                } else {
                    newCard = gameState.getGoldCardsVisible().stream()
                            .filter(card1 -> card1.getId() == Integer.parseInt(newId))
                            .findAny()
                            .get();
                    player.addCardToHand(newCard);
                    gameState.getGoldCardsVisible().remove(newCard);
                    gameState.getGoldCardsVisible().add((Card) gameState.getGoldDeck().drawCard());
                }
                player.addCardToHand(newCard);
            }

            if (card instanceof ResourceCard x) {
                player.addScore(x.getPoint());
                // player.addCardToPlayingHand((Card) game.getResourceDeck().extractCard());
            }
            else {
                Map<Resource, Integer> res = player.calculateResources();
                var x = (GoldCard) card;
                // player.addCardToPlayingHand((Card) game.getGoldDeck().extractCard());
                if (x.isFrontSide()) {
                    player.addScore(res.get(x.getPoint().getResource()) * x.getPoint().getQta());
                }
            }

            player.calculateResources();

            //view.displayPersonalResources(player.getPersonalResources());

            // notify player of updated hand
            observers.get(playerId).update(new GameEvent("updateHand", new ArrayList<>(gameState.getPlayerState(playerId).getHand())));
            // notify player of updated codex
            // observers.get(playerId).update(new GameEvent("updateCodex", new ArrayList<>(gameState.getPlayerState(playerId).getCodex())));

           // check if last turn
            lastTurn();
            // advance turn
            // advanceTurn();
        } else {
            observers.get(playerId).update(new GameEvent("Error", "Not your turn"));
        }
    }

    public List<GameObserver> getObservers() {
        return observers;
    }

    public void notifyCurrentPlayerTurn() throws RemoteException {
        GameEvent turnEvent = new GameEvent("currentPlayerTurn", gameState.getPlayerState(currentPlayerIndex).getHand());
        observers.get(currentPlayerIndex).update(turnEvent);
    }

    public void advanceTurn() throws RemoteException {
        currentPlayerIndex = (currentPlayerIndex + 1) % observers.size();
        notifyObservers(new GameEvent("updateTurnCounter", currentPlayerIndex));
        notifyCurrentPlayerTurn();
    }

    public void addObserver(GameObserver observer) {
        System.out.println("\nAdding observer " + observer);
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    public void notifyObservers(GameEvent event) throws RemoteException {
        System.out.println("Notifying observers: " + observers.size());
        for (GameObserver observer : observers) {
            observer.update(event);
        }
    }




    public Coordinate placeCardBottomRight(Cell[][] board, Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardTopRight(Cell[][] board, Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardBottomLeft(Cell[][] board, Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeCardTopLeft(Cell[][] board, Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }



    public void setCurrTurn(int index) {
        currentPlayerIndex = index;
    }

    public void lastTurn() throws RemoteException{
        Boolean last = false;
        if(gameState.getPlayerState(currentPlayerIndex).getScore() >= 20 || last==true) {
            last = true;
            if(currentPlayerIndex == observers.size()-1 ) {
                //calculate points
                List<Integer> rank = null;
                Map<Integer, Integer> scores= null;
                for(int i = 0; i < observers.size(); i++) {
                    PlayerState player = gameState.getPlayerState(currentPlayerIndex);
                    int preScore= player.getScore();
                    Objective card= null;
                    for(int j= 0 ; j<3; j++) {
                        if(j==0) {card= player.getSecretObj();}
                        if(j==1){card= gameState.getCommonObjective(0);}
                        if(j==2){card= gameState.getCommonObjective(1);}
                        if (card instanceof SxDiagonalObjective) {
                            SxDiagonalObjective c = (SxDiagonalObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof DxDiagonalObjective) {
                            DxDiagonalObjective c = (DxDiagonalObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof LObjective) {
                            LObjective c = (LObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof ReverseLObjective) {
                            ReverseLObjective c = (ReverseLObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof DownLObjective) {
                            DownLObjective c = (DownLObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof DownReverseLObjective) {
                            DownReverseLObjective c = (DownReverseLObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else if (card instanceof ResourceObjective) {
                            ResourceObjective c = (ResourceObjective) player.getSecretObj();
                            c.CalculatePoints(gameState.getPlayerState(currentPlayerIndex));
                        } else {
                            throw new IllegalArgumentException("Invalid card type");
                        }
                    }

                    int scoreObj = player.getScore() - preScore;
                    scores.put(currentPlayerIndex,scoreObj);
                    rank.add(currentPlayerIndex);
                }
                Collections.sort(rank, new Comparator<Integer>() {
                    @Override
                    public int compare(Integer index1, Integer index2) {
                        PlayerState player1 = gameState.getPlayerState(index1);
                        PlayerState player2 = gameState.getPlayerState(index2);
                        if(player1.getScore() == player2.getScore()) {
                            return Integer.compare(scores.get(index2), scores.get(index1)); // Ordine decrescente
                        }else {
                            return Integer.compare(player2.getScore(), player1.getScore()); // Ordine decrescente
                        }
                    }
                });



                notifyObservers(new GameEvent("endGame", gameState.getPlayerState(currentPlayerIndex)));
            } else {
                notifyObservers(new GameEvent("lastTurn", gameState.getPlayerState(currentPlayerIndex)));
            }
        }
    }


}

