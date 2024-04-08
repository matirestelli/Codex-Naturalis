package it.polimi.ingsw.gc38.model;

import it.polimi.ingsw.gc38.view.CliView;

import java.util.List;
import java.util.Map;
public class SxDiagonalObjective extends PositionObjective{
    private List<Color> colors;

    public void displayCard(CliView view) {
        final String ANSI_RED_BACKGROUND = "\u001B[41m";
        final String ANSI_COLOR_RESET = "\u001B[0m";
        final String ANSI_BOLD = "\u001B[1m";
        final String ANSI_COLOR_GOLD = "\u001B[33m";
        final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
        final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
        final String ANSI_GREEN_BACKGROUND = "\u001B[42m";

        String ANSIColor = "";

        if (getColor() == Color.RED) {
            ANSIColor = ANSI_RED_BACKGROUND;
        } else if (getColor() == Color.BLUE) {
            ANSIColor = ANSI_BLUE_BACKGROUND;
        } else if (getColor() == Color.PURPLE) {
            ANSIColor = ANSI_PURPLE_BACKGROUND;
        } else if (getColor() == Color.GREEN) {
            ANSIColor = ANSI_GREEN_BACKGROUND;
        }

        view.displayMessage(ANSIColor + " " + colors.get(0).toString().charAt(0) + " " + ANSI_BOLD + ANSI_COLOR_GOLD + this.getPoints() + ANSI_COLOR_RESET + ANSIColor + "   " + ANSI_COLOR_RESET);
        view.displayMessage(ANSIColor + "   " + colors.get(1).toString().charAt(0) + "   " + ANSI_COLOR_RESET);
        view.displayMessage(ANSIColor + "     " + colors.get(2).toString().charAt(0) + " " + ANSI_COLOR_RESET);
    }

    public void calculatePoints(Player p) {
        int rows = p.getMatrix().length;
        int cols = p.getMatrix()[0].length;

        for (int i = 0; i < rows-2 ; i++) {
            for (int j = 0; j < cols-2; j++) {
                // per ogni carta controllo solo la diagonale in alto a dx e se non la ho giá usata
                if (p.getMatrix()[i][j] != -1 && p.getMatrix()[i+1][j+1] != -1 && p.getMatrix()[i+2][j+2] != -1 &&
                        p.getMatrix()[i][j] < 90 && p.getMatrix()[i+1][j+1] < 90 && p.getMatrix()[i+2][j+2] < 90)
                {
                    if (this.getCard(p, p.getMatrix()[i][j]).getColor() == colors.get(0) &&
                            this.getCard(p, p.getMatrix()[i +1][j +1]).getColor() == colors.get(1) &&
                            this.getCard(p, p.getMatrix()[i + 2][j + 2]).getColor() == colors.get(2)) {
                        if (!this.getIDusati().contains(p.getMatrix()[i][j]) ||
                                !this.getIDusati().contains(p.getMatrix()[i + 1][j + 1]) ||
                                !this.getIDusati().contains(p.getMatrix()[i + 2][j + 2])) {
                            this.addIDusato(p.getMatrix()[i][j]);
                            this.addIDusato(p.getMatrix()[i + 1][j + 1]);
                            this.addIDusato(p.getMatrix()[i + 2][j + 2]);
                            this.setCompleted();
                        }
                    }
                }
            }
        }
        p.addScore(getCompleted() * getPoints());
        this.resetCompleted();
    }
}