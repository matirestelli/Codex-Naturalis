package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.Card;
import it.polimi.ingsw.network.ClientAbstract;
import it.polimi.ingsw.observers.GameObserver;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public class DisplayCodex extends MessageServer2Client{
    public DisplayCodex(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) throws RemoteException {
        String asker = (String) getData();
        client.getUIStrategy().getBoardString(asker);
    }
}
