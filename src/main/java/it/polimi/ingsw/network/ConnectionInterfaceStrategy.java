package it.polimi.ingsw.network;

import it.polimi.ingsw.core.model.GameEvent;

public interface ConnectionInterfaceStrategy {
    void sendMessage(String username, GameEvent event);
}
