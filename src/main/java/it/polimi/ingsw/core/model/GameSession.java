package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.observers.GameObserver;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GameSession implements Serializable {
    private String gameId;
    private List<Player> players;
    private GameState gameState;
    private GameController gameController;
    private int desiredPlayers;

    public GameSession(String gameId, int desiredPlayers) {
        this.gameId = gameId;
        this.desiredPlayers = desiredPlayers;

        this.players = new ArrayList<>();
        this.gameState = new GameState();
        this.gameController = new GameController(gameState);
    }

    public List<GameObserver> getObservers() {
        return gameController.getObservers();
    }

    public GameController getGameController() {
        return gameController;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public int availableSlots() {
        return desiredPlayers - players.size();
    }

    public String getGameId() {
        return gameId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void startGame() throws RemoteException {
        this.gameController.startGame();
    }

    public synchronized void addPlayerToGame(String username) {
        Player player = new Player(username);
        gameState.addPlayer(player);
        players.add(player);
    }

    public synchronized void addPlayer(String username) throws RemoteException {
        System.out.println("\nAdding player '" + username + "' to game '" + gameId + "'");
        addPlayerToGame(username);
    }

    public boolean allPlayersConnected() {
        return desiredPlayers == players.size();
    }

    public void addObserver(GameObserver observer) {
        gameController.addObserver(observer);
    }

    public void removeObserver(GameObserver observer) {
        gameController.removeObserver(observer);
    }
}
