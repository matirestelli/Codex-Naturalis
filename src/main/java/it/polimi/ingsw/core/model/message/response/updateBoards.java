package it.polimi.ingsw.core.model.message.response;

import it.polimi.ingsw.core.controller.GameController;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class updateBoards extends MessageClient2Server{
    public updateBoards(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(String username, GameController gc) throws RemoteException {
        List<String> strings= new ArrayList<>();
        strings.add(username);
        strings.add((String) getData());
        gc.updateBoards(strings);
    }
}
