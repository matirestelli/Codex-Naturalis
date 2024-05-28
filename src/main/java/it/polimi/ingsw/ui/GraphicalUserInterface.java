package it.polimi.ingsw.ui;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.chat.Chat;
import it.polimi.ingsw.core.utils.PlayableCardIds;

import java.util.List;

public class GraphicalUserInterface implements UserInterfaceStrategy {
    @Override
    public void initialize() {

    }

    @Override
    public void displayMessage(String message) {

    }

    @Override
    public void displayCard(Card card) {

    }

    @Override
    public void displayStarterCardBack(ResourceCard card) {

    }

    @Override
    public String displayResourcesStarter(ResourceCard card, int index1, int index2) {
        return null;
    }

    @Override
    public void displayCardBack(Card card) {

    }

    @Override
    public void placeCard(Card card, Coordinate position) {

    }

    @Override
    public void displayBoard() {

    }

    @Override
    public CardSelection askCardSelection(PlayableCardIds ids, List<Card> cards) {
        return null;
    }

    @Override
    public String displayAngle(List<Coordinate> angles) {
        return null;
    }

    @Override
    public Coordinate placeBottomRight(Card targetCard, Card cardToPlace) {
        return null;
    }

    @Override
    public Coordinate placeTopLeft(Card targetCard, Card cardToPlace) {
        return null;
    }

    @Override
    public Coordinate placeTopRight(Card targetCard, Card cardToPlace) {
        return null;
    }

    @Override
    public Coordinate placeBottomLeft(Card targetCard, Card cardToPlace) {
        return null;
    }

    public void visualizeStarterCard(Card card) {
    }

    public void setStarterSide() {
    }

    public void displayCommonObjective(List<Objective> obj) {
    }

    public Objective chooseObjective(List<Objective> obj) {
        return null;
    }

    public void displayHand(List<Card> hand) {
    }

    public void place(Card cardToPlace, Card targetCard, int position) {
    }

    public String askWhereToDraw(List<Card> cards) {
        return null;
    }

    public void displayChat(Chat chat, String username){}

    public void selectFromMenu(){}

    public String askUsername(){return null;}

    public String askJoinCreate(){return null;}

    public String askGameId(String joinCreate, String gameIds){return null;}

    public int askNumberOfPlayers(){return 0;}
}
