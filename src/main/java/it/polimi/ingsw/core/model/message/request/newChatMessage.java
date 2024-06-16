package it.polimi.ingsw.core.model.message.request;
import it.polimi.ingsw.core.model.chat.Message;
import it.polimi.ingsw.core.model.chat.MessagePrivate;
import it.polimi.ingsw.network.ClientAbstract;

public class newChatMessage extends MessageServer2Client  {
    public newChatMessage(String type, Object data) {
        super(type, data);
    }

    public void doAction(ClientAbstract client){
        Message message = ((Message) getData());
        client.getModelView().addUnreadedMessage();
        if (message.getSender().equals(client.getModelView().getMyUsername())) {
            message.setSender("You");
            client.getModelView().getChat().addMsg(message);
        } else {
            client.getModelView().getChat().addMsg(message);
        }
        client.getUIStrategy().newChatMessage(message);
    }
}
