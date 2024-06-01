package it.polimi.ingsw.core.model.message.response;
import it.polimi.ingsw.core.controller.GameController;
import it.polimi.ingsw.core.model.chat.Message;
import java.rmi.RemoteException;

public class messageBroadcast extends MessageClient2Server {

        public messageBroadcast(String type, Object data) {
            super(type, data);
        }

        @Override
        public void doAction(String username, GameController gc) throws RemoteException {
            gc.receivedMessageBroadcast((Message) getData());
        }
}

