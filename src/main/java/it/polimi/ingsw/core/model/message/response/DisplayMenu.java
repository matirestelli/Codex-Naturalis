package it.polimi.ingsw.core.model.message.response;

import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.core.model.chat.Message;

import java.rmi.RemoteException;

public class DisplayMenu extends MessageClient2Server{

    public DisplayMenu(String type, Object data) {
        super(type, data);
    }

    public void doAction(String username, GameController gc) throws RemoteException {
        gc.notYourTurn(username);
    }
}
