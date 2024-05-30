package it.polimi.ingsw.network;

import it.polimi.ingsw.core.model.message.request.MessageServer2Client;
import it.polimi.ingsw.network.rmi.client.GameClientImpl;
import it.polimi.ingsw.observers.GameObserver;
import it.polimi.ingsw.ui.UserInterfaceStrategy;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GameClientProxy extends UnicastRemoteObject implements GameObserver {
    private GameClientImpl clientImpl;

    public GameClientProxy(GameClientImpl clientImpl) throws RemoteException {
        super();
        this.clientImpl = clientImpl;
    }

    @Override
    public void update(MessageServer2Client message) throws RemoteException {
        clientImpl.update(message);
    }

    public UserInterfaceStrategy getUIStrategy() {
        return clientImpl.getUIStrategy();
    }
}
