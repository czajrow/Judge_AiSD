package records;

import court.Cell;
import court.Matrix;
import enums.Player;

import java.util.ArrayList;
import java.util.List;

public class GameView {

    private String message;
    private String winner;
    private String firstPlayer;
    private String secondPlayer;
    private String endReason;
    private int dimension;
    private List<Cell> fixed;

    private ArrayList<String> moves = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();

    private int current = 0;

    public void set(String message, String winner, String firstPlayer, String secondPlayer, String endReason, int dimension, List<Cell> fixed) {
        this.message = message;
        this.winner = winner;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.endReason = endReason;
        this.dimension = dimension;
        this.fixed = fixed;
    }

    @Override
    public String toString() {
        return message + " " + endReason;
    }

    public String getWinner() {
        return winner;
    }

    public String getFirstPlayer() {
        return firstPlayer;
    }

    public String getSecondPlayer() {
        return secondPlayer;
    }

    public String getEndReason() {
        return endReason;
    }

    public void addMove(String move, Player player) {
        moves.add(move);
        players.add(player);
    }

    public Matrix getNextMatrix() {
        return get(current + 1);
    }

    public Matrix getPrevMatrix() {
        return get(current - 1);
    }

    public boolean isFirst() {
        return current == 0;
    }

    public boolean isLast() {
        return current + 1 == moves.size();
    }

    public Matrix getFirst() {
        return get(0);
    }

    public Matrix getForward() {
        return get(current + 5);
    }

    private Matrix get(int index) {
        setCurrent(index);
        Matrix matrix = new Matrix(dimension, fixed);
        for (int i = 0; i < current; i++) {
            matrix.applyMove(moves.get(i), players.get(i));
        }
        return matrix;
    }

    private void setCurrent(int index) {
        if (index < 0) {
            current = 0;
        } else if (index > moves.size() - 1) {
            current = moves.size() - 1;
        } else {
            current = index;
        }
    }
}
