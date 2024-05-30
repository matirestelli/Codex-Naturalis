package it.polimi.ingsw.core.model;

import it.polimi.ingsw.network.socket.client.Client;

import java.io.Serializable;

public class GameEvent implements Serializable {
    private String type;
    private Object data;

    public GameEvent(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public void doAction(Client client) {
        client.getViewModel();
    }
}