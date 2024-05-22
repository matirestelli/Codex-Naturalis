package it.polimi.ingsw.ui;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.utils.PlayableCardIds;
public class ViewModelGame {
    private Map<Player, PlayerState> playerStates;
    private Card deckGBack;
    private Card deckRBack;
    private List<Card> resourceCardsVisible;
    private List<Card> goldCardsVisible;
    private List<Objective> commonObj;



    //lista che contiene la chat di gioco broadcast rappresentata come una lista di array con chiave lo username del giocatore e valore il messaggio
    private List<String[]> broadcastChat = new ArrayList<>();

    public Map<Player, PlayerState> getPlayerStates() {
        return playerStates;
    }

    public void setPlayerStates(Map<Player, PlayerState> playerStates) {
        this.playerStates = playerStates;
    }

    public Card getDeckGBack() {
        return deckGBack;
    }

    public void setDeckGBack(Card deckGBack) {
        this.deckGBack = deckGBack;
    }

    public Card getDeckRBack() {
        return deckRBack;
    }

    public void setDeckRBack(Card deckRBack) {
        this.deckRBack = deckRBack;
    }

    public List<Card> getResourceCardsVisible() {
        return resourceCardsVisible;
    }

    public void setResourceCardsVisible(List<Card> resourceCardsVisible) {
        this.resourceCardsVisible = resourceCardsVisible;
    }

    public List<Card> getGoldCardsVisible() {
        return goldCardsVisible;
    }

    public void setGoldCardsVisible(List<Card> goldCardsVisible) {
        this.goldCardsVisible = goldCardsVisible;
    }

    public List<Objective> getCommonObj() {
        return commonObj;
    }

    public void setCommonObj(List<Objective> commonObj) {
        this.commonObj = commonObj;
    }

    public List<String[]> getBroadcastChat() {
        return broadcastChat;
    }

    public void setBroadcastChat(List<String[]> broadcastChat) {
        this.broadcastChat = broadcastChat;
    }

}
