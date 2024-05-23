package it.polimi.ingsw.core.model.chat;
import java.io.Serializable;
import java.time.LocalTime;

public class Message implements Serializable {
    private String text;
    private String sender;
    private LocalTime time;

    public Message(String text, String sender) {
        this.time = java.time.LocalTime.now();
        this.text = text;
        this.sender = sender;
    }

    public Message(String text) {
        this.time = java.time.LocalTime.now();
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String whoIsReceiver() {
        return "all";
    }
}
