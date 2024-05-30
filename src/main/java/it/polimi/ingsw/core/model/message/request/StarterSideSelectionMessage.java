package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.network.ClientAbstract;

public class StarterSideSelectionMessage extends MessageServer2Client {
    public StarterSideSelectionMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        client.getUIStrategy().setStarterSide();
    }
}
