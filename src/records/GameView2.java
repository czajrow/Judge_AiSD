package records;

import court.Cell;
import court.Matrix;
import enums.Player;

import java.util.ArrayList;
import java.util.List;

public class GameView2 {

    private String message;
    private String winner;
    private String looser;
    private String endReason;
    private int dimension;
    private List<Cell> fixed;

    private ArrayList<String> moves = new ArrayList<>();
    private ArrayList<Player> players = new ArrayList<>();

    private int current = 0;

    public void set(String message, String winner, String looser, String endReason, int dimension, List<Cell> fixed) {
        this.message = message;
        this.winner = winner;
        this.looser = looser;
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

    public String getLooser() {
        return looser;
    }

    public String getEndReason() {
        return endReason;
    }

    public void addMove(String move, Player player) {
        moves.add(move);
        players.add(player);
    }

    public Matrix getNextMatrix() {
        if (current + 1 < moves.size()) {
            return get(current++);
        }
        return get(current);
    }

    public Matrix getPrevMatrix() {
        if (current - 1 >= 0) {
            return get(--current);
        }
        return get(current);
    }

    public boolean isFirst() {
        return current == 0;
    }

    public boolean isLast() {
        return current + 1 == moves.size();
    }

    public Matrix getFirst() {
        current = 0;
        return get(0);
    }

    public Matrix getForward() {
        if (current + 5 < moves.size()) {
            current += 5;
        }
        return get(current);
    }

    private Matrix get(int index) {
        Matrix matrix = new Matrix(dimension, fixed);
        for (int i = 0; i < index; i++) {
            matrix.applyMove(moves.get(i), players.get(i));
        }
        return matrix;
    }
}
