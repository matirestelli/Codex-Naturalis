package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.Objective;
import it.polimi.ingsw.network.ClientAbstract;

import java.util.List;

public class LoadedCommonObjectiveMessage extends MessageServer2Client {
    public LoadedCommonObjectiveMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        List<Objective> commonObj = (List<Objective>) this.getData();
        client.getModelView().setCommonObj(commonObj);
        client.getUIStrategy().displayCommonObjective(commonObj);
    }
}
