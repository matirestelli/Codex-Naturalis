package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.network.ClientAbstract;

public class YourTurnMessage extends MessageServer2Client {
    public YourTurnMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        client.getModelView().setMyTurn(true);
        client.getUIStrategy().currentTurn((PlayableCardIds) this.getData());
    }
}
