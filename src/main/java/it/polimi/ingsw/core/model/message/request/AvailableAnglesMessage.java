package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.Coordinate;
import it.polimi.ingsw.network.ClientAbstract;

import java.util.List;

public class AvailableAnglesMessage extends MessageServer2Client {
    public AvailableAnglesMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        if( this.getData() == null ) {
            client.getUIStrategy().noFreeAngles();
        }else {
            client.getUIStrategy().showAvailableAngles((List<Coordinate>) this.getData());
        }
    }
}
