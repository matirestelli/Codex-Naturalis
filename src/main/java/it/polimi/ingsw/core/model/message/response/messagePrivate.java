package it.polimi.ingsw.core.model.message.response;

import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.core.model.chat.MessagePrivate;

import java.rmi.RemoteException;

public class messagePrivate extends MessageClient2Server {

    public messagePrivate(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(String username, GameController gc) throws RemoteException {
        gc.receivedMessagePrivate((MessagePrivate) getData());
    }
}

