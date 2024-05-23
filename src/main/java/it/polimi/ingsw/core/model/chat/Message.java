package it.polimi.ingsw.core.model.chat;
import java.io.Serializable;
import java.time.LocalTime;
import static org.fusesource.jansi.Ansi.ansi;

public class Message implements Serializable {
    private String text;
    private String sender;
    private LocalTime time;
    private final static int row_chat = 16;
    private final static int col_chat = 96;

    /**
     * Constructor
     * @param text
     * @param sender
     */
    public Message(String text, String sender) {
        this.time = java.time.LocalTime.now();
        this.text = text;
        this.sender = sender;
    }

    /**
     * Constructor
     */
    public Message() {
        this.time = null;
        this.text = null;
        this.sender = null;
    }

    /**
     * Returns the message in string format
     * @param i
     * @param len
     * @param isPrivate
     * @return
     */
    public String toString(int i, int len, boolean isPrivate) {
        String padding = " ".repeat(Math.max(0, (len - text.length())));
        String priv = "[Private] ";
        if (!isPrivate)
            priv = "";
        if (sender.length() > 4)
            return String.valueOf(ansi().cursor(row_chat + i + 1, col_chat).a(priv + "[").a(this.time.getHour()).a(":").a(this.time.getMinute())
                    .a(":").a(this.time.getSecond()).a("] ")
                    .a(this.getSender().substring(0, 4)).a(".").a(": ").a(this.text).a(padding));
        else
            return String.valueOf(ansi().cursor(row_chat + i + 1, col_chat).a(priv + "[").a(this.time.getHour()).a(":").a(this.time.getMinute())
                    .a(":").a(this.time.getSecond()).a("] ")
                    .a(this.getSender()).a(": ").a(this.text).a(padding));
    }

    /**
     *
     * @return the message's text content
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text in the message to the param
     * @param text text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return the message's sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the message's sender
     * @param sender sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     *
     * @return the message's time of sending
     */
    public LocalTime getTime() {
        return time;
    }

    /**
     * Sets the message time to the parameter
     * @param time param
     */
    public void setTime(LocalTime time) {
        this.time = time;
    }

    /**
     *
     * @return * (everyone is a receiver)
     */
    public String whoIsReceiver() {
        return "*";
    }
}
