package it.polimi.ingsw.core.utils;

import it.polimi.ingsw.core.model.message.response.MessageClient2Server;

/**
 * This class represents a PlayerMove in the game.
 * It maintains the username and message of the player move.
 */
public class PlayerMove {
    private String username;
    private MessageClient2Server mex;

    /**
     * Constructor for the PlayerMove class.
     * @param username The username of the player.
     * @param mex The message of the player move.
     */
    public PlayerMove(String username, MessageClient2Server mex) {
        this.username = username;
        this.mex = mex;
    }

    /**
     * Returns the username of the player move.
     * @return The username of the player move.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the message of the player move.
     * @return The message of the player move.
     */
    public MessageClient2Server getMex() {
        return mex;
    }
}