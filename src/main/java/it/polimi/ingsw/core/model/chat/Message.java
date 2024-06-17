package it.polimi.ingsw.core.model.chat;
import it.polimi.ingsw.core.controller.GameController;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * This class represents a message in the chat.
 * It implements Serializable interface.
 * It maintains the text, sender and time of the message.
 */
public class Message implements Serializable {
    private String text;
    private String sender;
    private LocalTime time;

    /**
     * Constructor that initializes the text and sender of the message.
     * The time is set to the current time.
     * @param text Text of the message.
     * @param sender Sender of the message.
     */
    public Message(String text, String sender) {
        this.time = java.time.LocalTime.now();
        this.text = text;
        this.sender = sender;
    }

    /**
     * Constructor that initializes the text of the message.
     * The time is set to the current time.
     * @param text Text of the message.
     */
    public Message(String text) {
        this.time = java.time.LocalTime.now();
        this.text = text;
    }

    /**
     * Returns the text of the message.
     * @return Text of the message.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the message.
     * @param text Text of the message.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the sender of the message.
     * @return Sender of the message.
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the sender of the message.
     * @param sender Sender of the message.
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Returns the time of the message.
     * @return Time of the message.
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the time of the message.
     * @param time Time of the message.
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     * Returns the receiver of the message.
     * @return Receiver of the message.
     */
    public String whoIsReceiver() {
        return "all";
    }

    /**
     * Performs an action based on the message.
     * @param username Username of the user performing the action.
     * @param gameController GameController object to perform the action.
     */
    public void doAction(String username, GameController gameController) {

    }
}