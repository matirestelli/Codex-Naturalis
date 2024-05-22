package it.polimi.ingsw.ui;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.observers.GameObserver;

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
    CardSelection askCardSelection(PlayableCardIds ids, List<Card> cards);
    String displayAngle(List<Coordinate> angles);
    Coordinate placeBottomRight(Card targetCard, Card cardToPlace);
    Coordinate placeTopLeft(Card targetCard, Card cardToPlace);
    Coordinate placeTopRight(Card targetCard, Card cardToPlace);
    Coordinate placeBottomLeft(Card targetCard, Card cardToPlace);
    void visualizeStarterCard(Card card);
    boolean setStarterSide();
    void displayCommonObjective(List<Objective> obj);
    Objective chooseObjective(List<Objective> obj);
    void displayHand(List<Card> hand);
    void place(Card cardToPlace, Card targetCard, int position);
    String askWhereToDraw(List<Card> cards);


}