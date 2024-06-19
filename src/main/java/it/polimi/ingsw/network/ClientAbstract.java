package it.polimi.ingsw.network;

import it.polimi.ingsw.core.model.ModelView;
import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.core.model.message.response.StarterSideSelectedMessage;
import it.polimi.ingsw.ui.UserInterfaceStrategy;

public abstract class ClientAbstract {
    protected ModelView modelView;
    protected UserInterfaceStrategy uiStrategy;

    public ClientAbstract(ModelView modelView) {
        this.modelView = modelView;
    }

    public ModelView getModelView() {
        return modelView;
    }

    public UserInterfaceStrategy getUIStrategy() {
        return uiStrategy;
    }

    public abstract void disconnect();

    public abstract void sendMessage(MessageClient2Server message);
}