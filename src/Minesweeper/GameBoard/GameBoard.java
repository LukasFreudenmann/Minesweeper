package Minesweeper.GameBoard;

public class GameBoard {

    private GameCell[][] gameBoard;
    private int[][] a = {{1,0},{-1, 0},{0, 1},{0, -1},{1, 1},{1, -1},{-1, 1},{-1, -1}};
    private int height = 10;
    private int width = 10;
    private int bombs;

    /**
     * constructor of new Game Bord with size: height * width
     */
    public GameBoard(int height, int width) {
        this.gameBoard = new GameCell[height][width];
        this.height = height;
        this.width = width;
        for(int i = 0; i < gameBoard.length; i++) {
            for(int j = 0; j < gameBoard[i].length; j++) {
                gameBoard[i][j] = new GameCell();
            }
        }
    }

    public void prepareField(int[] bombs) {
        int counter = 0;
        int k = 0;
        for(int i = 0; i < gameBoard.length; i++) {
            for(int j = 0; j < gameBoard[i].length; j++) {
                if(k < bombs.length && counter == bombs[k] ) {
                    gameBoard[i][j].setBomb();
                    k++;
                }
                counter++;
            }
        }
        for(int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                addOne(i, j);
            }
        }
    }

    /**
     * discovers all Zero-Fields
     * @param row of current field
     * @param column of current field
     */
    public void discoverZero(int row, int column) {
        GameCell currentCell;
        if(gameBoard[row][column].getNearby() == 0) {
            for (int i = 0; i < 8; i++) {
                try {
                    currentCell = getGameCell(row + a[i][0], column + a[i][1]);
                } catch (ArrayIndexOutOfBoundsException a) {
                    continue;
                }
                // not marked and not revealed
                if (!currentCell.isMarked() && !currentCell.isRevealed()) {
                    if (currentCell.getNearby() == 0) {
                        currentCell.setRevealed(true);
                        discoverZero(row + a[i][0],column + a[i][1]);
                    } else {
                        currentCell.setRevealed(true);
                    }
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                try {
                    currentCell = getGameCell(row + a[i][0], column + a[i][1]);
                } catch (ArrayIndexOutOfBoundsException a) {
                    continue;
                }
                if (!currentCell.isMarked() && !currentCell.isRevealed()) {
                    if (currentCell.getNearby() == 0) {
                        currentCell.setRevealed(true);
                        discoverZero(row + a[i][0],column + a[i][1]);
                    }
                }
            }
        }
    }

    public void discoverAll() {
        for (GameCell[] line : gameBoard) {
            for (GameCell gameCell : line) {
                gameCell.setRevealed(true);
            }

        }
    }

    public boolean checkWin() {
        for (GameCell[] line : gameBoard) {
            for (GameCell gameCell : line) {
                if((!gameCell.isRevealed() && !gameCell.isBomb()) || (!(gameCell).isBomb() && gameCell.isMarked())) {
                    return false;
                }
            }
        }
        return true;
    }

    public void reset() {
        this.gameBoard = new GameCell[this.height][this.width];
        for(int i = 0; i < gameBoard.length; i++) {
            for(int j = 0; j < gameBoard[i].length; j++) {
                gameBoard[i][j] = new GameCell();
            }
        }
    }

    public GameCell getGameCell() {
        return gameBoard[Coordinate.getRow()][Coordinate.getColumn()];
    }

    // getter for height
    public int getHeight() {
        return height;
    }

    // getter for width
    public int getWidth() {
        return width;
    }

    private GameCell getGameCell(int row, int column) throws ArrayIndexOutOfBoundsException {
        return gameBoard[row][column];
    }

    /**
     * calculate how much bombs are nearby
     * @param row of current field
     * @param column of current field
     */
    private void addOne(int row, int column) {
        if (gameBoard[row][column].isBomb()) {
            for (int i = 0; i < 8; i++) {
                if (isInBound(row + a[i][0], column + a[i][1])) {
                    gameBoard[row + a[i][0]][column + a[i][1]].setNearby();
                }
            }
        }
    }

    public boolean isInBound(int row, int column) {
        if(row < 0 || row >= height || column < 0 || column >= width) {
            return false;
        }
        return true;
    }

    public void setBombs(int bombs) {
        this.bombs = bombs;
    }

    public int getAmountOfBombs() {
        return bombs;
    }

    public int getAmountOfMarked() {
        int counter = 0;
        for(GameCell[] line : gameBoard) {
            for(GameCell gameCell : line) {
                if(gameCell.isMarked()) {
                    counter++;
                }
            }
        }
        return counter;
    }
}