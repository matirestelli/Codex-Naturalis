package it.polimi.ingsw.core.model;

import it.polimi.ingsw.core.model.enums.Color;

//import java.util.List;
//import java.util.Map;
public class ReverseLObjective extends PositionObjective {
    private Color color1;
    private Color color2;

    public String getType() {
        return "ReverseL";
    }

    public void setColor1(Color color) {
        this.color1 = color;
    }
    public void setColor2(Color color) {
        this.color2 = color;
    }

    public Color getColor1() {
        return color1;
    }
    public Color getColor2() {
        return color2;
    }



    public void CalculatePoints(PlayerState p) {
        int rows = p.getMatrix().length;
        int cols = p.getMatrix()[0].length;

        for (int i = 0; i < rows-3 ; i++) {
            for (int j = 1; j < cols; j++) {
                // per ogni carta controllo solo la diagonale in alto a dx e se non la ho giá usata
                if (p.getMatrix()[i][j] != -1 && p.getMatrix()[i+2][j] != -1 && p.getMatrix()[i+3][j-1] != -1 &&
                        p.getMatrix()[i][j] < 90 && p.getMatrix()[i+2][j] < 90 && p.getMatrix()[i+3][j-1] < 90)
                {
                    if (this.getCard(p, p.getMatrix()[i][j]).getColor() == color1 &&
                            this.getCard(p, p.getMatrix()[i +2][j]).getColor() == color1 &&
                            this.getCard(p, p.getMatrix()[i + 3][j - 1]).getColor() == color2) {
                        if (!this.getIDusati().contains(p.getMatrix()[i][j]) &&
                                !this.getIDusati().contains(p.getMatrix()[i + 2][j]) &&
                                !this.getIDusati().contains(p.getMatrix()[i + 3][j - 1])) {
                            this.addIDusato(p.getMatrix()[i][j]);
                            this.addIDusato(p.getMatrix()[i + 2][j]);
                            this.addIDusato(p.getMatrix()[i + 3][j - 1]);
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