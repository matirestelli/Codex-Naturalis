package it.polimi.ingsw.core.model.chat;


public class MessagePrivate extends Message{

    private String receiverPrivate;

    public MessagePrivate(String text, String sender, String receiver){
        super(text,sender);
        this.receiverPrivate=receiver;
    }

    public MessagePrivate(String text, String receiver){
        super(text);
        this.receiverPrivate = receiver;
    }

    @Override
    public String whoIsReceiver(){
        return receiverPrivate;
    }
}
