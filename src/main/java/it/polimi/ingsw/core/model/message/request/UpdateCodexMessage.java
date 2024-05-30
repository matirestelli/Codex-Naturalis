package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.Card;
import it.polimi.ingsw.network.ClientAbstract;

import java.rmi.RemoteException;
import java.util.List;

public class UpdateCodexMessage extends MessageServer2Client {
    public UpdateCodexMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) throws RemoteException {
        client.getModelView().setMyCodex((List<Card>) getData());
    }
}
