package it.polimi.ingsw.core.model.message.request;

import it.polimi.ingsw.core.model.ViewModelPlayerState;
import it.polimi.ingsw.network.ClientAbstract;

import java.util.Map;

public class UpdatedPlayerStateMessage extends MessageServer2Client{
    public UpdatedPlayerStateMessage(String type, Object data) {
        super(type, data);
    }

    @Override
    public void doAction(ClientAbstract client) {
        Map<String, ViewModelPlayerState> playerUpdated = (Map<String, ViewModelPlayerState>) getData();
        String usernameTarget = playerUpdated.keySet().stream().findFirst().orElse(null);
        if(usernameTarget.equals(client.getModelView().getMyUsername())){
            client.getModelView().setMyCodex(playerUpdated.get(client.getModelView().getMyUsername()).getCodex());
            client.getModelView().setMyResources(playerUpdated.get(client.getModelView().getMyUsername()).getPersonalResources());
            client.getModelView().setMyScore(playerUpdated.get(client.getModelView().getMyUsername()).getScore());

            client.getUIStrategy().updateMyPlayerState();
        }
        else{
            client.getModelView().setStateOfPlayer(usernameTarget, playerUpdated.get(usernameTarget));

            client.getUIStrategy().updatePlayerState(usernameTarget);
        }
    }
}
