package it.polimi.ingsw.core.utils;

import it.polimi.ingsw.core.model.GameEvent;

public class PlayerMove {
    private String username;
    private GameEvent event;

    public PlayerMove(String username, GameEvent event) {
        this.username = username;
        this.event = event;
    }

    public String getUsername() {
        return username;
    }

    public GameEvent getEvent() {
        return event;
    }
}
