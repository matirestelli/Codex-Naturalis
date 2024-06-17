package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.controller.GameController;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a GameSession in the game.
 * It maintains the game id, list of players, game state, game controller, and the desired number of players.
 */
public class GameSession {
    private String gameId; // unique identifier for the game
    private List<Player> players; // list of players in the game
    private GameState gameState; // current state of the game
    private GameController gameController; // controller for the game
    private int desiredPlayers; // number of players required to start the game

    /**
     * Constructs a GameSession with the given game id and desired number of players.
     * @param gameId The game id.
     * @param desiredPlayers The desired number of players.
     */
    public GameSession(String gameId, int desiredPlayers) throws RemoteException {
        this.gameId = gameId;
        this.desiredPlayers = desiredPlayers;

        this.players = new ArrayList<>();
        this.gameState = new GameState();
        this.gameController = new GameController(gameState);
    }

    /**
     * Returns the game controller.
     * @return The game controller.
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Returns the number of available slots in the game.
     * @return The number of available slots.
     */
    public int availableSlots() {
        return desiredPlayers - players.size();
    }

    /**
     * Returns the game id.
     * @return The game id.
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * Adds a player to the game.
     * @param username The username of the player to add.
     */
    public synchronized void addPlayer(String username) {
        Player player = new Player(username);
        gameState.addPlayer(player);
        players.add(player);
    }

    /**
     * Checks if all players are connected.
     * @return True if all players are connected, false otherwise.
     */
    public boolean allPlayersConnected() {
        return desiredPlayers == players.size();
    }
}