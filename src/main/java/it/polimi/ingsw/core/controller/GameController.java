package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.observers.GameObserver;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;

public class GameController implements Serializable {
    private GameState gameState;
    private transient List<GameObserver> observers;
    private int currentPlayerIndex;

    public GameController(GameState gameState) {
        this.gameState = gameState;
        this.observers = new ArrayList<>();
        this.currentPlayerIndex = 0;
    }

    public void startGame() throws RemoteException {
        System.out.println("Game started [from GameController]");
        gameState.loadDecks();
        gameState.shuffleDecks();

        for (GameObserver observer : observers) {
            observer.update(new GameEvent("loadedStarterDeck", gameState.getStarterDeck()));
            // observers.get(i).update(new GameEvent("loadedStarterDeck", gameState));
        }

        gameState.assignStarterCardToPlayers();

        /*
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update(new GameEvent("assignedStarterCard", gameState.getPlayerState(i).getStarterCard()));
        }
        */

        gameState.assignFirstHandToPlayers();
        for (int i = 0; i < observers.size(); i++) {
            observers.get(i).update(new GameEvent("updateHand", gameState.getPlayerState(i).getHand()));
        }

        notifyCurrentPlayerTurn();
    }

    public void playerSelectsCard(String username, CardSelection cardSelection) throws RemoteException {
        int playerId = gameState.getPlayerId(username);
        System.out.println("Stampando: " + playerId + " | " + currentPlayerIndex);
        if (playerId == currentPlayerIndex) {
            int cardId = cardSelection.getId();
            // get card from player hand by id
            Card card = gameState.getPlayerState(playerId).getCardFromHand(cardId);

            gameState.getPlayerState(playerId).removeCardFromHand(card);
            // add card to player codex
            gameState.getPlayerState(playerId).addCardToCodex(card);
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
                    Card card= null;
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

