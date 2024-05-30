package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.Objective;
import it.polimi.ingsw.network.ClientAbstract;

import java.util.List;

public class ChooseSecretObjMessage extends MessageServer2Client {
    public ChooseSecretObjMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        client.getUIStrategy().chooseObjective((List<Objective>) this.getData());
    }
}
