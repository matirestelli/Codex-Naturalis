package it.polimi.ingsw.network.rmi.server;

import it.polimi.ingsw.network.rmi.client.GameClient;
import it.polimi.ingsw.observers.GameObserver;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GameServer extends Remote {
    void registerClient(GameClient client) throws RemoteException;

    void createNewSession(String gameId, String username, int desiredPlayers, GameObserver observer) throws RemoteException;

    void joinGameSession(String gameId, String username, GameObserver observer) throws RemoteException;

    String listGameSessions() throws RemoteException;
}
