package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.network.ClientAbstract;


public class displayMenu extends MessageServer2Client {
    public displayMenu(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        client.getUIStrategy().selectFromMenu();
    }
}

