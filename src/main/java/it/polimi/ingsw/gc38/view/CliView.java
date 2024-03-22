package it.polimi.ingsw.gc38.view;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import it.polimi.ingsw.gc38.model.*;

public class CliView {
    private static final String ANSI_GOLD_PURPLE_BACKGROUND = "\u001B[48;5;129m";
    private static final String ANSI_GOLD_GREEN_BACKGROUND = "\u001B[48;5;28m";
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

    public CardCornerInput askForCardToPlay(List<Integer> ids, List<Integer> idsBack) {
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
            input = scanner.nextLine().trim(); // Prende l'input e rimuove gli spazi bianchi iniziali e finali

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

        return new CardCornerInput(Integer.parseInt(splitInput[0]), splitInput[1].equals("f"));
    }

    public String displayResources(Card card, int index1, int index2, String ANSIColor) {
        String upResources = ANSIColor;

        if (card.getFrontCorners().containsKey(index1))
            if (!card.getFrontCorners().get(index1).isEmpty())
                upResources += card.getFrontCorners().get(index1).getResource().toString().charAt(0); //
            else
                upResources += ANSI_WHITE_BACKGROUND + " " + ANSIColor; //
        else
            upResources += " ";

        if (card instanceof ResourceCard c) {
            if (index1 == 1 && c.getPoint() > 0)
                upResources += "  " + ANSI_YELLOW + c.getPoint() + "  " + ANSI_RESET;
            else
                upResources += "     ";
        } else if (card instanceof GoldCard c) {
            if (index1 == 1) {
                if (c.getPoint().getResource() != Resource.NO_RESOURCE)
                    upResources += " " + ANSI_YELLOW + c.getPoint().getQta() + c.getPoint().getResource().toString().charAt(0) + "  " + ANSI_RESET;
                else
                    upResources += "  " + ANSI_YELLOW + c.getPoint().getQta() + "  " + ANSI_RESET;
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
                upResources += ANSI_WHITE_BACKGROUND + " " + ANSI_RESET;
        else
            upResources += ANSIColor + " " + ANSI_RESET;

        upResources += ANSI_RESET;

        return upResources;
    }

    public String displayResourcesNoColor(ColoredCard card1, int index1, int index2) {
        Card card = (Card) card1;
        String upResources = "";

        if (card.getFrontCorners().containsKey(index1))
            if (!card.getFrontCorners().get(index1).isEmpty()) {
                upResources += card.getFrontCorners().get(index1).getResource().toString().charAt(0);
            }
            else
                upResources += " ";
        else
            upResources += " ";

        // upResources += " ";

        /*if (card1 instanceof ResourceCard c) {
            if (index1 == 1 && c.getPoint() > 0) {
                upResources += c.getPoint() + "  ";
            } else {
                upResources += "   ";
            }
        } else if (card1 instanceof GoldCard c) {
            if (index1 == 1 && c.getPoint().getQta() > 0)
                upResources += Integer.toString(c.getPoint().getQta()) + c.getPoint().getResource().toString().charAt(0);
            else
                upResources += " ";
        }*/
        if (card1 instanceof ResourceCard c) {
            if (index1 == 1 && c.getPoint() > 0)
                upResources += "  " + c.getPoint() + "  ";
            else
                upResources += "     ";
        } else if (card1 instanceof GoldCard c) {
            if (index1 == 1) {
                if (c.getPoint().getResource() != Resource.NO_RESOURCE)
                    upResources += "  " + c.getPoint().getQta() + c.getPoint().getResource().toString().charAt(0) + " ";
                else
                    upResources += "  " + c.getPoint() + "  ";
            } else if (index1 == 0) {
                for (Requirement r : c.getRequirements()) {
                    upResources += Integer.toString(r.getQta()) + r.getResource().toString().charAt(0);
                }
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

    public void displayBoard(Cell[][] matrixBoard) {
        Card card;

        System.out.print("+");
        for (int i = 0; i < matrixBoard[0].length; i++)
            System.out.print("-");
        System.out.println("+");

        for (int i = 0; i < matrixBoard.length; i++) {
            System.out.print("|");
            for (int j = 0; j < matrixBoard[i].length; j++) {
                card = matrixBoard[i][j].getCard();
                if (card != null)
                    System.out.print(matrixBoard[i][j].getColor() + matrixBoard[i][j].getCharacter() + ANSI_RESET);
                else
                    System.out.print(matrixBoard[i][j].getCharacter());
            }
            System.out.print("|");
            System.out.println(ANSI_RESET);
        }

        System.out.print("+");
        for (int i = 0; i < matrixBoard[0].length; i++)
            System.out.print("-");
        System.out.println("+");
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

    public void displayResourceCardBack(ColoredCard card1) {
        Card card;
        if (card1 instanceof ResourceCard) {
            card = (ResourceCard) card1;
        } else {
            card = (GoldCard) card1;
        }

        String ANSIColor = "";

        if (card1.getColor() == Color.RED) {
            if (card1 instanceof GoldCard)
                ANSIColor = ANSI_GOLD_RED_BACKGROUND;
            else
                ANSIColor = ANSI_RED_BACKGROUND;
        }
        else if (card1.getColor() == Color.BLUE) {
            if (card1 instanceof GoldCard)
                ANSIColor = ANSI_GOLD_BLUE_BACKGROUND;
            else
                ANSIColor = ANSI_BLUE_BACKGROUND;
        }
        else if (card1.getColor() == Color.PURPLE) {
            if (card1 instanceof GoldCard)
                ANSIColor = ANSI_GOLD_PURPLE_BACKGROUND;
            else
                ANSIColor = ANSI_PURPLE_BACKGROUND;
        }
        else if (card1.getColor() == Color.GREEN) {
            if (card1 instanceof GoldCard)
                ANSIColor = ANSI_GOLD_GREEN_BACKGROUND;
            else
                ANSIColor = ANSI_GREEN_BACKGROUND;
        }
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

    public void placeCard(Cell[][] matrixBoard, ColoredCard card, Coordinate leftUpCorner) {
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

        for (int i = 0; i < 7; i++) {
            matrixBoard[y][x + i].setCard(c);
            if (c.isFrontSide())
                matrixBoard[y][x + i].setCharacter(displayResourcesNoColor(card, 1, 2).charAt(i));
            matrixBoard[y][x + i].setColor(ANSIColor);

            matrixBoard[y + 1][x + i].setCard(c);
            matrixBoard[y + 1][x + i].setColor(ANSIColor);

            matrixBoard[y + 2][x + i].setCard(c);
            if (c.isFrontSide())
                matrixBoard[y + 2][x + i].setCharacter(displayResourcesNoColor(card, 0, 3).charAt(i));
            matrixBoard[y + 2][x + i].setColor(ANSIColor);
        }

        if (!whichCard) {
            ResourceCard resc = (ResourceCard) card;
            if (c.isFrontSide() && resc.getPoint() > 0)
                matrixBoard[y][x + 3].setColor(ANSI_YELLOW + ANSIColor);
        } else {
            GoldCard gold = (GoldCard) card;
            if (gold.getPoint().getResource() != Resource.NO_RESOURCE)
                matrixBoard[y][x + 3].setColor(ANSI_YELLOW + ANSIColor);
        }

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(0) && c.getFrontCorners().get(0).isEmpty()))
            matrixBoard[y + 2][x].setColor(ANSI_WHITE_BACKGROUND);

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(1) && c.getFrontCorners().get(1).isEmpty()))
            matrixBoard[y][x].setColor(ANSI_WHITE_BACKGROUND);

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(2) && c.getFrontCorners().get(2).isEmpty()))
            matrixBoard[y][x + 6].setColor(ANSI_WHITE_BACKGROUND);

        if (!c.isFrontSide() || (c.getFrontCorners().containsKey(3) && c.getFrontCorners().get(3).isEmpty()))
            matrixBoard[y + 2][x + 6].setColor(ANSI_WHITE_BACKGROUND);

        if (!c.isFrontSide()) {
            if (c.getBackResources().size() == 1)
                matrixBoard[y][x + 3].setCharacter(c.getBackResources().get(0).toString().charAt(0));
            else if (c.getBackResources().size() == 2) {
                matrixBoard[y][x + 2].setCharacter(c.getBackResources().get(0).toString().charAt(0));
                matrixBoard[y][x + 3].setCharacter(c.getBackResources().get(1).toString().charAt(0));
            } else {
                matrixBoard[y][x + 2].setCharacter(c.getBackResources().get(0).toString().charAt(0));
                matrixBoard[y][x + 3].setCharacter(c.getBackResources().get(1).toString().charAt(0));
                matrixBoard[y][x + 4].setCharacter(c.getBackResources().get(2).toString().charAt(0));
            }
        }

        // fix with card ids with multiple digits
        if (c.getId() > 9) {
            String idString = String.valueOf(c.getId());
            matrixBoard[y + 1][x + 2].setCharacter(idString.charAt(0));
            matrixBoard[y + 1][x + 3].setCharacter(idString.charAt(1));
        } else
            matrixBoard[y + 1][x + 3].setCharacter((char) (c.getId() + '0'));
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

        if (index1 == 1) {
            if (card.getBackResources().size() == 1)
                upResources += ANSI_YELLOW_BACKGROUND + " " + card.getBackResources().get(0).toString().charAt(0) + "  " + ANSI_RESET;
            else if (card.getBackResources().size() == 2)
                upResources += ANSI_YELLOW_BACKGROUND + card.getBackResources().get(0).toString().charAt(0) + card.getBackResources().get(1).toString().charAt(0) + "  " + ANSI_RESET;
            else
                upResources += ANSI_YELLOW_BACKGROUND + card.getBackResources().get(0).toString().charAt(0) + card.getBackResources().get(1).toString().charAt(0) + card.getBackResources().get(2).toString().charAt(0) +  " " + ANSI_RESET;
        } else
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
        System.out.println(ANSI_YELLOW_BACKGROUND + "  " + card.getId() + "   " + ANSI_RESET);
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
