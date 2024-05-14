package it.polimi.ingsw.core.controller;

import it.polimi.ingsw.core.model.Card;
import it.polimi.ingsw.core.model.CardSelection;
import it.polimi.ingsw.core.model.GameEvent;
import it.polimi.ingsw.core.model.GameState;
import it.polimi.ingsw.observers.GameObserver;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

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
                notifyObservers(new GameEvent("endGame", gameState.getPlayerState(currentPlayerIndex)));
            } else {
                notifyObservers(new GameEvent("lastTurn", gameState.getPlayerState(currentPlayerIndex)));
            }
        }
    }


}

