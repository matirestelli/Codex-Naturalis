package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.network.ClientAbstract;
import javafx.util.Pair;

import java.util.List;

public class LastTurnMessage extends MessageServer2Client{
    public LastTurnMessage(String type, Object data) {
        super(type, data);
    }

    public void doAction(ClientAbstract client) {
        client.getUIStrategy().lastTurn();
    }
}
