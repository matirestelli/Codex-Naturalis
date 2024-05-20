package it.polimi.ingsw.ui;

import it.polimi.ingsw.core.model.Card;
import it.polimi.ingsw.core.model.CardSelection;
import it.polimi.ingsw.core.model.Coordinate;
import it.polimi.ingsw.core.model.ResourceCard;

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
    public CardSelection askCardSelection(List<Integer> ids, List<Integer> idsBack) {
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
}
