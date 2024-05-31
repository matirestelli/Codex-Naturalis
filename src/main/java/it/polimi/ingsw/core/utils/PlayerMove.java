package it.polimi.ingsw.core.utils;

import it.polimi.ingsw.core.model.message.response.MessageClient2Server;

public class PlayerMove {
    private String username;
    private MessageClient2Server mex;

    public PlayerMove(String username, MessageClient2Server mex) {
        this.username = username;
        this.mex = mex;
    }

    public String getUsername() {
        return username;
    }

    public MessageClient2Server getMex() {
        return mex;
    }
}
