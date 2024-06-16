package it.polimi.ingsw.core.utils;

import it.polimi.ingsw.core.model.chat.MessagePrivate;
import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMoveTest {

        private MessageClient2Server mex;
        @Test
        void getUsername() {
            PlayerMove playerMove = new PlayerMove("username", null);
            assertEquals("username", playerMove.getUsername());
        }

        @Test
        void getMex() {
            //mex = new MessagePrivate("test", null);
            PlayerMove playerMove = new PlayerMove(null, mex);
            //assertEquals(mex, playerMove.getMex());
        }

}