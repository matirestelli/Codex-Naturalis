package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.Card;
import it.polimi.ingsw.network.ClientAbstract;

import java.util.List;

public class UpdatedDecksMessage extends MessageServer2Client{
    public UpdatedDecksMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        List<Card> updatedDecks = (List<Card>) getData();
        client.getModelView().getResourceCardsVisible().add(updatedDecks.get(0));
        client.getModelView().getResourceCardsVisible().add(updatedDecks.get(1));
        client.getModelView().getGoldCardsVisible().add(updatedDecks.get(2));
        client.getModelView().getGoldCardsVisible().add(updatedDecks.get(3));
        client.getModelView().setDeckRBack(updatedDecks.get(4));
        client.getModelView().setDeckGBack(updatedDecks.get(5));

        client.getUIStrategy().updateDecks(updatedDecks);
    }
}
