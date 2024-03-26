package it.polimi.ingsw.gc38.model;
//import java.util.List;
//import java.util.Map;
public class DownLObjective extends PositionObjective{
    private Color color1;
    private Color color2;

    public void setColor1(Color color) {
        this.color1 = color;
    }
    public void setColor2(Color color) {
        this.color2 = color;
    }

    public void CalculatePoints(Player p) {
        int rows = p.getMatrix().length;
        int cols = p.getMatrix()[0].length;

        for (int i = 2; i < rows ; i++) {
            for (int j = 0; j < cols-1; j++) {
                // per ogni carta controllo solo la diagonale in alto a dx e se non la ho giá usata
                if (p.getMatrix()[i][j] != -1 && p.getMatrix()[i-1][j] != -1 && p.getMatrix()[i-2][j+1] != -1 &&
                        p.getMatrix()[i][j] < 90 && p.getMatrix()[i-1][j] < 90 && p.getMatrix()[i-2][j+1] < 90)
                {
                    if (this.getCard(p, p.getMatrix()[i][j]).getColor() == color1 &&
                            this.getCard(p, p.getMatrix()[i -1][j]).getColor() == color1 &&
                            this.getCard(p, p.getMatrix()[i - 2][j + 1]).getColor() == color2) {
                        if (!this.getIDusati().contains(p.getMatrix()[i][j]) ||
                                !this.getIDusati().contains(p.getMatrix()[i - 1][j]) ||
                                !this.getIDusati().contains(p.getMatrix()[i - 2][j + 1])) {
                            this.addIDusato(p.getMatrix()[i][j]);
                            this.addIDusato(p.getMatrix()[i - 1][j]);
                            this.addIDusato(p.getMatrix()[i - 2][j + 1]);
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