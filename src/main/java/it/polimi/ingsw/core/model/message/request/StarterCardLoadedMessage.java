package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.Card;
import it.polimi.ingsw.core.model.Coordinate;
import it.polimi.ingsw.core.model.ResourceCard;
import it.polimi.ingsw.network.ClientAbstract;

public class StarterCardLoadedMessage extends MessageServer2Client {
    public StarterCardLoadedMessage(String type, Object data) {
        super(type, data);
    }

    public void doAction(ClientAbstract client) {
        ResourceCard starterCard = (ResourceCard) this.getData();
        // TODO: fix this hardcoded value
        starterCard.setCentre(new Coordinate(10 / 2 * 7 - 5,10 / 2 * 3 - 5));
        client.getModelView().setMyStarterCard((ResourceCard) this.getData());
        client.getModelView().addCardToCodex((Card) this.getData());
        client.getUIStrategy().visualiseStarterCardLoaded((Card) this.getData());
    }
}
