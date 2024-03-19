package it.polimi.ingsw.gc38.view;

import java.util.List;
import java.util.Scanner;

import it.polimi.ingsw.gc38.model.*;

public class CliView {
    private Scanner scanner;

    private final String ANSI_BLUE_BACKGROUND;
    private final String ANSI_RESET;
    private final String ANSI_RED_BACKGROUND;
    private final String ANSI_YELLOW;
    private final String ANSI_WHITE_BACKGROUND;
    private final String ANSI_PURPLE_BACKGROUND;
    private final String ANSI_GREEN_BACKGROUND;

    public CliView() {
        this.scanner = new Scanner(System.in);

        this.ANSI_BLUE_BACKGROUND = "\u001B[44m";
        this.ANSI_RED_BACKGROUND = "\u001B[41m";
        this.ANSI_YELLOW = "\u001B[33m";
        this.ANSI_WHITE_BACKGROUND = "\u001B[47m";
        this.ANSI_PURPLE_BACKGROUND = "\u001B[45m";
        this.ANSI_GREEN_BACKGROUND = "\u001B[48;5;34m";
        this.ANSI_RESET = "\u001B[0m";

    }

    public String askForNickname() {
        System.out.print("Inserisci nickname: ");
        return scanner.nextLine();
    }

    public void welcomeMessage() {
        System.out.println("Benvenuto in Codex Naturalis!\n");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayStarterCard(StarterCard card) {
        String blankSpace = "                    ";

        String upResources = "| " + card.getFrontCorners().get(1).getResource().toString().charAt(0) + "     "
                + card.getFrontCorners().get(2).getResource().toString().charAt(0) + " |";
        upResources += blankSpace + "| ";
        upResources += card.getBackCorners().containsKey(1) && !card.getBackCorners().get(1).isEmpty()
                ? card.getBackCorners().get(1).getResource().toString().charAt(0)
                : " ";
        upResources += "     ";
        upResources += card.getBackCorners().containsKey(2) && !card.getBackCorners().get(2).isEmpty()
                ? card.getBackCorners().get(2).getResource().toString().charAt(0)
                : " ";
        upResources += " |";

        String downResources = "| " + card.getFrontCorners().get(0).getResource().toString().charAt(0) + "     "
                + card.getFrontCorners().get(3).getResource().toString().charAt(0) + " |";
        downResources += blankSpace + "| ";
        downResources += card.getBackCorners().containsKey(0) && !card.getBackCorners().get(0).isEmpty()
                ? card.getBackCorners().get(0).getResource().toString().charAt(0)
                : " ";
        downResources += "     ";
        downResources += card.getBackCorners().containsKey(3) && !card.getBackCorners().get(3).isEmpty()
                ? card.getBackCorners().get(3).getResource().toString().charAt(0)
                : " ";
        downResources += " |";

        String centralResources = "";
        if (card.getBackResources().size() == 1) {
            centralResources += "|    o    |" + blankSpace + "|         |\n";
            centralResources += "|   o o   |" + blankSpace + "|  o "
                    + card.getBackResources().get(0).toString().charAt(0) + " o  |\n";
            centralResources += "|    o    |" + blankSpace + "|         |";
        } else if (card.getBackResources().size() == 2) {
            centralResources += "|    o    |" + blankSpace + "|    "
                    + card.getBackResources().get(1).toString().charAt(0) + "    |\n";
            centralResources += "|   o o   |" + blankSpace + "|   o o   |\n";
            centralResources += "|    o    |" + blankSpace + "|    "
                    + card.getBackResources().get(0).toString().charAt(0) + "    |";
        } else {
            centralResources += "|    o    |" + blankSpace + "|    "
                    + card.getBackResources().get(2).toString().charAt(0) + "    |\n";
            centralResources += "|   o o   |" + blankSpace + "|  o "
                    + card.getBackResources().get(1).toString().charAt(0) + " o  |\n";
            centralResources += "|    o    |" + blankSpace + "|    "
                    + card.getBackResources().get(0).toString().charAt(0) + "    |";
        }

        System.out.println("+---------+" + blankSpace + "+---------+");
        System.out.println(upResources);
        System.out.println(centralResources);
        System.out.println(downResources);
        System.out.println("+---------+" + blankSpace + "+---------+");
    }

    public Boolean askForSide() {
        System.out.print("Ecco la carta estratta, vuoi visualizzare il front o il back? (f/b): ");

        return scanner.nextLine().equals("f") ? true : false;
    }

    public CardCornerInput askForCardToPlay(List<Integer> ids) {
        String message = "Inserisci l'id della carta che vuoi giocare (";
        for (int i = 0; i < ids.size(); i++) {
            message += ids.get(i) + ".F / " + ids.get(i) + ".B";
            if (i != ids.size() - 1)
                message += " / ";
        }
        message += "): ";
        System.out.print(message);

        String input = scanner.nextLine();
        // string split to get the id of the card to play
        String[] splittedInput = input.split("\\.");

        CardCornerInput res = new CardCornerInput(Integer.parseInt(splittedInput[0]), splittedInput[1].equals("F") ? true : false);
        return res;
    }

    public int askForCornerToPlay(List<Integer> cornersKey) {
        String out = "Inserisci l'angolo in cui vuoi giocare la carta (";
        for (int i = 0; i < cornersKey.size(); i++) {
            out += cornersKey.get(i);
            if (i != cornersKey.size() - 1)
                out += " / ";
        }
        out += "): ";
        System.out.print(out);
        return Integer.parseInt(scanner.nextLine());
    }

    public String askForCardToAttach(String out) {
        System.out.print(out);
        return scanner.nextLine();
    }

    public String displayResources(ResourceCard card, int index1, int index2) {
        String ANSIColor = "";
        if (card.getColor() == Color.RED)
            ANSIColor = ANSI_RED_BACKGROUND;
        else if (card.getColor() == Color.BLUE)
            ANSIColor = ANSI_BLUE_BACKGROUND;
        else if (card.getColor() == Color.PURPLE)
            ANSIColor = ANSI_PURPLE_BACKGROUND;
        else if (card.getColor() == Color.GREEN)
            ANSIColor = ANSI_GREEN_BACKGROUND;

        String upResources = ANSIColor;

        if (card.getFrontCorners().containsKey(index1))
            if (!card.getFrontCorners().get(index1).isEmpty())
                upResources += card.getFrontCorners().get(index1).getResource().toString().charAt(0) + " ";
            else
                upResources += ANSI_WHITE_BACKGROUND + " " + ANSIColor + " ";
        else
            upResources += "  ";

        upResources += " ";

        if (index1 == 1 && card.getPoint() > 0)
            upResources += ANSI_YELLOW + card.getPoint() + "  " + ANSI_RESET;
        else
            upResources += "   ";

        upResources += ANSIColor;
        if (card.getFrontCorners().containsKey(index2)) {
            if (!card.getFrontCorners().get(index2).isEmpty()) {
                upResources += card.getFrontCorners().get(index2).getResource().toString().charAt(0);
            } else {
                upResources += ANSI_WHITE_BACKGROUND + " " + ANSI_RESET; // |_
            }
        } else {
            // if (index1 == 1)
            upResources += ANSIColor + " " + ANSI_RESET;
            // else
            // upResources += ANSI_BLUE_BACKGROUND + "__" + ANSI_RESET;
        }

        upResources += ANSI_RESET;
        return upResources;
    }

    public String displayResourcesNoColor(ResourceCard card, int index1, int index2) {
        String upResources = "";

        if (card.getFrontCorners().containsKey(index1))
            if (!card.getFrontCorners().get(index1).isEmpty())
                upResources += card.getFrontCorners().get(index1).getResource().toString().charAt(0) + " ";
            else
                upResources += "  ";
        else
            upResources += "  ";

        upResources += " ";

        if (index1 == 1 && card.getPoint() > 0)
            upResources += card.getPoint() + "  ";
        else
            upResources += "   ";

        if (card.getFrontCorners().containsKey(index2))
            if (!card.getFrontCorners().get(index2).isEmpty())
                upResources += card.getFrontCorners().get(index2).getResource().toString().charAt(0);
            else
                upResources += "  ";
        else
            upResources += "  ";

        return upResources;
    }

    public void displayBoard(Cell[][] matrixBoard) {
        final String ANSI_RESET = "\u001B[0m";

        ResourceCard card;
        for (int i = 0; i < matrixBoard.length; i++) {
            for (int j = 0; j < matrixBoard[i].length; j++) {
                card = (ResourceCard) matrixBoard[i][j].getCard();
                if (card != null)
                    System.out.print(matrixBoard[i][j].getColor() + matrixBoard[i][j].getCharacter() + ANSI_RESET);
                else
                    System.out.print(matrixBoard[i][j].getCharacter());
            }
            System.out.println(ANSI_RESET);
        }
    }

    public void displayResourceCard(ResourceCard card) {
        String ANSIColor = "";

        if (card.getColor() == Color.RED)
            ANSIColor = ANSI_RED_BACKGROUND;
        else if (card.getColor() == Color.BLUE)
            ANSIColor = ANSI_BLUE_BACKGROUND;
        else if (card.getColor() == Color.PURPLE)
            ANSIColor = ANSI_PURPLE_BACKGROUND;
        else if (card.getColor() == Color.GREEN)
            ANSIColor = ANSI_GREEN_BACKGROUND;

        System.out.println(displayResources(card, 1, 2));
        if (card.getId() > 9)
            System.out.println(ANSIColor + "  " + card.getId() + "   " + ANSI_RESET);
        else
            System.out.println(ANSIColor + "   " + card.getId() + "   " + ANSI_RESET);
        System.out.println(displayResources(card, 0, 3));

        System.out.println();
    }

    public void displayResourceCardBack(ResourceCard card) {
        final String ANSI_RESET = "\u001B[0m";
        final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
        final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

        System.out.println(ANSI_WHITE_BACKGROUND + " " + ANSI_BLUE_BACKGROUND + "  " + card.getBackResources().get(0).toString().charAt(0) + "  " + ANSI_WHITE_BACKGROUND + " ");
        System.out.println(ANSI_BLUE_BACKGROUND + "   " + card.getId() + "   ");
        System.out.println(ANSI_WHITE_BACKGROUND + " " + ANSI_BLUE_BACKGROUND + "     " + ANSI_WHITE_BACKGROUND + " " + ANSI_RESET);

        System.out.println();
    }

    public void placeCard(Cell[][] matrixBoard, ResourceCard card, Coordinate leftUpCorner) {
        int x = leftUpCorner.getX();
        int y = leftUpCorner.getY();
        String ANSIColor = "";

        if (card.getColor() == Color.RED)
            ANSIColor = ANSI_RED_BACKGROUND;
        else if (card.getColor() == Color.BLUE)
            ANSIColor = ANSI_BLUE_BACKGROUND;
        else if (card.getColor() == Color.PURPLE)
            ANSIColor = ANSI_PURPLE_BACKGROUND;
        else if (card.getColor() == Color.GREEN)
            ANSIColor = ANSI_GREEN_BACKGROUND;

        for (int i = 0; i < 7; i++) {
            matrixBoard[y][x + i].setCard(card);
            if (card.isFrontSide())
                matrixBoard[y][x + i]
                        .setCharacter(displayResourcesNoColor(card, 1, 2).charAt(i));
            matrixBoard[y][x + i].setColor(ANSIColor);

            matrixBoard[y + 1][x + i].setCard(card);
            matrixBoard[y + 1][x + i].setColor(ANSIColor);

            matrixBoard[y + 2][x + i].setCard(card);
            if (card.isFrontSide())
                matrixBoard[y + 2][x + i]
                        .setCharacter(displayResourcesNoColor(card, 0, 3).charAt(i));
            matrixBoard[y + 2][x + i].setColor(ANSIColor);
        }

        if (card.isFrontSide() && card.getPoint() > 0)
            matrixBoard[y][x + 3].setColor(ANSI_YELLOW + ANSIColor);

        if (!card.isFrontSide() || (card.getFrontCorners().containsKey(0) && card.getFrontCorners().get(0).isEmpty()))
            matrixBoard[y + 2][x].setColor(ANSI_WHITE_BACKGROUND);

        if (!card.isFrontSide() || (card.getFrontCorners().containsKey(1) && card.getFrontCorners().get(1).isEmpty()))
            matrixBoard[y][x].setColor(ANSI_WHITE_BACKGROUND);

        if (!card.isFrontSide() || (card.getFrontCorners().containsKey(2) && card.getFrontCorners().get(2).isEmpty()))
            matrixBoard[y][x + 6].setColor(ANSI_WHITE_BACKGROUND);

        if (!card.isFrontSide() || (card.getFrontCorners().containsKey(3) && card.getFrontCorners().get(3).isEmpty()))
            matrixBoard[y + 2][x + 6].setColor(ANSI_WHITE_BACKGROUND);

        if (!card.isFrontSide())
            matrixBoard[y][x + 3].setCharacter(card.getBackResources().get(0).toString().charAt(0));

        // fix with card ids with multiple digits
        if (card.getId() > 9) {
            String idString = String.valueOf(card.getId());
            matrixBoard[y + 1][x + 2].setCharacter(idString.charAt(0));
            matrixBoard[y + 1][x + 3].setCharacter(idString.charAt(1));
        } else
            matrixBoard[y + 1][x + 3].setCharacter((char) (card.getId() + '0'));

        // convert int to string and get the first character

    }

    public void printMatrixTest(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++)
                System.out.print(matrix[i][j] + " ");
            System.out.println();
        }
    }
}
