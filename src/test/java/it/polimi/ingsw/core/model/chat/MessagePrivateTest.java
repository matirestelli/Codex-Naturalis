package it.polimi.ingsw.core.model.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MessagePrivateTest {
    private MessagePrivate messagePrivate;
    private MessagePrivate messagePrivate2;
    @BeforeEach
    void setUp() {
        messagePrivate = new MessagePrivate("Hello", "Sender", "Receiver");
        messagePrivate2 = new MessagePrivate("Hello", "Receiver");
    }

    @Test
    void testGetText() {
        assertEquals("Hello", messagePrivate.getText());
    }

    @Test
    void testSetText() {
        messagePrivate.setText("Hi");
        assertEquals("Hi", messagePrivate.getText());
    }

    @Test
    void testGetSender() {
        assertEquals("Sender", messagePrivate.getSender());
    }

    @Test
    void testSetSender() {
        messagePrivate.setSender("NewSender");
        assertEquals("NewSender", messagePrivate.getSender());
    }

    @Test
    void testGetTime() {
        assertTrue(messagePrivate.getTime() instanceof LocalTime);
    }

    @Test
    void testSetTime() {
        LocalTime newTime = LocalTime.now();
        messagePrivate.setTime(newTime);
        assertEquals(newTime, messagePrivate.getTime());
    }

    @Test
    void testWhoIsReceiver() {
        assertEquals("Receiver", messagePrivate.whoIsReceiver());
    }
}