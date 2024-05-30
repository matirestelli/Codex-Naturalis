package it.polimi.ingsw.network.rmi.client;

import it.polimi.ingsw.core.model.message.request.MessageServer2Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameClient extends Remote {
    void update(MessageServer2Client event) throws RemoteException;
}