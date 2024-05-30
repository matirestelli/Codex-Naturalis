package it.polimi.ingsw.core.utils;

import it.polimi.ingsw.core.model.message.GameEvent;
import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.core.model.message.response.StarterSideSelectedMessage;

public class PlayerMove {
    private String username;
    private GameEvent event;
    private MessageClient2Server mex;

    public PlayerMove(String username, MessageClient2Server mex) {
        this.username = username;
        this.mex = mex;
    }

    public String getUsername() {
        return username;
    }

    public GameEvent getEvent() {
        return event;
    }

    public MessageClient2Server getMex() {
        return mex;
    }
}
