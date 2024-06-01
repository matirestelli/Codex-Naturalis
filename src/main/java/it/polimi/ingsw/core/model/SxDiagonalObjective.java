package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;

public class SxDiagonalObjective extends PositionObjective{
    private Color color1;

    public String getType() {
        return "SxDiagonal";
    }

    public void setColor(Color color) {
        this.color1 = color;
    }

    public void CalculatePoints(PlayerState p) {
        int rows = p.getMatrix().length;
        int cols = p.getMatrix()[0].length;

        for (int i = 0; i < rows-2 ; i++) {
            for (int j = 0; j < cols-2; j++) {
                // per ogni carta controllo solo la diagonale in alto a dx e se non la ho giá usata
                if (p.getMatrix()[i][j] != -1 && p.getMatrix()[i+1][j+1] != -1 && p.getMatrix()[i+2][j+2] != -1 &&
                        p.getMatrix()[i][j] < 90 && p.getMatrix()[i+1][j+1] < 90 && p.getMatrix()[i+2][j+2] < 90)
                {
                    if (this.getCard(p, p.getMatrix()[i][j]).getColor() == color1 &&
                            this.getCard(p, p.getMatrix()[i +1][j +1]).getColor() == color1 &&
                            this.getCard(p, p.getMatrix()[i + 2][j + 2]).getColor() == color1) {
                        if (!this.getIDusati().contains(p.getMatrix()[i][j]) &&
                                !this.getIDusati().contains(p.getMatrix()[i + 1][j + 1]) &&
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