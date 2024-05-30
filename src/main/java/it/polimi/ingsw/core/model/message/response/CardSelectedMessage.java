package it.polimi.ingsw.core.model.message.response;

import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.core.model.CardSelection;

import java.rmi.RemoteException;

public class CardSelectedMessage extends MessageClient2Server {
    public CardSelectedMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(String username, GameController gc) throws RemoteException {
        gc.handleCardSelected(username, (CardSelection) getData());
    }
}