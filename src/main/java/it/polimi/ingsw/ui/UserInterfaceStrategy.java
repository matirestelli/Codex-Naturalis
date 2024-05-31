package it.polimi.ingsw.ui;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.chat.Chat;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.network.ClientAbstract;

import java.util.List;

public interface UserInterfaceStrategy {
    //metodi per i messaggi
    void showAvailableAngles(List<Coordinate> data);
    void chooseObjective(List<Objective> obj);
    void askWhereToDraw(List<Card> cards);
    void displayCommonObjective(List<Objective> obj);
    void showNotYourTurn();
    public void visualiseStarterCardLoaded(Card card);
    void setStarterSide();

    void displayHand(List<Card> hand);
    void currentTurn(PlayableCardIds data);
    void updateDecks(List<Card> updatedDecks);
    void updateMyPlayerState();
    void updatePlayerState(String player);

    //todo ancora in messaggi
    void lastTurn();
    //todo ancora in messaggi
    void endGame();

    //metodi per la cli:
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

    void place(Card cardToPlace, Card targetCard, int position);
    public void displayChat(Chat chat, String username);
    public void selectFromMenu();
    public String askUsername();
    public String askJoinCreate();
    public String askGameId(String joinCreate, String gameIds);
    public int askNumberOfPlayers();
    public void displayPawn(Color pawn);

    void setClient(ClientAbstract client);
    void setViewModel(ModelView modelView);
}