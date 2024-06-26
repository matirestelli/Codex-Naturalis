package it.polimi.ingsw.network;

import it.polimi.ingsw.core.model.ModelView;
import it.polimi.ingsw.core.model.message.response.MessageClient2Server;
import it.polimi.ingsw.core.model.message.response.StarterSideSelectedMessage;
import it.polimi.ingsw.ui.UserInterfaceStrategy;

/**
 * This class is the abstract class for the client.
 */
public abstract class ClientAbstract {
    protected ModelView modelView;
    protected UserInterfaceStrategy uiStrategy;

    /**
     * Constructs a new ClientAbstract with the specified ModelView.
     *
     * @param modelView the ModelView for the client
     */
    public ClientAbstract(ModelView modelView) {
        this.modelView = modelView;
    }

    /**
     * Returns the ModelView of the client.
     *
     * @return the ModelView of the client
     */
    public ModelView getModelView() {
        return modelView;
    }

    /**
     * Returns the UserInterfaceStrategy of the client.
     *
     * @return the UserInterfaceStrategy of the client
     */
    public UserInterfaceStrategy getUIStrategy() {
        return uiStrategy;
    }

    /**
     * Disconnects the client. The specific implementation is left to the subclasses.
     */
    public abstract void disconnect();

    /**
    * Sends a message to the server.
    */
    public abstract void sendMessage(MessageClient2Server message);
}