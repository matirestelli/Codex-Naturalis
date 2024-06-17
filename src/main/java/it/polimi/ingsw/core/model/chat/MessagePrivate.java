package it.polimi.ingsw.core.model.chat;

/**
 * This class represents a private message in the chat.
 * It extends the Message class.
 * It maintains the receiver of the private message.
 */
public class MessagePrivate extends Message{

    /**
     * Receiver of the private message.
     */
    private String receiverPrivate;

    /**
     * Constructor that initializes the text, sender and receiver of the private message.
     * @param text Text of the message.
     * @param sender Sender of the message.
     * @param receiver Receiver of the private message.
     */
    public MessagePrivate(String text, String sender, String receiver){
        super(text,sender);
        this.receiverPrivate=receiver;
    }

    /**
     * Constructor that initializes the text and receiver of the private message.
     * @param text Text of the message.
     * @param receiver Receiver of the private message.
     */
    public MessagePrivate(String text, String receiver){
        super(text);
        this.receiverPrivate = receiver;
    }

    /**
     * Returns the receiver of the private message.
     * @return Receiver of the private message.
     */
    @Override
    public String whoIsReceiver(){
        return receiverPrivate;
    }
}