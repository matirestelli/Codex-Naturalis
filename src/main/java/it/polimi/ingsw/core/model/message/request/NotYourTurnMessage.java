package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.network.ClientAbstract;

public class NotYourTurnMessage extends MessageServer2Client {
    public NotYourTurnMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        client.getModelView().setMyTurn(false);
        client.getUIStrategy().showNotYourTurn();
    }
}
