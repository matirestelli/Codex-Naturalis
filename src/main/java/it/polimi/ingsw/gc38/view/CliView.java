package it.polimi.ingsw.gc38.view;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import it.polimi.ingsw.gc38.model.*;

public class CliView {
    private Scanner scanner;

    private final String ANSI_BLUE_BACKGROUND;

    private final String ANSI_GOLD_BLUE_BACKGROUND;
    private final String ANSI_RESET;
    private final String ANSI_RED_BACKGROUND;
    private final String ANSI_YELLOW;
    private final String ANSI_WHITE_BACKGROUND;
    private final String ANSI_PURPLE_BACKGROUND;
    private final String ANSI_GREEN_BACKGROUND;
    private final String ANSI_YELLOW_BACKGROUND;

    private final String ANSI_GOLD_RED_BACKGROUND;

    public CliView() {
        this.scanner = new Scanner(System.in);

        this.ANSI_BLUE_BACKGROUND = "\u001B[44m";
        this.ANSI_GOLD_BLUE_BACKGROUND = "\u001B[48;5;18m";
        this.ANSI_RED_BACKGROUND = "\u001B[41m";
        this.ANSI_GOLD_RED_BACKGROUND = "\u001B[48;5;214m";
        this.ANSI_YELLOW = "\u001B[33m";
        this.ANSI_WHITE_BACKGROUND = "\u001B[47m";
        this.ANSI_PURPLE_BACKGROUND = "\u001B[45m";
        this.ANSI_GREEN_BACKGROUND = "\u001B[48;5;34m";
        this.ANSI_YELLOW_BACKGROUND = "\u001B[48;5;179m";
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

    public void displayStarterCard(Card card) {
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
        System.out.print("Ecco la carta estratta, vuoi posizionarla visualizzando il front o il back? (f/b): ");
        // continue to ask for input until the input is valid
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("f") || input.equals("b"))
                return input.equals("f");
            else
                System.out.print("Input non valido, riprova: ");
        }
    }

    public CardCornerInput askForCardToPlay(List<Integer> ids) {
        String message = "Inserisci l'id della carta che vuoi giocare (";
        for (int i = 0; i < ids.size(); i++) {
            message += ids.get(i) + ".f / " + ids.get(i) + ".b";
            if (i != ids.size() - 1)
                message += " / ";
        }
        message += "): ";
        System.out.print(message);

        String input = scanner.nextLine();
        // continue to ask for input until the input is valid and the card is in the list
        while (!input.matches("\\d+\\.(f|b)") || !ids.contains(Integer.parseInt(input.split("\\.")[0]))) {
            System.out.print("Input non valido, riprova: ");
            input = scanner.nextLine();
        }

        // string split to get the id of the card to play
        String[] splitInput = input.split("\\.");

        return new CardCornerInput(Integer.parseInt(splitInput[0]), splitInput[1].equals("f"));
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

    public String displayResources(Card card, int index1, int index2, String ANSIColor) {
        String upResources = ANSIColor;

        if (card.getFrontCorners().containsKey(index1))
            if (!card.getFrontCorners().get(index1).isEmpty())
                upResources += card.getFrontCorners().get(index1).getResource().toString().charAt(0) + " ";
            else
                upResources += ANSI_WHITE_BACKGROUND + " " + ANSIColor + " ";
        else
            upResources += "  ";

        upResources += " ";

        if (card instanceof ResourceCard c) {
            if (index1 == 1 && c.getPoint() > 0)
                upResources += ANSI_YELLOW + c.getPoint() + "  " + ANSI_RESET;
            else
                upResources += "   ";
        } else if (card instanceof GoldCard c) {
            if (index1 == 1) {
                if (c.getPoint().getResource() != Resource.NO_RESOURCE)
                    upResources += ANSI_YELLOW + c.getPoint().getQta() + c.getPoint().getResource().toString().charAt(0) + " " + ANSI_RESET;
                else
                    upResources += ANSI_YELLOW + c.getPoint() + "  " + ANSI_RESET;
            }
        }

        upResources += ANSIColor;
        if (card.getFrontCorners().containsKey(index2))
            if (!card.getFrontCorners().get(index2).isEmpty())
                upResources += card.getFrontCorners().get(index2).getResource().toString().charAt(0);
            else
                upResources += ANSI_WHITE_BACKGROUND + " " + ANSI_RESET;
        else
            upResources += ANSIColor + " " + ANSI_RESET;

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

    public void displayResourceCard(ColoredCard card) {
        Card c;
        if (card instanceof ResourceCard) {
            c = (ResourceCard) card;
        } else {
            c = (GoldCard) card;
        }

        String ANSIColor = "";
        if (card.getColor() == Color.RED) {
            if (card instanceof GoldCard)
                ANSIColor = ANSI_GOLD_RED_BACKGROUND;
            else
                ANSIColor = ANSI_RED_BACKGROUND;
        }
        else if (card.getColor() == Color.BLUE) {
            if (card instanceof GoldCard) {
                ANSIColor = ANSI_GOLD_BLUE_BACKGROUND;
                System.out.println("ciao");
            }
            else
                ANSIColor = ANSI_BLUE_BACKGROUND;
        }
        else if (card.getColor() == Color.PURPLE)
            ANSIColor = ANSI_PURPLE_BACKGROUND;
        else if (card.getColor() == Color.GREEN)
            ANSIColor = ANSI_GREEN_BACKGROUND;
        else
            ANSIColor = ANSI_YELLOW_BACKGROUND;

        System.out.println(displayResources(c, 1, 2, ANSIColor));
        if (c.getId() > 9)
            System.out.println(ANSIColor + "  " + c.getId() + "   " + ANSI_RESET);
        else
            System.out.println(ANSIColor + "   " + c.getId() + "   " + ANSI_RESET);
        System.out.println(displayResources(c, 0, 3, ANSIColor));

        System.out.println();
    }

    public void displayResourceCardBack(ResourceCard card) {
        String ANSIColor = "";

        if (card.getColor() == Color.RED)
            ANSIColor = ANSI_RED_BACKGROUND;
        else if (card.getColor() == Color.BLUE)
            ANSIColor = ANSI_BLUE_BACKGROUND;
        else if (card.getColor() == Color.PURPLE)
            ANSIColor = ANSI_PURPLE_BACKGROUND;
        else if (card.getColor() == Color.GREEN)
            ANSIColor = ANSI_GREEN_BACKGROUND;
        else
            ANSIColor = ANSI_YELLOW_BACKGROUND;

        System.out.println(ANSI_WHITE_BACKGROUND + " " + ANSIColor + "  " + card.getBackResources().get(0).toString().charAt(0) + "  " + ANSI_WHITE_BACKGROUND + " " + ANSI_RESET);
        if (card.getId() > 9)
            System.out.println(ANSIColor + "  " + card.getId() + "   " + ANSI_RESET);
        else
            System.out.println(ANSIColor + "   " + card.getId() + "   " + ANSI_RESET);
        System.out.println(ANSI_WHITE_BACKGROUND + " " + ANSIColor + "     " + ANSI_WHITE_BACKGROUND + " " + ANSI_RESET);

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
        else
            ANSIColor = ANSI_YELLOW_BACKGROUND;

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

        if (!card.isFrontSide()) {
            if (card.getBackResources().size() == 1)
                matrixBoard[y][x + 3].setCharacter(card.getBackResources().get(0).toString().charAt(0));
            else if (card.getBackResources().size() == 2) {
                matrixBoard[y][x + 2].setCharacter(card.getBackResources().get(0).toString().charAt(0));
                matrixBoard[y][x + 3].setCharacter(card.getBackResources().get(1).toString().charAt(0));
            } else {
                matrixBoard[y][x + 2].setCharacter(card.getBackResources().get(0).toString().charAt(0));
                matrixBoard[y][x + 3].setCharacter(card.getBackResources().get(1).toString().charAt(0));
                matrixBoard[y][x + 4].setCharacter(card.getBackResources().get(2).toString().charAt(0));
            }
        }

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

    public String displayResourcesStarter(ResourceCard card, int index1, int index2) {
        String upResources = ANSI_YELLOW_BACKGROUND;

        if (card.getBackCorners().containsKey(index1))
            if (!card.getBackCorners().get(index1).isEmpty())
                upResources += card.getBackCorners().get(index1).getResource().toString().charAt(0) + " ";
            else
                upResources += ANSI_WHITE_BACKGROUND + " " + ANSI_YELLOW_BACKGROUND + " ";
        else
            upResources += "  ";

        // upResources += " ";

        if (index1 == 1)
            upResources += card.getId() + "  ";
        else
            upResources += "    ";

        upResources += ANSI_YELLOW_BACKGROUND;
        if (card.getBackCorners().containsKey(index2))
            if (!card.getBackCorners().get(index2).isEmpty())
                upResources += card.getBackCorners().get(index2).getResource().toString().charAt(0);
            else
                upResources += ANSI_WHITE_BACKGROUND + " " + ANSI_RESET;
        else
            upResources += ANSI_YELLOW_BACKGROUND + " " + ANSI_RESET;

        upResources += ANSI_RESET;
        return upResources;
    }

    public void displayStarterCardBack(ResourceCard card) {
        System.out.println(displayResourcesStarter(card, 1, 2));
        if (card.getBackResources().size() == 1)
            System.out.println(ANSI_YELLOW_BACKGROUND + "   " + card.getBackResources().get(0).toString().charAt(0) + "   " + ANSI_RESET);
        else if (card.getBackResources().size() == 2)
            System.out.println(ANSI_YELLOW_BACKGROUND + "  " + card.getBackResources().get(0).toString().charAt(0) + card.getBackResources().get(1).toString().charAt(0) + "   " + ANSI_RESET);
        else
            System.out.println(ANSI_YELLOW_BACKGROUND + "  " + card.getBackResources().get(0).toString().charAt(0) + card.getBackResources().get(1).toString().charAt(0) + card.getBackResources().get(2).toString().charAt(0) +  "  " + ANSI_RESET);
        System.out.println(displayResourcesStarter(card, 0, 3));

        System.out.println();
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
        while (!input.matches("\\d+\\.\\d+") ||  !angles.contains(new Coordinate(Integer.parseInt(input.split("\\.")[0]), Integer.parseInt(input.split("\\.")[1])))) {
            System.out.print("Input non valido, riprova: ");
            input = scanner.nextLine();
        }

        return input;
    }

    public void displayPersonalResources(Map<Resource, Integer> resources) {
        System.out.println("Risorse personali:");
        for (Resource res : Resource.values()) {
            System.out.println(res.toString() + ": " + resources.get(res));
        }
    }
}
