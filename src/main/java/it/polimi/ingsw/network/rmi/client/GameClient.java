package it.polimi.ingsw.network.rmi.client;

import it.polimi.ingsw.core.model.GameEvent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameClient extends Remote {
    void notify(GameEvent event) throws RemoteException;
}