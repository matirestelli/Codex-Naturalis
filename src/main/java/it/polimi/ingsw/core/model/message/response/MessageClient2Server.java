package it.polimi.ingsw.core.model.message.response;

import it.polimi.ingsw.core.controller.GameController;

import java.rmi.RemoteException;

public abstract class MessageClient2Server implements java.io.Serializable {
    private String type;
    private Object data;

    public MessageClient2Server(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public Object getData() {
        return data;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void appendToType(String append) {
        type += append;
    }

    public abstract void doAction(String username, GameController gc) throws RemoteException;

}
