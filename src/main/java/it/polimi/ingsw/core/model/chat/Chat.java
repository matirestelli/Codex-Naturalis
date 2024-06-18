package it.polimi.ingsw.core.model.chat;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a chat in the application.
 * It implements Serializable and ChatIC interfaces.
 * It maintains a list of messages and provides methods to add and retrieve messages.
 */
public class Chat implements Serializable, ChatIC {
    /**
     * List of messages in the chat.
     */
    private List<Message> msgs;

    /**
     * Maximum number of messages shown in the chat.
     */
    private final int max_messagesShown = 100;

    /**
     * Default constructor. Initializes the list of messages.
     */
    public Chat() {
        msgs = new ArrayList<>();
    }

    /**
     * Constructor that initializes the list of messages with the given list.
     * @param msgs List of messages.
     */
    public Chat(List<Message> msgs) {
        this.msgs = msgs;
    }

    /**
     * Returns the list of messages in the chat.
     * @return List of messages.
     */
    public List<Message> getMsgs() {
        return msgs;
    }

    /**
     * Adds a message to the chat. If the number of messages exceeds the maximum limit, the oldest message is removed.
     * @param m Message to be added.
     */
    public void addMsg(Message m) {
        if (msgs.size() > max_messagesShown)
            msgs.remove(0);
        msgs.add(m);
    }

    /**
     * Adds a message to the chat with the given sender and text. If the number of messages exceeds the maximum limit, the oldest message is removed.
     * @param sender Sender of the message.
     * @param text Text of the message.
     */
    public void addMsg(String sender, String text) {
        Message temp = new Message(text, sender);
        if (msgs.size() > max_messagesShown)
            msgs.remove(0);
        msgs.add(temp);
    }

    /**
     * Adds a private message to the chat with the given sender, receiver and text. If the number of messages exceeds the maximum limit, the oldest message is removed.
     * @param text Text of the message.
     * @param sender Sender of the message.
     * @param receiver Receiver of the message.
     */
    public void addMsg( String text, String sender,String receiver) {
        MessagePrivate temp = new MessagePrivate(text, sender, receiver);
        if (msgs.size() > max_messagesShown)
            msgs.remove(0);
        msgs.add(temp);
    }

    /**
     * Returns the text of the last message in the chat.
     * @return Text of the last message.
     */
    public String getLast() {
        return msgs.get(msgs.size() - 1).getText();
    }

    /**
     * Returns the last message in the chat.
     * @return Last message.
     */
    public Message getLastMessage() {
        return msgs.get(msgs.size() - 1);
    }

    /**
     * Sets the list of messages in the chat.
     * @param msgs List of messages.
     */
    public void setMsgs(List<Message> msgs) {
        this.msgs = msgs;
    }
}