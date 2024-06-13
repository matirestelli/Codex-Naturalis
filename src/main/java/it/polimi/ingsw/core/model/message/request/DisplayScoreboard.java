package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.Objective;
import it.polimi.ingsw.network.ClientAbstract;

import java.util.List;
import java.util.Map;

public class DisplayScoreboard extends MessageServer2Client{
    public DisplayScoreboard(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        //client.getUIStrategy().displayScoreboard((Map<String,Integer>) getData());
    }
}
