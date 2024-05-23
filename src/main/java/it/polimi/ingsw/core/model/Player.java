package it.polimi.ingsw.core.model;

import java.io.Serializable;

public class Player implements Serializable {
    private String username;
    private GameSession currentSession;

    public Player(String username) {
        this.username = username;
    }

    public Player(){this.username = username;}

    public String getUsername() {
        return username;
    }

    public GameSession getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(GameSession currentSession) {
        this.currentSession = currentSession;
    }
}
