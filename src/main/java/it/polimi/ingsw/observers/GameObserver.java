package it.polimi.ingsw.observers;

import it.polimi.ingsw.core.model.message.request.MessageServer2Client;

import java.rmi.Remote;

public interface GameObserver extends Remote {
    void update(MessageServer2Client event) throws java.rmi.RemoteException;
}
