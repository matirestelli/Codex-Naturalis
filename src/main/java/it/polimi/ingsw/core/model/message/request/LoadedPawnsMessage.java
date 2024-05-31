package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.network.ClientAbstract;

import java.util.Map;

public class LoadedPawnsMessage extends MessageServer2Client{
    public LoadedPawnsMessage(String type, Object data) {
        super(type, data);
    }

    public void doAction(ClientAbstract client) {
        Map<String, Color> playerPawns = (Map<String, Color>)this.getData();
        client.getModelView().setPlayerPawns(playerPawns);
        client.getUIStrategy().displayPawn(playerPawns.get(client.getModelView().getMyUsername()));
    }
}
