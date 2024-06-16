package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.network.ConnectionChecker;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class GameSession {
    private String gameId; // unique identifier for the game
    // TODO: check if players is needed
    private List<Player> players; // list of players in the game
    private GameState gameState; // current state of the game
    private GameController gameController; // controller for the game
    private int desiredPlayers; // number of players required to start the game

    // Constructor for the GameSession class
    public GameSession(String gameId, int desiredPlayers) throws RemoteException {
        this.gameId = gameId;
        this.desiredPlayers = desiredPlayers;

        this.players = new ArrayList<>();
        this.gameState = new GameState();
        this.gameController = new GameController(gameState);
    }

    // Method to get the game controller
    public GameController getGameController() {
        return gameController;
    }

    // Method to check how many slots are available in the game
    public int availableSlots() {
        return desiredPlayers - players.size();
    }

    // Method to get the game ID
    public String getGameId() {
        return gameId;
    }

    // Method to add a player to the game
    public synchronized void addPlayer(String username) {
        // TODO: check if player already exists
        // TODO: check if game is full

        // TODO: Remove this or implement new logger system
        System.out.println("\nAdding player '" + username + "' to game '" + gameId + "'");

        Player player = new Player(username);
        // add player to the game state
        gameState.addPlayer(player);
        players.add(player);
    }

    // Method to check if all players are connected
    public boolean allPlayersConnected() {
        return desiredPlayers == players.size();
    }
}
