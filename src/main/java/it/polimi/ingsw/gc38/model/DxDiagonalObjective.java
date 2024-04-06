package it.polimi.ingsw.gc38.model;
import java.util.List;
import java.util.Map;

public class DxDiagonalObjective extends PositionObjective{
    public void CalculatePoints(Player p) {
        int rows = p.getMatrix().length;
        int cols = p.getMatrix()[0].length;

        for (int i = 0; i < rows-2 ; i++) {
            for (int j = 2; j < cols; j++) {
                // per ogni carta controllo solo la diagonale in alto a dx e se non la ho giá usata
                if (p.getMatrix()[i][j] != -1 && p.getMatrix()[i+1][j-1] != -1 && p.getMatrix()[i+2][j-2] != -1 &&
                        p.getMatrix()[i][j] < 90 && p.getMatrix()[i+1][j-1] < 90 && p.getMatrix()[i+2][j-2] < 90)
                {
                    if (this.getCard(p, p.getMatrix()[i][j]).getColor() == getColor() &&
                            this.getCard(p, p.getMatrix()[i +1][j - 1]).getColor() == getColor() &&
                            this.getCard(p, p.getMatrix()[i + 2][j - 2]).getColor() == getColor()) {
                        if (!this.getIDusati().contains(p.getMatrix()[i][j]) ||
                                !this.getIDusati().contains(p.getMatrix()[i + 1][j - 1]) ||
                                !this.getIDusati().contains(p.getMatrix()[i + 2][j - 2])) {
                            this.addIDusato(p.getMatrix()[i][j]);
                            this.addIDusato(p.getMatrix()[i + 1][j - 1]);
                            this.addIDusato(p.getMatrix()[i + 2][j - 2]);
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