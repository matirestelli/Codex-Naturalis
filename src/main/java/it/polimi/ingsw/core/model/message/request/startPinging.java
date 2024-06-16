package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.network.ClientAbstract;

public class startPinging extends MessageServer2Client {
    public startPinging(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        client.startPinging();
    }
}
