package it.polimi.ingsw.gc38.model;

//import java.util.List;
//import java.util.Map;

public class DxDiagonalObjective extends PositionObjective{
    private Color color;

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    //ridefinizione del metodo complete()

    //patter che l'algoritmo ricorsivo controllerà per ogni carta del colore color e che non sia contenuta nella lista di carte già usate per completare un obiettivo

    /*
    // se è in posizione 1:
    if (matrix[x][x].getCard().getColor() == color){
        if (matrix[x-1][x+1].getCard().getColor() == color){
           if (matrix[x-2][x+2].getCard().getColor() == color) {
           }
        }
     }
     // se è in posizione 2:
    if (matrix[x][x].getCard().getColor() == color){
        if (matrix[x+1][x-1].getCard().getColor() == color){
           if (matrix[x-1][x+1].getCard().getColor() == color) {
           }
        }
     }
     // se è in posizione 3:
    if (matrix[x][x].getCard().getColor() == color){
        if (matrix[x-1][x-1].getCard().getColor() == color){
           if (matrix[x-2][x-2].getCard().getColor() == color) {
           }
        }
     }
     */

}