package it.polimi.ingsw.core.model.message.response;

import it.polimi.ingsw.core.controller.GameController;

import java.rmi.RemoteException;

public class StarterSideSelectedMessage extends MessageClient2Server {
    public StarterSideSelectedMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(String username, GameController gc) throws RemoteException {
        gc.handleStarterSideSelected(username, (boolean) getData());
    }
}
