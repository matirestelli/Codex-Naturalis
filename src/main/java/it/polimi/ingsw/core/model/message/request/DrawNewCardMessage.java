package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.Card;
import it.polimi.ingsw.network.ClientAbstract;

import java.util.List;

public class DrawNewCardMessage extends MessageServer2Client {
    public DrawNewCardMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {

        client.getUIStrategy().askWhereToDraw((List<Card>) getData());
    }
}
