package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.network.ClientAbstract;

import java.rmi.RemoteException;
import java.util.List;

public class updateBoards extends MessageServer2Client{
    public updateBoards(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) throws RemoteException {
        List<String> utility = (List<String>) getData();
        client.getModelView().setBoardToPrintByUsername(utility.get(0), utility.get(1));
    }
}