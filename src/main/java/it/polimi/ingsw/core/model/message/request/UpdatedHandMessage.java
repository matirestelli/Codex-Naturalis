package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.Card;
import it.polimi.ingsw.network.ClientAbstract;

import java.util.List;

public class UpdatedHandMessage extends MessageServer2Client {
    public UpdatedHandMessage(String type, Object data) {
        super(type, data);
    }

    public void doAction(ClientAbstract client) {
        client.getModelView().setMyHand((List<Card>) this.getData());
        client.getUIStrategy().displayHand((List<Card>) this.getData());
    }
}
