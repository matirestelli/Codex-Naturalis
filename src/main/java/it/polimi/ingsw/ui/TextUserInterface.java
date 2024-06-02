package it.polimi.ingsw.ui;

import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.chat.Chat;
import it.polimi.ingsw.core.model.chat.Message;
import it.polimi.ingsw.core.model.chat.MessagePrivate;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;
import it.polimi.ingsw.core.model.message.response.*;
import it.polimi.ingsw.core.utils.PlayableCardIds;
import it.polimi.ingsw.network.ClientAbstract;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TextUserInterface implements UserInterfaceStrategy {
    private Scanner scanner = new Scanner(System.in);
    private int cardWidth = 7;
    private int cardHeight = 3;
    private int matrixDimension = 10;
    private Cell[][] gameBoard;
    private ClientAbstract gameClient;

    public TextUserInterface(ClientAbstract gameClient) {
        this.gameClient = gameClient;
        this.gameBoard = new Cell[matrixDimension * cardHeight][matrixDimension * cardWidth];
        for (int i = 0; i < matrixDimension * cardHeight; i++)
            for (int j = 0; j < matrixDimension * cardWidth; j++) {
                gameBoard[i][j] = new Cell();
                this.gameBoard[i][j].setCharacter(' ');
            }
    }

    @Override
    public void visualiseStarterCardLoaded(Card card) {
        visualizeStarterCard(card);
        // gameClient.sendMessage(new StarterSideSelectedMessage("starterSideSelected", side));
    }

    @Override
    public void displayCard(Card card) {
        Card c = card instanceof ResourceCard ? (ResourceCard) card : (GoldCard) card;
        AnsiColor ANSIColor = getANSIColorForCard(c);

        System.out.println(displayResources(c, 1, 2, ANSIColor));
        if (c.getId() > 9)
            System.out.println(ANSIColor + "  " + AnsiColor.BOLD + c.getId() + "   " + AnsiColor.RESET);
        else
            System.out.println(ANSIColor + "   " + AnsiColor.BOLD + c.getId() + "   " + AnsiColor.RESET);
        System.out.println(displayResources(c, 0, 3, ANSIColor));

        System.out.println();
    }

    @Override
    public void displayCardBack(Card card1) {
        Card card = card1 instanceof ResourceCard ? (ResourceCard) card1 : (GoldCard) card1;
        AnsiColor ANSIColor = getANSIColorForCard(card1);

        System.out.println(ANSIColor.WHITE_BACKGROUND + " " + ANSIColor + "  " + card.getBackResources().getFirst().toString().charAt(0) + "  " + AnsiColor.WHITE_BACKGROUND + " " + ANSIColor.RESET);
        if (card.getId() > 9)
            System.out.println(ANSIColor + "  " + AnsiColor.BOLD + card.getId() + "   " + AnsiColor.RESET);
        else
            System.out.println(ANSIColor + "   " + AnsiColor.BOLD + card.getId() + "   " + AnsiColor.RESET);
        System.out.println(AnsiColor.WHITE_BACKGROUND + " " + ANSIColor + "     " + AnsiColor.WHITE_BACKGROUND + " " + AnsiColor.RESET);

        System.out.println();
    }

    public String displayResources(Card card, int index1, int index2, AnsiColor ANSIColor) {
        String upResources = "";
        upResources += ANSIColor;

        if (card.getFrontCorners().containsKey(index1))
            if (!card.getFrontCorners().get(index1).isEmpty())
                upResources += card.getFrontCorners().get(index1).getResource().toString().charAt(0); //
            else
                upResources += ANSIColor.WHITE_BACKGROUND + " " + ANSIColor;
        else
            upResources += " ";

        if (card instanceof ResourceCard c) {
            if (index1 == 1 && c.getPoint() > 0)
                upResources += "  " + ANSIColor.YELLOW + c.getPoint() + "  " + ANSIColor.RESET;
            else
                upResources += "     ";
        } else if (card instanceof GoldCard c) {
            if (index1 == 1) {
                if (c.getPoint().getResource() != Resource.NO_RESOURCE)
                    upResources += " " + ANSIColor.YELLOW + c.getPoint().getQta() + c.getPoint().getResource().toString().charAt(0) + "  " + AnsiColor.RESET;
                else
                    upResources += "  " + ANSIColor.YELLOW + c.getPoint().getQta() + "  " + ANSIColor.RESET;
            } else if (index1 == 0) {
                if (c.getRequirements().size() == 1)
                    upResources += " " + c.getRequirements().getFirst().getQta() + c.getRequirements().getFirst().getResource().toString().charAt(0) + " ";
                else
                    for (Requirement r : c.getRequirements())
                        upResources += Integer.toString(r.getQta()) + r.getResource().toString().charAt(0);
                upResources += " ";
            }
        }

        upResources += ANSIColor;
        if (card.getFrontCorners().containsKey(index2))
            if (!card.getFrontCorners().get(index2).isEmpty())
                upResources += card.getFrontCorners().get(index2).getResource().toString().charAt(0);
            else
                upResources += ANSIColor.WHITE_BACKGROUND + " " + ANSIColor.RESET;
        else
            upResources += ANSIColor + " " + ANSIColor.RESET;

        upResources += ANSIColor.RESET;

        return upResources;
    }

    public AnsiColor getANSIColorForCard(Card card1) {
        AnsiColor ANSIColor;

        if (card1.getColor() == Color.RED)
            ANSIColor = card1 instanceof GoldCard ? AnsiColor.GOLD_RED_BACKGROUND : AnsiColor.RED_BACKGROUND;
        else if (card1.getColor() == Color.BLUE)
            ANSIColor = card1 instanceof GoldCard ? AnsiColor.GOLD_BLUE_BACKGROUND : AnsiColor.BLUE_BACKGROUND;
        else if (card1.getColor() == Color.PURPLE)
            ANSIColor = card1 instanceof GoldCard ? AnsiColor.GOLD_PURPLE_BACKGROUND : AnsiColor.PURPLE_BACKGROUND;
        else if (card1.getColor() == Color.GREEN)
            ANSIColor = card1 instanceof GoldCard ? AnsiColor.GOLD_GREEN_BACKGROUND : AnsiColor.GREEN_BACKGROUND;
        else
            ANSIColor = AnsiColor.YELLOW_BACKGROUND;

        return ANSIColor;
    }

    public void displayStarterCardBack(ResourceCard card) {
        System.out.println(displayResourcesStarter(card, 1, 2));
        System.out.println(AnsiColor.YELLOW_BACKGROUND + "  " + AnsiColor.BOLD + card.getId() + "   " + AnsiColor.RESET);
        System.out.println(displayResourcesStarter(card, 0, 3));
        System.out.println();
    }

    public String displayResourcesStarter(ResourceCard card, int index1, int index2) {
        String upResources = AnsiColor.YELLOW_BACKGROUND.toString();

        if (card.getBackCorners().containsKey(index1))
            if (!card.getBackCorners().get(index1).isEmpty())
                upResources += card.getBackCorners().get(index1).getResource().toString().charAt(0) + " ";
            else
                upResources += AnsiColor.WHITE_BACKGROUND + " " + AnsiColor.YELLOW_BACKGROUND + " ";
        else
            upResources += "  ";

        if (index1 == 1) {
            if (card.getBackResources().size() == 1)
                upResources += AnsiColor.YELLOW_BACKGROUND + " " + card.getBackResources().get(0).toString().charAt(0) + "  " + AnsiColor.RESET;
            else if (card.getBackResources().size() == 2)
                upResources += AnsiColor.YELLOW_BACKGROUND.toString() + card.getBackResources().get(0).toString().charAt(0) + card.getBackResources().get(1).toString().charAt(0) + "  " + AnsiColor.RESET;
            else
                upResources += AnsiColor.YELLOW_BACKGROUND.toString() + card.getBackResources().get(0).toString().charAt(0) + card.getBackResources().get(1).toString().charAt(0) + card.getBackResources().get(2).toString().charAt(0) + " " + AnsiColor.RESET;
        } else
            upResources += "    ";

        upResources += AnsiColor.YELLOW_BACKGROUND;
        if (card.getBackCorners().containsKey(index2))
            if (!card.getBackCorners().get(index2).isEmpty())
                upResources += card.getBackCorners().get(index2).getResource().toString().charAt(0);
            else
                upResources += AnsiColor.WHITE_BACKGROUND + " " + AnsiColor.RESET;
        else
            upResources += AnsiColor.YELLOW_BACKGROUND + " " + AnsiColor.RESET;

        upResources += AnsiColor.RESET;
        return upResources;
    }

    public void placeCard(Card card, Coordinate leftUpCorner) {
        if (leftUpCorner == null)
            leftUpCorner = new Coordinate(matrixDimension / 2 * cardWidth - 5, matrixDimension / 2 * cardHeight - 5);
        int x = leftUpCorner.getX();
        int y = leftUpCorner.getY();
        boolean whichCard = true;

        Card c;
        if (card instanceof ResourceCard) {
            whichCard = false;
            c = (ResourceCard) card;
        } else {
            c = (GoldCard) card;
        }

        String ANSIColor = getANSIColorForCard(c).toString();

        for (int i = 0; i < 7; i++) {
            gameBoard[y][x + i].setCard(c);
            if (c.isFrontSide())
                gameBoard[y][x + i].setCharacter(displayResourcesNoColor(card, 1, 2).charAt(i));
            gameBoard[y][x + i].setColor(ANSIColor);

            gameBoard[y + 1][x + i].setCard(c);
            gameBoard[y + 1][x + i].setColor(ANSIColor + AnsiColor.BOLD);

            gameBoard[y + 2][x + i].setCard(c);
            if (c.isFrontSide())
                gameBoard[y + 2][x + i].setCharacter(displayResourcesNoColor(card, 0, 3).charAt(i));
            gameBoard[y + 2][x + i].setColor(ANSIColor);
        }

        if (!whichCard) {
            ResourceCard resc = (ResourceCard) card;
            if (c.isFrontSide() && resc.getPoint() > 0)
                gameBoard[y][x + 3].setColor(AnsiColor.YELLOW + ANSIColor);
        } else {
            GoldCard gold = (GoldCard) card;
            if (gold.getPoint().getResource() != Resource.NO_RESOURCE)
                gameBoard[y][x + 3].setColor(AnsiColor.YELLOW + ANSIColor);
        }

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(0) && c.getFrontCorners().get(0).isEmpty()))
            gameBoard[y + 2][x].setColor(AnsiColor.WHITE_BACKGROUND.toString());

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(1) && c.getFrontCorners().get(1).isEmpty()))
            gameBoard[y][x].setColor(AnsiColor.WHITE_BACKGROUND.toString());

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(2) && c.getFrontCorners().get(2).isEmpty()))
            gameBoard[y][x + 6].setColor(AnsiColor.WHITE_BACKGROUND.toString());

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(3) && c.getFrontCorners().get(3).isEmpty()))
            gameBoard[y + 2][x + 6].setColor(AnsiColor.WHITE_BACKGROUND.toString());

        if (!c.isFrontSide()) {
            if (c.getBackResources().size() == 1)
                gameBoard[y][x + 3].setCharacter(c.getBackResources().get(0).toString().charAt(0));
            else if (c.getBackResources().size() == 2) {
                gameBoard[y][x + 2].setCharacter(c.getBackResources().get(0).toString().charAt(0));
                gameBoard[y][x + 3].setCharacter(c.getBackResources().get(1).toString().charAt(0));
            } else {
                gameBoard[y][x + 2].setCharacter(c.getBackResources().get(0).toString().charAt(0));
                gameBoard[y][x + 3].setCharacter(c.getBackResources().get(1).toString().charAt(0));
                gameBoard[y][x + 4].setCharacter(c.getBackResources().get(2).toString().charAt(0));
            }
        }

        // fix with card ids with multiple digits
        if (c.getId() > 9) {
            String idString = String.valueOf(c.getId());
            gameBoard[y + 1][x + 2].setCharacter(idString.charAt(0));
            gameBoard[y + 1][x + 3].setCharacter(idString.charAt(1));
        } else
            gameBoard[y + 1][x + 3].setCharacter((char) (c.getId() + '0'));
    }

    public String displayResourcesNoColor(Card card1, int index1, int index2) {
        Card card = (Card) card1;
        String upResources = "";

        if (card.getFrontCorners().containsKey(index1))
            if (!card.getFrontCorners().get(index1).isEmpty())
                upResources += card.getFrontCorners().get(index1).getResource().toString().charAt(0);
            else
                upResources += " ";
        else
            upResources += " ";

        if (card1 instanceof ResourceCard c) {
            if (index1 == 1 && c.getPoint() > 0)
                upResources += "  " + c.getPoint() + "  ";
            else
                upResources += "     ";
        } else if (card1 instanceof GoldCard c) {
            if (index1 == 1) {
                if (c.getPoint().getResource() != Resource.NO_RESOURCE)
                    upResources += " " + c.getPoint().getQta() + c.getPoint().getResource().toString().charAt(0) + "  ";
                else
                    upResources += "  " + c.getPoint().getQta() + "  ";
            } else if (index1 == 0) {
                if (c.getRequirements().size() == 1)
                    upResources += " " + c.getRequirements().getFirst().getQta() + c.getRequirements().getFirst().getResource().toString().charAt(0) + " ";
                else
                    for (Requirement r : c.getRequirements())
                        upResources += Integer.toString(r.getQta()) + r.getResource().toString().charAt(0);

                upResources += " ";
            }
        }

        if (card.getFrontCorners().containsKey(index2))
            if (!card.getFrontCorners().get(index2).isEmpty())
                upResources += card.getFrontCorners().get(index2).getResource().toString().charAt(0);
            else
                upResources += " ";
        else
            upResources += " ";

        return upResources;
    }

    public void displayBoard() {
        Card card;

        System.out.print("+");
        for (int i = 0; i < gameBoard[0].length; i++)
            System.out.print("-");
        System.out.println("+");

        for (int i = 0; i < gameBoard.length; i++) {
            System.out.print("|");
            for (int j = 0; j < gameBoard[i].length; j++) {
                card = gameBoard[i][j].getCard();
                if (card != null)
                    System.out.print(gameBoard[i][j].getColor() + gameBoard[i][j].getCharacter() + AnsiColor.RESET);
                else
                    System.out.print(gameBoard[i][j].getCharacter());
            }
            System.out.print("|");
            System.out.println(AnsiColor.RESET);
        }

        System.out.print("+");
        for (int i = 0; i < gameBoard[0].length; i++)
            System.out.print("-");
        System.out.println("+");
    }

    public void getBoardString(String asker) {
        Card card;
        StringBuilder toprint = new StringBuilder();
        toprint.append("+");
        for (int i = 0; i < gameBoard[0].length; i++)
            toprint.append("-");
        toprint.append("+\n");

        for (int i = 0; i < gameBoard.length; i++) {
            toprint.append("|");
            for (int j = 0; j < gameBoard[i].length; j++) {
                card = gameBoard[i][j].getCard();
                if (card != null)
                    toprint.append(gameBoard[i][j].getColor() + gameBoard[i][j].getCharacter() + AnsiColor.RESET);
                else
                    toprint.append(gameBoard[i][j].getCharacter());
            }
            toprint.append("|\n");
            toprint.append(AnsiColor.RESET);
        }

        toprint.append("+");
        for (int i = 0; i < gameBoard[0].length; i++)
            toprint.append("-");
        toprint.append("+");
        List<String> strings = new ArrayList<>();
        strings.add(toprint.toString());
        strings.add(asker);
        gameClient.sendMessage(new sendBoard("displayBoard", strings));
    }

    public CardSelection askCardSelection(PlayableCardIds ids, List<Card> hand) {
        for (Card card : hand) {
            if (ids.getPlayingHandIds().contains(card.getId()))
                displayCard(card);
            displayCardBack(card);
        }

        String message = "Inserisci l'id della carta che vuoi giocare (";
        for (int i = 0; i < ids.getPlayingHandIds().size(); i++) {
            message += ids.getPlayingHandIds().get(i) + ".f / " + ids.getPlayingHandIds().get(i) + ".b";
            if (i != ids.getPlayingHandIds().size() - 1)
                message += " / ";
        }

        if (!ids.getPlayingHandIdsBack().isEmpty())
            message += " / ";

        for (int i = 0; i < ids.getPlayingHandIdsBack().size(); i++) {
            message += ids.getPlayingHandIdsBack().get(i) + ".b";
            if (i != ids.getPlayingHandIdsBack().size() - 1)
                message += " / ";
        }

        message += "): ";
        System.out.print(message);

        boolean validInput = false;
        String input = "";
        while (!validInput) {
            input = scanner.nextLine();
            input = input.trim();

            try {
                // Verifica se l'input termina con ".b" e controlla la validità
                if (input.endsWith(".b")) {
                    int id = Integer.parseInt(input.substring(0, input.length() - 2)); // Rimuove ".b" e prova a convertire in int
                    if (ids.getPlayingHandIdsBack().contains(id) || ids.getPlayingHandIds().contains(id)) {
                        validInput = true; // L'ID è valido e nell'elenco idsBack
                    }
                } else if (input.endsWith(".f")) {
                    // Controlla se l'input è un ID valido in ids (senza ".b")
                    int id = Integer.parseInt(input.endsWith(".f") ? input.substring(0, input.length() - 2) : input); // Rimuove ".f" se presente e prova a convertire in int
                    if (ids.getPlayingHandIds().contains(id)) {
                        validInput = true; // L'ID è valido e nell'elenco ids
                    }
                }
            } catch (NumberFormatException e) {
                // Gestisce il caso in cui l'input non sia un numero
                validInput = false;
            }

            if (!validInput) {
                System.out.println("Input non valido. Riprova.");
                System.out.print(message); // Mostra nuovamente il messaggio di richiesta input
            }
        }

        String[] splitInput = input.split("\\.");

        return new CardSelection(Integer.parseInt(splitInput[0]), splitInput[1].equals("f"));
    }

    public void selectFromMenu() {
        System.out.println("Select an option: \n");
        System.out.println("\t1. Visualize messages\n");
        System.out.println("\t2. Send message\n");
        System.out.println("\t3. Continue with the game\n");
        System.out.println("\t4. Visualize scoreboard\n");
        System.out.println("\t5. Visualize other players codex\n");
        System.out.println("\t6. Exit\n");
        System.out.println("> ");
        String input;
        input = scanner.nextLine();
        switch (input) {
            case "1" -> {
                gameClient.getModelView().setMyUnreadedMessages(0);
                displayChat(gameClient.getModelView().getChat(), gameClient.getModelView().getMyUsername());
                gameClient.sendMessage(new DisplayMenu("displayMenu", null));
            }
            case "2" -> {
                System.out.print("Receiver ( All");
                for (String user : gameClient.getModelView().getPlayers())
                    System.out.print(" / " + user);
                System.out.print(" ): ");
                input = "";
                input = scanner.nextLine().trim();
                while (!input.equals("All") && !gameClient.getModelView().getPlayers().contains(input)) {
                    System.out.print("Invalid Input! Retry: ");
                    input = scanner.nextLine();
                }
                if(input.equals("All")) {
                    System.out.print("Write a message to all: ");
                    input = "";
                    input = scanner.nextLine();
                    Message m = new Message(input, gameClient.getModelView().getMyUsername());
                    gameClient.sendMessage(new messageBroadcast("messageToAll", m));
                }else {
                    System.out.print("Message to " + input + ": ");
                    String text = scanner.nextLine();
                    MessagePrivate m = new MessagePrivate(text, gameClient.getModelView().getMyUsername(), input);
                    gameClient.sendMessage(new messagePrivate("messageToUser", m));
                }
            }
            case "3" -> {
                if (gameClient.getModelView().getMyUnreadedMessages() > 0)
                    System.out.println("New message received! You have not read it yet\n");

                if (!gameClient.getModelView().isMyTurn())
                    System.out.println("Wait for your turn...\n");
            }
            case "4" -> {
                gameClient.sendMessage(new DisplayScoreboard("displayScoreboard", null));
                gameClient.sendMessage(new DisplayMenu("displayMenu", null));
            }
            case "5" -> {
                System.out.println("Choose the player: ");
                for (String player : gameClient.getModelView().getPlayers()) {
                    if (!player.equals(gameClient.getModelView().getMyUsername()))
                        System.out.println(player + ", ");
                }
                input = "";
                input= scanner.nextLine();
                while (!gameClient.getModelView().getPlayers().contains(input) || input.equals(gameClient.getModelView().getMyUsername())) {
                    System.out.print("Invalid Input! Retry: ");
                    input = scanner.nextLine();
                }
                List<String> usernames = new ArrayList<>();
                usernames.add(input);
                usernames.add(gameClient.getModelView().getMyUsername());
                gameClient.sendMessage(new DisplayCodex("displayCodex", usernames));
                //gameClient.sendMessage(new DisplayMenu("displayMenu", null));
            }
            case "6" -> {

            }
        }
    }

    public void displayScoreboard(Map<String, Integer> scoreboard) {
        System.out.println("Scoreboard:\n");
        for (String player : gameClient.getModelView().getPlayers()) {
            int score = scoreboard.get(player);
            System.out.print(player + ": " +score + " ");
            for (int i = 0; i < score; i++) {
                System.out.print("■");
            }
            System.out.println("\n");
        }
        System.out.println();
    }


    public void displayChat(Chat chat, String username) {
        System.out.println();
        for (Message m : chat.getMsgs()) {
            if (m instanceof MessagePrivate) {
                MessagePrivate mPrivate = (MessagePrivate) m;
                if (mPrivate.getSender().equals(username))
                    System.out.println(m.getTime().getHour() + ":" + m.getTime().getMinute() + " " + " [private] you: " + m.getText());
                else
                    System.out.println(m.getTime().getHour() + ":" + m.getTime().getMinute() + " " + " [private] " + m.getSender() + ": " + m.getText());
            } else {
                System.out.println(m.getTime().getHour() + ":" + m.getTime().getMinute() + " " + m.getSender() + ": " + m.getText());
            }
        }
        System.out.println();
    }

    public void currentTurn(PlayableCardIds ids) {
        System.out.println("It's your turn!\n");
        CardSelection cs = askCardSelection(ids, gameClient.getModelView().getMyHand());

        gameClient.getModelView().setMyPlayingCard(gameClient.getModelView().getMyHand().stream().filter(c -> c.getId() == cs.getId()).findFirst().orElse(null));
        gameClient.getModelView().getMyHand().remove(gameClient.getModelView().getMyPlayingCard());

        gameClient.sendMessage(new CardSelectedMessage("cardSelection", cs));
    }

    @Override
    public void updateDecks(List<Card> updatedDecks) {

    }

    @Override
    public void updateMyPlayerState() {
        // only for GUI
    }

    @Override
    public void updatePlayerState(String player) {
        // only for GUI
    }

    @Override
    public void lastTurn() {
        System.out.println("Last turn, play carefully!\n");
    }

    @Override
    public void endGame(List<Pair<String, Integer>> data) {
        System.out.println("Game over!\nResults:\n");
        for (int i = 1; i < data.size()+1; i++) {
            System.out.println("#" + i + " " +data.get(i-1).getKey() + " Points: " + data.get(i-1).getValue());
        }
    }

    public void showNotYourTurn() {
        gameClient.getModelView().setMyTurn(false);
        // display message

        if (gameClient.getModelView().getMyUnreadedMessages() > 0)
            System.out.println("\nThere are " + gameClient.getModelView().getMyUnreadedMessages() + " messages you have not read\n\n");
        selectFromMenu();
    }

    public String displayAngle(List<Coordinate> angles) {
        String out = "Seleziona la carta e l'angolo (";
        for (Coordinate angle : angles) {
            out += angle.getX() + "." + angle.getY();
            if (angles.indexOf(angle) != angles.size() - 1)
                out += " / ";
        }

        System.out.print(out + "): ");

        // continue to ask for input until the input is valid and the angle is in the list
        String input = scanner.nextLine();
        while (!input.matches("\\d+\\.\\d+") || !angles.contains(new Coordinate(Integer.parseInt(input.split("\\.")[0]), Integer.parseInt(input.split("\\.")[1])))) {
            System.out.print("Input non valido, riprova: ");
            input = scanner.nextLine();
        }

        return input;
    }

    public Coordinate placeBottomRight(Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeTopRight(Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() + cardWidth - 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeBottomLeft(Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() + cardHeight - 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public Coordinate placeTopLeft(Card card, Card cardToPlace) {
        Coordinate leftUpCorner;
        leftUpCorner = new Coordinate(card.getCentre().getX() - cardWidth + 1,
                card.getCentre().getY() - cardHeight + 1);
        cardToPlace.setCentre(leftUpCorner);

        return leftUpCorner;
    }

    public void visualizeStarterCard(Card card) {
        displayCard(card);
        displayStarterCardBack((ResourceCard) card);
    }

    public void setStarterSide() {
        System.out.println("Choose front side or back side of starter card (f/b): ");
        String input = scanner.nextLine();
        while (!input.equals("f") && !input.equals("b")) {
            System.out.print("Invalid Input! Retry: ");
            input = scanner.nextLine();
        }

        gameClient.getModelView().getMyStarterCard().setSide(input.equals("f"));
        placeCard(gameClient.getModelView().getMyStarterCard(), null);
        displayBoard();

        gameClient.sendMessage(new StarterSideSelectedMessage("starterSideSelected", input.equals("f")));
    }

    public void displayCommonObjective(List<Objective> objectives) {
        System.out.println("Game's Common objectives:\n");
        for (Objective objective : objectives) {
            // objective.displayCard();
            // TODO: Fix and implement displayObjective method
            System.out.println(objective.getId());
        }
        System.out.println("\n");
    }

    public void chooseObjective(List<Objective> objectives) {
        System.out.println("Secret objectives received:");
        System.out.println();
        for (Objective objective : objectives) {
            // TODO: Fix and implement displayObjective method
            System.out.println(objective.getId());
        }
        System.out.println("Choose an objective card to keep: ");
        List<Integer> ids = new ArrayList<>();
        for (Objective objective : objectives) {
            ids.add(objective.getId());
        }

        int cardId = scanner.nextInt();
        while (!ids.contains(cardId)) {
            System.out.println("Invalid Input! Retry: ");
            cardId = scanner.nextInt();
        }
        scanner.nextLine();
        // get card from objective list given the id
        int finalCardId = cardId;

        gameClient.sendMessage(new SelectedObjMessage("chooseSecretObjective", objectives.stream().filter(o -> o.getId() == finalCardId).findFirst().orElse(null)));
        // return objectives.stream().filter(o -> o.getId() == finalCardId).findFirst().orElse(null);
    }

    public void displayHand(List<Card> hand) {
        System.out.println("Your hand:\n");
        for (Card card : hand) {
            displayCard(card);
            displayCardBack(card);
        }
    }

    public void place(Card cardToPlay, Card targetCard, int cornerSelected) {
        if (cornerSelected == 0)
            placeCard(cardToPlay, placeBottomLeft(targetCard, cardToPlay));
        else if (cornerSelected == 1)
            placeCard(cardToPlay, placeTopLeft(targetCard, cardToPlay));
        else if (cornerSelected == 2)
            placeCard(cardToPlay, placeTopRight(targetCard, cardToPlay));
        else
            placeCard(cardToPlay, placeBottomRight(targetCard, cardToPlay));

    }

    public void askWhereToDraw(List<Card> cards) {
        List<Integer> ids = new ArrayList<>();
        String m = "(";
        for (Card c : cards) {
            displayCard(c);
            displayCardBack(c);
            ids.add(c.getId());
            m += c.getId() + " / ";
        }
        String mex = "Vuoi perscare una di queste carte o vuoi pescare da A (Resource) o da B (Gold)? ";
        mex += m.substring(0, m.length() - 3) + " / A / B): ";
        System.out.println(mex);

        String input = scanner.nextLine();

        try {
            while (!input.equals("A") && !input.equals("B") && !ids.contains(Integer.parseInt(input))) {
                System.out.print("Input non valido, riprova: ");
                input = scanner.nextLine();
            }
        } catch (NumberFormatException e) {
            System.out.print("Input non valido, riprova: ");
            input = scanner.nextLine();
        }

        gameClient.sendMessage(new SelectedDrewCard("drawCard", input));
    }

    public void displayPawn(Color pawn) {
        String red = "\033[91m";
        String green = "\033[92m";
        String yellow = "\033[93m";
        String blue = "\033[94m";
        String reset = "\033[0m";
        String colorAnsi = "";
        switch (pawn) {
            case RED -> colorAnsi = red;
            case GREEN -> colorAnsi = green;
            case YELLOW -> colorAnsi = yellow;
            case BLUE -> colorAnsi = blue;
        }
        String cube = colorAnsi + "█";
        String printPawn = String.format(
                "%s%s%s%s%s%s\n", cube, cube, cube, cube, cube, reset);
        System.out.print(printPawn);
        System.out.println("Pawn assigned: " + pawn);
    }

    public String askUsername() {
        System.out.println("Insert your username: ");
        String username = scanner.nextLine().trim();
        return username;
    }

    public String askJoinCreate() {
        String input = scanner.nextLine();
        while (!input.equals("join") && !input.equals("create")) {
            System.out.println("Invalid Input! Retry: ");
            input = scanner.nextLine();
        }
        return input;
    }

    public String askGameId(String joinCreate, String gameIds) {
        String gameId = "";
        if( joinCreate.equals("join")) {
            System.out.println("Game available: " + gameIds);
            System.out.println("Insert the game id to join: ");
            gameId = scanner.nextLine().trim();
            while(!gameIds.contains(gameId)) {
                System.out.println("Invalid Input! Retry: ");
                gameId = scanner.nextLine().trim();
            }
        } else if(joinCreate.equals("create")) {
            System.out.println("Insert the game id: ");
            gameId = scanner.nextLine().trim();
        }
        return gameId;
    }

    public int askNumberOfPlayers() {
        System.out.println("Insert the number of players (2-4): ");
        int numPlayers = scanner.nextInt();
        while (numPlayers < 2 || numPlayers > 4) {
            System.out.println("Invalid Input! Retry: ");
            numPlayers = scanner.nextInt();
        }
        scanner.nextLine();
        return numPlayers;
    }

    public void showAvailableAngles(List<Coordinate> angles) {
        String input = displayAngle(angles);

        // get card from player's hand by id
        // TODO: create object for handling card selection
        String[] splitCardToPlay = input.split("\\.");
        int cardToAttachId = Integer.parseInt(splitCardToPlay[0]);

        // card where to attach the selected card
        Card targetCard = gameClient.getModelView().getMyCodex().stream().filter(c -> c.getId() == cardToAttachId).findFirst().orElse(null);
        place(gameClient.getModelView().getMyPlayingCard(), targetCard, Integer.parseInt(splitCardToPlay[1]));
        gameClient.getModelView().addCardToCodex(gameClient.getModelView().getMyPlayingCard());

        displayBoard();

        gameClient.sendMessage(new AngleSelectedMessage("angleSelection", new CardToAttachSelected(input, gameClient.getModelView().getMyCodex())));
    }

    public void displayPersonalResources(Map<Resource, Integer> resources) {
        System.out.println("Your resources:\n");
        for (Map.Entry<Resource, Integer> entry : resources.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println();
    }

    @Override
    public void setClient(ClientAbstract client) {

    }

    @Override
    public void setViewModel(ModelView modelView) {

    }
}