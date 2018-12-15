package court;

import enums.Player;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Matrix {

    private Cell[][] cells;
    public final int DIMENSION;
    private String lastMove;

    public Matrix(int dimension) {

        DIMENSION = dimension;

        cells = new Cell[DIMENSION][DIMENSION];
        for (int x = 0; x < DIMENSION; x++) {
            for (int y = 0; y < DIMENSION; y++) {
                cells[x][y] = new Cell(x, y);
            }
        }
    }

    public void setCellOwner(int x, int y, Player player) {
        if (x >= DIMENSION || y >= DIMENSION) {
            throw new IllegalArgumentException("Out of bound");
        }
        Cell cell = cells[x][y];
        if (cell.getOwner() != Player.DEFAULT) {
            throw new IllegalArgumentException("Can't change owner of this cell. It's owned");
        }
        cell.setOwner(player);
    }


    public Player getCellOwner(int x, int y) {
        if (x >= DIMENSION || y >= DIMENSION) {
            throw new IllegalArgumentException("Out of bound");
        }
        return cells[x][y].getOwner();
    }

    public boolean applyMove(String move, Player player) {
        LinkedList<Integer> numbers = new LinkedList<>();
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(move);
        while (m.find()) {
            numbers.add(Integer.parseInt(m.group()));
        }

        int x1 = numbers.get(0);
        int y1 = numbers.get(1);
        int x2 = numbers.get(2);
        int y2 = numbers.get(3);

        if (isMoveValid(x1, y1, x2, y2)) {

            setCellOwner(numbers.get(0), numbers.get(1), player);
            setCellOwner(numbers.get(2), numbers.get(3), player);

            setLastMove(move);
            return true;
        }
        return false;
    }

    public boolean isMoveValid(int x1, int y1, int x2, int y2) {
        return (x1 >= 0 && x1 < DIMENSION) &&
                (y1 >= 0 && y1 < DIMENSION) &&
                (x2 >= 0 && x2 < DIMENSION) &&
                (y2 >= 0 && y2 < DIMENSION) &&
                (
                        (x1 == x2 && Math.abs(y1 - y2) == 1) ||
                                (y1 == y2 && Math.abs(x1 - x2) == 1)
                ) &&
                getCellOwner(x1, y1) == Player.DEFAULT &&
                getCellOwner(x2, y2) == Player.DEFAULT;

    }

    public String getLastMove() {
        return lastMove;
    }

    public void setLastMove(String lastMove) {
        this.lastMove = lastMove;
    }

    public boolean isFull() {
        for (int x = 0; x < DIMENSION; x++) {
            for (int y = 0; y < DIMENSION; y++) {
                if (cells[x][y].getOwner() == Player.DEFAULT) {
                    if (countNeighbours(x, y) > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private int countNeighbours(int x, int y) {
        int count = 0;
        ;
        if (x != DIMENSION - 1 && cells[x + 1][y].getOwner() == Player.DEFAULT) {
            count++;
        }
        if (x != 0 && cells[x - 1][y].getOwner() == Player.DEFAULT) {
            count++;
        }
        if (y != DIMENSION - 1 && cells[x][y + 1].getOwner() == Player.DEFAULT) {
            count++;
        }
        if (y != 0 && cells[x][y - 1].getOwner() == Player.DEFAULT) {
            count++;
        }
        return count;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int y = 0; y < DIMENSION; y++) {
            for (int x = 0; x < DIMENSION; x++) {
                if (cells[x][y].getOwner() == Player.DEFAULT) {
                    sb.append('[').append(']');
                } else {
                    sb.append('\u2588').append('\u2588');
                }
            }
            sb.append('\n');
        }
        return sb.toString();
    }
}
