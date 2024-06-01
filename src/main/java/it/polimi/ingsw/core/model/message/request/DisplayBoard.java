package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.network.ClientAbstract;

public class DisplayBoard extends MessageServer2Client{
    public DisplayBoard(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        System.out.println((String) this.getData());
    }
}
