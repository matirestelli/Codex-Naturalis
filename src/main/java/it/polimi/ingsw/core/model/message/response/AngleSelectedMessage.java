package it.polimi.ingsw.core.model.message.response;

import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.core.model.CardToAttachSelected;

import java.rmi.RemoteException;

public class AngleSelectedMessage extends MessageClient2Server {
    public AngleSelectedMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(String username, GameController gc) throws RemoteException {
        gc.angleChosen(username, (CardToAttachSelected) getData());
    }
}
