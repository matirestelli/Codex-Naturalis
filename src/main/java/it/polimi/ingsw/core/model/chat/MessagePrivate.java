package it.polimi.ingsw.core.model.chat;


public class MessagePrivate extends Message{

    private String receiverPrivate;

    public MessagePrivate(String text, String sender, String receiver){
        super(text,sender);
        this.receiverPrivate=receiver;
    }

    @Override
    public String whoIsReceiver(){
        return receiverPrivate;
    }
}
