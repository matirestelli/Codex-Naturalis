package it.polimi.ingsw.observers;

import it.polimi.ingsw.core.model.GameEvent;

import java.rmi.Remote;

public interface GameObserver extends Remote {
    void update(GameEvent event) throws java.rmi.RemoteException;
}
