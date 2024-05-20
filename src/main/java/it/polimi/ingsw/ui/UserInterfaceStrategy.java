package it.polimi.ingsw.ui;

import it.polimi.ingsw.core.model.Card;
import it.polimi.ingsw.core.model.CardSelection;
import it.polimi.ingsw.core.model.Coordinate;
import it.polimi.ingsw.core.model.ResourceCard;

import java.util.List;

public interface UserInterfaceStrategy {
    void initialize();
    void displayMessage(String message);
    void displayCard(Card card);
    void displayCardBack(Card card);
    void displayStarterCardBack(ResourceCard card);
    String displayResourcesStarter(ResourceCard card, int index1, int index2);
    void placeCard(Card card, Coordinate position);
    void displayBoard();
    CardSelection askCardSelection(List<Integer> ids, List<Integer> idsBack);
    String displayAngle(List<Coordinate> angles);
    Coordinate placeBottomRight(Card targetCard, Card cardToPlace);
    Coordinate placeTopLeft(Card targetCard, Card cardToPlace);
    Coordinate placeTopRight(Card targetCard, Card cardToPlace);
    Coordinate placeBottomLeft(Card targetCard, Card cardToPlace);
}