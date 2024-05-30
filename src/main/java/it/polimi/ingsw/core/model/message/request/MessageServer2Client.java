package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.network.ClientAbstract;

import java.io.Serializable;
import java.rmi.RemoteException;

public abstract class MessageServer2Client implements Serializable {
    private String type;
    private Object data;

    public MessageServer2Client(String type, Object data) {
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

    public abstract void doAction(ClientAbstract client) throws RemoteException;
}
