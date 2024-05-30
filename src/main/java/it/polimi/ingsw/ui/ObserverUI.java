package it.polimi.ingsw.ui;

import it.polimi.ingsw.core.model.message.GameEvent;

public interface ObserverUI {
    void updateUI(GameEvent gameEvent);
}
