package Minesweeper;

import Minesweeper.GameBoard.GameBoard;
import Minesweeper.GameBoard.Coordinate;
import Minesweeper.GameBoard.GameCell;
import Minesweeper.Layout.AlertBox;

import java.util.Arrays;
import java.util.Random;

public class GameEngine {

    private GameBoard gameBoard;
    private int[] randomNumbers;
    private int height;
    private int width;
    private int bombs;
    private int turn = 0;
    private GameLayout gameLayout;

    public GameEngine(GameLayout gameLayout) {
        this.gameLayout = gameLayout;
        // normal
        this.height = 16;
        this.width = 16;
        this.bombs = 40;
    }

    public void startGame() {
        this.gameBoard = new GameBoard(height, width);
        gameBoard.setBombs(bombs);
        turn = 0;
    }

    /**
     * reveals a field
     * @param row position of field
     * @param column position of field
     */
    public void reveal(int row, int column) {
        if (!gameBoard.isInBound(row, column)) {
            return;
        }
        if (turn == 0) {
            calculateBombs(row, column);
        }
        turn++;
        Coordinate.setRowCoordinate(row);
        Coordinate.setColumnCoordinate(column);
        if(gameBoard.getGameCell().isMarked() || gameBoard.getGameCell().isRevealed()) {

        } else if(gameBoard.getGameCell().isBomb()) {       // field is a bomb
            gameBoard.discoverAll();
            gameLayout.setGameOver(true);
            AlertBox.loose(gameLayout);
        } else {                                            // field is > 0
            gameBoard.getGameCell().setRevealed(true);
            gameBoard.discoverZero(row, column);
            if(gameBoard.checkWin()) {
                gameLayout.setGameOver(true);
                AlertBox.win(gameLayout);
            }
        }
    }

    /**
     * method to mark a field
     * @param row position of field
     * @param column position of field
     */
    public void mark(int row, int column) {
        if (!gameBoard.isInBound(row, column)) {
            return;
        }
        Coordinate.setRowCoordinate(row);
        Coordinate.setColumnCoordinate(column);
        if (gameBoard.getGameCell().isRevealed()) {
            return;
        }
        if (getMarked() <= 0 && !gameBoard.getGameCell().isMarked()) {
            return;
        }
        gameBoard.getGameCell().setMarked();
        gameLayout.bombDisplay();
    }

    /**
     * setter for size of game-board
     * @param height of game-board
     * @param width of game-board
     */
    public void setDimension(int height, int width) {
        this.height = height;
        this.width = width;
    }

    /**
     * setter for amount o bombs
     * @param bombs amount of bombs
     */
    public void setAmountOfBombs(int bombs) {
        if (bombs > (height*width) - 1) {
            System.out.println("The amount of bombs can't be higher than the size of your game board.");
            System.out.println("Please try again:");
            return;
        } else if (bombs < 0) {
            System.out.println("The amount of bombs can't be negativ.");
            System.out.println("Please try again:");
            return;
        }
        this.bombs = bombs;
    }

    public void reset() {
        gameBoard.reset();
        turn = 0;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public GameCell getGameCell(int row, int column) {
        Coordinate.setRowCoordinate(row);
        Coordinate.setColumnCoordinate(column);
        return gameBoard.getGameCell();
    }

    /**
     * calculate random Bombs and set them into the game-board
     */
    private void calculateBombs(int row, int column) {
        int setPosition = ((row * this.width) + column);
        randomNumbers = new int[this.bombs];
        Random random = new Random();
        int tempNumber;
        for (int i = 0; i < bombs; i++) {
            tempNumber = random.nextInt((gameBoard.getHeight() * gameBoard.getWidth()));
            if(checkDuplication(i, tempNumber) || tempNumber == setPosition) {
                i--;
            } else {
                randomNumbers[i] = tempNumber;
            }
        }
        Arrays.sort(randomNumbers);
        gameBoard.prepareField(randomNumbers);
    }

    /**
     * checks if there are two equal randomNumbers
     * @param index of array
     * @param tempNumber number to check
     * @return true if this number exists already
     */
    private boolean checkDuplication(int index, int tempNumber) {
        for (int i = 0; i < index; i++) {
            if (randomNumbers[i] == tempNumber) {
                return true;
            }
        }
        return false;
    }

    public int getMarked() {
        return gameBoard.getAmountOfBombs() - gameBoard.getAmountOfMarked();
    }

    public int getBombs() {
        return gameBoard.getAmountOfBombs();
    }
}