package it.polimi.ingsw.gc38.model;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.util.stream.IntStream;

public class DownReverseLObjective extends PositionObjective {
    private Color color1;
    private Color color2;
    //private int[][] matrix;
    // private List<Card> codex;
    // private List<Integer> playersCardsId;
    //private List<Integer> IDusati = new ArrayList<>();
    //private int[] indexs = {0,0};

    //Points lo hai dalla classe padre Objective

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

    /*
    //metodo in java funzionale per trovare un oggetto carta dato il suo id nella lista di carte del giocatore
    private Card getCard(int id) {
        Card cardSearched = this.codex.stream()
                .filter(card -> card.getId() == id)
                .findFirst()
                .orElse(null);
        return cardSearched;
    }
*/


    //ridefinizione del metodo complete()
    // nota bene devo passare dalla lista perchè matrix è di interi e non di carte


    //invece di algoritmo ricorsivo -> uso java funzionale , stream lineare della matrice e per ogni elemento diverso da 0 e che non è contenuto nella lista di carte già usate controllo il pattern
    //NB mi servono le coordinate della carta nella matrice -> metodo che dato id carta mi dice i suoi indici nella matrice
    /*
    private int[] getIndexes(int id) {
        int[] indexes = new int[2];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                if (matrix[i][j] == id) {
                    indexes[0] = i;
                    indexes[1] = j;
                }
            }
        }
        return indexes;
     */
/*
    @Override
    public boolean complete(Player p) {
        completed = 0;
        indexs[] = {0,0};
        matrix = p.getMatrix();
        codex = p.getCodex();
        IntStream matrixStream = Arrays.stream(p.getMatrix()).flatMapToInt(x -> Arrays.stream(x))
                .filter(x -> x != -1);
        playersCardsId = matrixStream.boxed().collect(Collectors.toList());
        for (int i = 0; i < playersCardsId.size(); i++) {
            //se non è contenuta nella lista di carte già usate
            if (this.getCard(playersCardsId.get(i)).getColor() == color2 && !IDusati.contains(playersCardsId.get(i))) {
                indexs = getIndexes(playersCardsId.get(i));
                if (this.getCard(matrix[indexs[0] + 1][indexs[1] - 1]).getColor() == color1){
                    if (this.getCard(matrix[indexs[0] + 2][indexs[1] - 1]).getColor() == color1) {
                        completed ++;
                        IDusati.add(matrix[indexs[0]][indexs[1]]);
                        IDusati.add(matrix[indexs[0] + 1][indexs[1] - 1]);
                        IDusati.add(matrix[indexs[0] + 2][indexs[1] - 1]);
                    }
                }
            }
            if (this.getCard(playersCardsId.get(i)).getColor() == color1 && !IDusati.contains(playersCardsId.get(i))) {
                indexs = getIndexes(playersCardsId.get(i));
                if (this.getCard(matrix[indexs[0] + 1][indexs[1]]).getColor() == color1){
                    if (this.getCard(matrix[indexs[0] - 1][indexs[1] + 1]).getColor() == color2) {
                        completed ++;
                        IDusati.add(matrix[indexs[0]][indexs[1]]);
                        IDusati.add(matrix[indexs[0] + 1][indexs[1]]);
                        IDusati.add(matrix[indexs[0] - 1][indexs[1] + 1]);
                    }
                }
            }
            if (this.getCard(playersCardsId.get(i)).getColor() == color1 && !IDusati.contains(playersCardsId.get(i))) {
                indexs = getIndexes(playersCardsId.get(i));
                if (this.getCard(matrix[indexs[0] - 1][indexs[1]]).getColor() == color1){
                    if (this.getCard(matrix[indexs[0] - 2][indexs[1] + 1]).getColor() == color2) {
                        completed ++;
                        IDusati.add(matrix[indexs[0]][indexs[1]]);
                        IDusati.add(matrix[indexs[0] - 1][indexs[1]]);
                        IDusati.add(matrix[indexs[0] - 2][indexs[1] + 1]);
                    }
                }
            }

            if (completed > 0) {
                this.setCompleted(true);
            }
            else {
                this.setCompleted(false);
            }
            return this.isCompleted();
        }


 */


    /*
    //pattern dell'obiettivo con gestione sbagliata della matrice -> da guardare solamente per capire il pattern delle posizioni di x,y
    // se è in posizione 1:
    if (matrix[x][x].getCard().getColor() == color2){
        if (matrix[x+1][x-1].getCard().getColor() == color1){
           if (matrix[x+2][x-1].getCard().getColor() == color1) {
           }
        }
     }
     // se è in posizione 2:
    if (matrix[x][x].getCard().getColor() == color1){
        if (matrix[x+1][x].getCard().getColor() == color1){
           if (matrix[x-1][x+1].getCard().getColor() == color2) {
           }
        }
     }
     // se è in posizione 3:
    if (matrix[x][x].getCard().getColor() == color1){
        if (matrix[x-1][x].getCard().getColor() == color1){
           if (matrix[x-2][x+1].getCard().getColor() == color2) {
           }
        }
     }
     */
    //}

}

