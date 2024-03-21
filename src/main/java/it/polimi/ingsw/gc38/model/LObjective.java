package it.polimi.ingsw.gc38.model;

//import java.util.List;
//import java.util.Map;
public class LObjective extends PositionObjective {
    private Color color1;
    private Color color2;
    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public Color getColor1() {
        return color1;
    }
    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    public Color getColor2() {
        return color2;
    }

    //ridefinizione del metodo verify()

//patter che l'algoritmo ricorsivo controllerà per ogni carta del colore color e che non sia contenuta nella lista di carte già usate per completare un obiettivo
    /*
    // se è in posizione 1:
    if (matrix[x][x].getCard().getColor() == color2){
        if (matrix[x-1][x-1].getCard().getColor() == color1){
           if (matrix[x-2][x-1].getCard().getColor() == color1) {
           }
        }
     }
     // se è in posizione 2:
    if (matrix[x][x].getCard().getColor() == color1){
        if (matrix[x-1][x].getCard().getColor() == color1){
           if (matrix[x+1][x+1].getCard().getColor() == color2) {
           }
        }
     }
     // se è in posizione 3:
    if (matrix[x][x].getCard().getColor() == color1){
        if (matrix[x+1][x].getCard().getColor() == color1){
           if (matrix[x+2][x+1].getCard().getColor() == color2) {
           }
        }
     }
     */
}
