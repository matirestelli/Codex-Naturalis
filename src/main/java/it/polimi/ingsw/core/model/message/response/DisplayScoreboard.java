package it.polimi.ingsw.core.model.message.response;

import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.core.model.message.request.MessageServer2Client;
import it.polimi.ingsw.network.ClientAbstract;

import java.rmi.RemoteException;
import java.util.Map;

public class DisplayScoreboard extends MessageClient2Server{
    public DisplayScoreboard(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(String username, GameController gc) throws RemoteException {
        gc.scoreboard(username);
    }
}

