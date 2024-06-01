package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.Card;
import it.polimi.ingsw.network.ClientAbstract;
import javafx.util.Pair;

import java.util.List;

public class EndGameMessage extends MessageServer2Client{

    public EndGameMessage(String type, Object data) {
        super(type, data);
    }

    public void doAction(ClientAbstract client) {
        client.getUIStrategy().endGame((List<Pair<String, Integer>>) getData());
    }
}
