package it.polimi.ingsw.core.model.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {
    private Chat chat;
    private Chat chat2;
    private List<Message> messages;

    @BeforeEach
    void setUp() {
        chat = new Chat();
        chat2 = new Chat(messages);
    }

    @Test
    void testAddMessage() {
        chat.addMsg("Hello", "Sender", "Receiver");
        for(int i = 0; i < 100; i++)
            chat.addMsg("Sender", "Hello");
        Message message = new Message("Hello", "Sender");
        Message message2 = new Message("Hello", "Sender");
        Message message4 = new Message("Hello", "Sender");
        Message message5 = new Message("Hello", "Sender");
        Message message6 = new Message("Hello", "Sender");
        Message message7 = new Message("Hello", "Sender");
        Message message8 = new Message("Hello", "Sender");
        Message message9 = new Message("Hello", "Sender");
        Message message10 = new Message("Hello", "Sender");
        Message message11 = new Message("Hello", "Sender");
        chat.addMsg(message);
        chat.addMsg(message2);
        chat.addMsg("Sender", "Hi");
        chat.addMsg("Hello rec", "Sender", "receiver");
        chat.addMsg(message4);
        chat.addMsg(message5);
        chat.addMsg(message6);
        chat.addMsg(message7);
        chat.addMsg(message8);
        chat.addMsg(message9);
        chat.addMsg(message10);
        chat.addMsg(message11);
        chat.addMsg("ciao", "Sender");
        chat.addMsg("ciao", "Sender", "receiver");
        assertTrue(chat.getMsgs().contains(message));
        assertTrue(chat.getMsgs().contains(message10));
    }

    @Test
    void testGetMessages() {
        Message message1 = new Message("Hello", "Sender");
        Message message2 = new Message("Hi", "Sender");
        chat.addMsg(message1);
        chat.addMsg(message2);
        assertTrue(chat.getMsgs().contains(message1));
        assertTrue(chat.getMsgs().contains(message2));
    }

    @Test
    void testGetLast() {
        Message message1 = new Message("PreLast", "Sender");
        Message message2 = new Message("Last", "Sender");
        chat.addMsg(message1);
        chat.addMsg(message2);
        assertEquals("Last", chat.getLast());
    }

    @Test
    void testGetLastMessage() {
        Message message1 = new Message("PreLast", "Sender");
        Message message2 = new Message("Last", "Sender");
        chat.addMsg(message1);
        chat.addMsg(message2);
        assertEquals(message2, chat.getLastMessage());
    }

    @Test
    void testSetMessages() {
        Message message1 = new Message("set", "Sender");
        Message message2 = new Message("set2", "Sender");
        chat.addMsg(message1);
        chat.addMsg(message2);
        chat2.setMsgs(chat.getMsgs());
        assertEquals(chat.getMsgs(), chat2.getMsgs());
    }
    // Add more tests for other methods
}