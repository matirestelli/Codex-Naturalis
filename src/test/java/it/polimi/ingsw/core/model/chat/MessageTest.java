package it.polimi.ingsw.core.model.chat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class MessageTest {
    private Message message;
    private Message message2;

    @BeforeEach
    void setUp() {
        message = new Message("Hello", "Sender");
        message2 = new Message("Hello");
    }

    @Test
    void testGetText() {
        assertEquals("Hello", message.getText());
    }

    @Test
    void testSetText() {
        message.setText("Hi");
        assertEquals("Hi", message.getText());
    }

    @Test
    void testGetSender() {
        assertEquals("Sender", message.getSender());
    }

    @Test
    void testSetSender() {
        message.setSender("NewSender");
        assertEquals("NewSender", message.getSender());
    }

    @Test
    void testGetTime() {
        assertTrue(message.getTime() instanceof LocalTime);
    }

    @Test
    void testSetTime() {
        LocalTime newTime = LocalTime.now();
        message.setTime(newTime);
        assertEquals(newTime, message.getTime());
    }

    @Test
    void testWhoIsReceiver() {
        assertEquals("all", message.whoIsReceiver());
    }
}