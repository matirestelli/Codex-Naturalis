package it.polimi.ingsw.core.model.chat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chat implements Serializable, ChatIC {
    private List<Message> msgs;

    private final int max_messagesShown = 10;

    public Chat() {
        msgs = new ArrayList<>();
    }

    public Chat(List<Message> msgs) {
        this.msgs = msgs;
    }

    public List<Message> getMsgs() {
        return msgs;
    }

    public void addMsg(Message m) {
        if (msgs.size() > max_messagesShown)
            msgs.remove(0);
        msgs.add(m);
    }

    //add a msg to the chat
    public void addMsg(String sender, String text) {
        Message temp = new Message(text, sender);
        if (msgs.size() > max_messagesShown)
            msgs.remove(0);
        msgs.add(temp);
    }

    //add a private msg to the chat
    public void addMsg( String text, String sender,String receiver) {
        MessagePrivate temp = new MessagePrivate(text, sender, receiver);
        if (msgs.size() > max_messagesShown)
            msgs.remove(0);
        msgs.add(temp);
    }

    public String getLast() {
        return msgs.get(msgs.size() - 1).getText();
    }

    public Message getLastMessage() {
        return msgs.get(msgs.size() - 1);
    }

    public void setMsgs(List<Message> msgs) {
        this.msgs = msgs;
    }
}
