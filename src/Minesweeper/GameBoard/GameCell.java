package Minesweeper.GameBoard;

public class GameCell {

    private boolean revealed;
    private boolean bomb;
    private boolean marked;
    private int nearby;

    public GameCell () {
        this.bomb = false;
        revealed = false;
        marked = false;
    }


    public boolean isBomb() {
        return bomb;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public boolean isMarked() {
        return marked;
    }

    public int getNearby() {
        return nearby;
    }

    public void setNearby() {
        this.nearby++;
    }

    public void setMarked() {
        if (marked) {
            this.marked = false;
        } else {
            this.marked = true;
        }
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public void setBomb() {
        this.bomb = true;
        this.nearby = -20;
    }

}