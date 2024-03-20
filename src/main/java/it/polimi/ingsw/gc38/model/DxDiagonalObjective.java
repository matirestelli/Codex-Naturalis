package it.polimi.ingsw.gc38.model;
import java.util.List;
import java.util.Map;

public class DxDiagonalObjective extends PositionObjective{
    private Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void CalculatePoints(Player p) {
        int rows = p.getMatrix().length;
        int cols = p.getMatrix()[0].length;

        for (int i = 0; i < rows - 2; i++) {
            for (int j = 0; j < cols - 2; j++) {
                // per ogni carta controllo solo la diagonale in alto a dx e se non la ho giá usata
                if (p.this.getCard(p,p.getMatrix()[i][j]).getColor() == color &&
                    p.this.getCard(p,p.getMatrix()[i+1][j+1]).getColor() == color &&
                    p.this.getCard(p,p.getMatrix()[i+2][j+2]).getColor() == color) {
                    if( !this.getIDusati().contains(p.getMatrix()[i][j]) ||
                        !this.getIDusati().contains(p.getMatrix()[i+1][j+1]) ||
                        !this.getIDusati().contains(p.getMatrix()[i+2][j+2]) {
                            this.addIDusato(p.getMatrix()[i][j]);
                            this.addIDusato(p.getMatrix()[i+1][j+1]);
                            this.addIDusato(p.getMatrix()[i+2][j+2]);
                            this.setCompleted();
                    }
                }
            }
        }
     p.addScore(getCompleted() * getPoints());
    }
}