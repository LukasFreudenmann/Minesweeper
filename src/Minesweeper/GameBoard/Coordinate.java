package Minesweeper.GameBoard;

public class Coordinate {

    private static int row;
    private static int column;

    public static void setColumnCoordinate(int columnCoordinate) {
        Coordinate.column = columnCoordinate;
    }

    public static void setRowCoordinate(int rowCoordinate) {
        Coordinate.row = rowCoordinate;
    }

    public static int getRow() {
        return row;
    }

    public static int getColumn() {
        return column;
    }
}