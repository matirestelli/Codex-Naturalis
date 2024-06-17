package it.polimi.ingsw.core.model.chat;

import java.util.List;

/**
 * The ChatIC interface represents a chat in the application.
 * It provides methods to retrieve messages and the last message from the chat.
 */
public interface ChatIC {

    /**
     * Retrieves the list of messages in the chat.
     * @return A list of messages {@link Message} in the chat.
     */
    List<Message> getMsgs();

    /**
     * Retrieves the text of the last message in the chat.
     * @return The text of the last message in the chat.
     */
    String getLast();

    /**
     * Retrieves the last message in the chat.
     * @return The last message {@link Message} in the chat.
     */
    Message getLastMessage();
}
