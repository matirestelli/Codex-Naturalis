package it.polimi.ingsw.core.model.message.response;

import it.polimi.ingsw.core.controller.GameController;

import java.rmi.RemoteException;


public class ExitGame extends MessageClient2Server{
    public ExitGame(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(String username, GameController gc) throws RemoteException {
        gc.exitGame(username);
    }
}

