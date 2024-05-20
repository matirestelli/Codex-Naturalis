package it.polimi.ingsw.ui;

import it.polimi.ingsw.clientmodel.Cell;
import it.polimi.ingsw.core.model.*;
import it.polimi.ingsw.core.model.enums.Color;
import it.polimi.ingsw.core.model.enums.Resource;

import java.util.List;
import java.util.Scanner;

public class TextUserInterface implements UserInterfaceStrategy {
    private Scanner scanner = new Scanner(System.in);
    private int cardWidth = 7;
    private int cardHeight = 3;
    private int matrixDimension = 10;
    private Cell[][] gameBoard;

    @Override
    public void initialize() {
        System.out.println("Text User Interface initialized!");
        this.gameBoard = new Cell[matrixDimension * cardHeight][matrixDimension * cardWidth];
        for (int i = 0; i < matrixDimension * cardHeight; i++)
            for (int j = 0; j < matrixDimension * cardWidth; j++) {
                gameBoard[i][j] = new Cell();
                this.gameBoard[i][j].setCharacter(' ');
            }
    }

    @Override
    public void displayMessage(String message) {
        System.out.print(message);
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
                upResources += AnsiColor.YELLOW_BACKGROUND.toString() + card.getBackResources().get(0).toString().charAt(0) + card.getBackResources().get(1).toString().charAt(0) + card.getBackResources().get(2).toString().charAt(0) +  " " + AnsiColor.RESET;
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
            leftUpCorner = new Coordinate(matrixDimension / 2 * cardWidth - 5,matrixDimension / 2 * cardHeight - 5);
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

    public CardSelection askCardSelection(List<Integer> ids, List<Integer> idsBack) {
        String message = "Inserisci l'id della carta che vuoi giocare (";
        for (int i = 0; i < ids.size(); i++) {
            message += ids.get(i) + ".f / " + ids.get(i) + ".b";
            if (i != ids.size() - 1)
                message += " / ";
        }

        if (!idsBack.isEmpty())
            message += " / ";

        for (int i = 0; i < idsBack.size(); i++) {
            message += idsBack.get(i) + ".b";
            if (i != idsBack.size() - 1)
                message += " / ";
        }

        message += "): ";
        System.out.print(message);

        boolean validInput = false;
        String input = "";
        while (!validInput) {
            input = scanner.nextLine();
            input = input.trim();

            // input = scanner.nextLine().trim(); // Prende l'input e rimuove gli spazi bianchi iniziali e finali

            try {
                // Verifica se l'input termina con ".b" e controlla la validità
                if (input.endsWith(".b")) {
                    int id = Integer.parseInt(input.substring(0, input.length() - 2)); // Rimuove ".b" e prova a convertire in int
                    if (idsBack.contains(id) || ids.contains(id)) {
                        validInput = true; // L'ID è valido e nell'elenco idsBack
                    }
                } else {
                    // Controlla se l'input è un ID valido in ids (senza ".b")
                    int id = Integer.parseInt(input.endsWith(".f") ? input.substring(0, input.length() - 2) : input); // Rimuove ".f" se presente e prova a convertire in int
                    if (ids.contains(id)) {
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
}