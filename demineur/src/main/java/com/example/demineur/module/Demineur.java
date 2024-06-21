package com.example.demineur.module;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Demineur {

    private int[][] bombBoard;
    private int[][] cptBombBoard;
    private IntegerProperty[][] caseOpened;
    private BooleanProperty loose;
    private double bombPourcentage;
    private int cptBomb;
    private int tour;
    public Demineur(int height, int width, double bombPourcentage){
        this.bombBoard = new int[height][width];
        this.cptBombBoard = new int[height][width];
        this.caseOpened = new IntegerProperty[height][width];

        this.bombPourcentage = bombPourcentage;
        this.cptBomb = (int)(height * width * this.bombPourcentage);

        this.loose = new SimpleBooleanProperty(false);
        this.tour = 0;

        initBoard();

    }

    public void initBoard(){
        setCaseOpened();
    }

    public void setCaseOpened(){
        for(int h = 0 ; h < caseOpened.length ; h++){
            for(int w = 0 ; w < caseOpened[h].length ; w++){
                caseOpened[h][w] = new SimpleIntegerProperty(-1);
            }
        }
    }

    public void setBomb(int hFirstClick, int wFirstClick){
        int boardHeight = bombBoard.length;
        int boardWidth = bombBoard[0].length;
        int tempCptBomb = cptBomb;

        while(tempCptBomb != 0){
            int x = (int)(Math.random()*boardWidth);
            int y = (int)(Math.random()*boardHeight);
            if(!isABomb(y, x) && !isNearFirstClick(hFirstClick, wFirstClick, y, x)){
                System.out.println("Bomb :\n"+hFirstClick+" "+y+"\n"+wFirstClick+" "+x);
                bombBoard[y][x] = 1;
                tempCptBomb--;
            }
        }

        setCptBombBoard();
    }

    public boolean isABomb(int h, int w){
        return bombBoard[h][w]==1;
    }

    /**
     * Si la case sélectionnée dans les coordonnées de la méthode est à moins d'une case d'écart, la méthode
     * retourne vraie, sinon faux.
     * @param h
     * @param w
     * @return
     */
    public boolean isNearFirstClick(int hFirst, int wFirst, int h, int w){
        return Math.abs(hFirst-h)<=1 && Math.abs(wFirst-w)<=1;
    }

    public void setCptBombBoard(){
        for(int h = 0; h < bombBoard.length ; h++){
            for(int w = 0; w < bombBoard[h].length ; w++){
                if(bombBoard[h][w] != 1) {
                    int cptBombAround = 0;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++)
                            if (i != 0 || j != 0)
                                if(isOnBoard(h+i,w+j) && bombBoard[h+i][w+j]==1)
                                    cptBombAround++;
                    }
                    cptBombBoard[h][w] = cptBombAround;
                }
            }
        }
    }

    public boolean isOnBoard(int h, int w){
        return h>=0 && h<bombBoard.length && w>=0 && w<bombBoard[0].length;
    }

    public void firstOpen(int h, int w){
        setBomb(h, w);
        recursiveOpen(h, w);
        terminalBoardDisplay(bombBoard);
        terminalBoardDisplay(cptBombBoard);
    }

    public void clicked(int h, int w){
        if(tour==0)
            firstOpen(h, w);
        else if(!isCaseDisplayed(h, w))
            open(h, w);


        tour++;
    }

    public void open(int h, int w){
        if(bombBoard[h][w] == 1){
            loose.set(true);
        }
        else{
            recursiveOpen(h, w);
        }
    }

    public void recursiveOpen(int h, int w){
        caseOpened[h][w].set(cptBombBoard[h][w]);
        if(cptBombBoard[h][w]==0) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++)
                    if(isOnBoard(h+i, w+j) && !isCaseDisplayed(h+i, w+j))
                        recursiveOpen(h + i, w + j);
            }
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++)
                    if(isOnBoard(h+i, w+j))
                        caseOpened[h + i][w + j].set(cptBombBoard[h + i][w + j]);
            }
        }
    }

    public void terminalBoardDisplay(int[][] tab){
        for(int i = 0 ; i < tab.length ; i++){
            for(int j = 0 ; j < tab[i].length ; j++){
                System.out.print(tab[i][j]+"  ");
            }
            System.out.println();
        }
    }

    public boolean isCaseDisplayed(int h, int w){
        return caseOpened[h][w].get()!=-1;
    }

    public IntegerProperty[][] getCaseOpened() {
        return caseOpened;
    }

    public int getTour() {
        return tour;
    }

    public BooleanProperty isLoose() {
        return loose;
    }

    public int[][] getBombBoard() {
        return bombBoard;
    }

    public int getCptBomb() {
        return cptBomb;
    }
}
