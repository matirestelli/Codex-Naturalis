package it.polimi.ingsw.core.model;

import java.io.Serializable;

/**
 * This class represents a Player in the game.
 * It implements the Serializable interface.
 * It maintains the username and the current game session of the player.
 */
public class Player implements Serializable {
    private String username;
    private GameSession currentSession;

    /**
     * Constructor for the Player class.
     * @param username The username of the player.
     */
    public Player(String username) {
        this.username = username;
    }

    /**
     * Sets the username of the player.
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Default constructor for the Player class.
     */
    public Player(){
        this.username = username;
    }

    /**
     * Returns the username of the player.
     * @return The username of the player.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the current game session of the player.
     * @return The current game session of the player.
     */
    public GameSession getCurrentSession() {
        return currentSession;
    }

    /**
     * Sets the current game session of the player.
     * @param currentSession The game session to set.
     */
    public void setCurrentSession(GameSession currentSession) {
        this.currentSession = currentSession;
    }
}