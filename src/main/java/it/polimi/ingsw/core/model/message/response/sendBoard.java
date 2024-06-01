package it.polimi.ingsw.core.model.message.response;

import it.polimi.ingsw.core.controller.GameController;

import java.rmi.RemoteException;
import java.util.List;

public class sendBoard extends MessageClient2Server{
    public sendBoard(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(String username, GameController gc) throws RemoteException {
        gc.printBoard((List<String>) this.getData());
    }
}

