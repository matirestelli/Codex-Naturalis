package it.polimi.ingsw.network;

import it.polimi.ingsw.core.model.message.request.MessageServer2Client;
import it.polimi.ingsw.network.rmi.client.GameClientImpl;
import it.polimi.ingsw.observers.GameObserver;
import it.polimi.ingsw.ui.UserInterfaceStrategy;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class is the proxy of the client that is used by the server to notify the client of updates.
 */
public class GameClientProxy extends UnicastRemoteObject implements GameObserver {
    private GameClientImpl clientImpl;

    /**
     * Constructs a new GameClientProxy with the specified client implementation.
     *
     * @param clientImpl the client implementation that this proxy is associated with
     * @throws RemoteException if the exportObject call fails
     */
    public GameClientProxy(GameClientImpl clientImpl) throws RemoteException {
        super();
        this.clientImpl = clientImpl;
    }

    /**
     * Updates the client with the specified message.
     *
     * @param message the message to send to the client
     * @throws RemoteException if the remote invocation fails
     */
    @Override
    public void update(MessageServer2Client message) throws RemoteException {
        clientImpl.update(message);
    }

    /**
     * Returns the user interface strategy of the client.
     *
     * @return the user interface strategy of the client
     */
    public UserInterfaceStrategy getUIStrategy() {
        return clientImpl.getUIStrategy();
    }
}
