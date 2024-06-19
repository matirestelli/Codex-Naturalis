package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.model.message.response.ExitGame;
import it.polimi.ingsw.network.ClientAbstract;

import java.rmi.RemoteException;
import java.util.Map;

public class checkConnection extends MessageServer2Client {
    public checkConnection(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) throws RemoteException {
        try{
        client.getModelView().isCheckConnection();
        }catch (RemoteException e) {
            System.out.println("RemoteException caught: " + e.getMessage());
            client.sendMessage(new ExitGame("exitGame", null));
        }
    }
}
