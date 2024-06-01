package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.network.ClientAbstract;

import java.util.Map;

public class BeforeTurnMessage extends MessageServer2Client {
    public BeforeTurnMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        client.getModelView().setMyResources((Map<Resource, Integer>) getData());
        client.getUIStrategy().displayPersonalResources((Map<Resource, Integer>) getData());
    }
}
