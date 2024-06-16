package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.PlayerState;
import it.polimi.ingsw.network.ClientAbstract;

public class playerStates extends MessageServer2Client {
    public playerStates(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        client.getModelView().setMyPlayerState((PlayerState)getData());
    }
}
