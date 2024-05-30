package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.network.ClientAbstract;

import java.util.List;

public class LoadedPlayersMessage extends MessageServer2Client{
    public LoadedPlayersMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        client.getModelView().setPlayers((List<String>) getData());
    }
}
