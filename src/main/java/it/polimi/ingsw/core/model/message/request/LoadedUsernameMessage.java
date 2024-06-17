package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.network.ClientAbstract;

public class LoadedUsernameMessage extends MessageServer2Client{
    public LoadedUsernameMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {

        client.getModelView().setMyUsername((String) getData());
        client.getModelView().setGameStarted(true);
    }
}
