package it.polimi.ingsw.core.model.message.response;

import it.polimi.ingsw.core.controller.GameController;

import it.polimi.ingsw.core.model.Objective;

import java.rmi.RemoteException;

public class SelectedObjMessage extends MessageClient2Server {
    public SelectedObjMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(String username, GameController gc) throws RemoteException {
        int id = ((Objective) getData()).getId();
        System.out.println(getType() + " " + id);
        gc.handleChoseObjective(username, (Objective) getData());
    }
}
