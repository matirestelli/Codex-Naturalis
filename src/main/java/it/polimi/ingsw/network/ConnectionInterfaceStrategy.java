package it.polimi.ingsw.network;

import it.polimi.ingsw.core.model.message.GameEvent;

public interface ConnectionInterfaceStrategy {
    void sendMessage(String username, GameEvent event);
}
