package records;

import court.Matrix;

import java.util.ArrayList;
import java.util.List;

public class GameView {

    private String message;
    private String winner;
    private String looser;
    private String endReason;

    private List<Matrix> matrixes = new ArrayList<>();
    private int current = 0;

    public GameView() {

    }

    public void set(String message, String winner, String looser, String endReason) {
        this.message = message;
        this.winner = winner;
        this.looser = looser;
        this.endReason = endReason;
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

    public void addMatrix(Matrix matrix) {
        matrixes.add(matrix);
    }

    public Matrix getNextMatrix() {
        if (current + 1 < matrixes.size()) {
            return matrixes.get(++current);
        }
        return matrixes.get(current);
    }

    public Matrix getPrevMatrix() {
        if (current - 1 >= 0) {
            return matrixes.get(--current);
        }
        return matrixes.get(current);
    }

    public boolean isFirst() {
        return current == 0;
    }

    public boolean isLast() {
        return current + 1 == matrixes.size();
    }

    public Matrix getFirst() {
        current = 0;
        return matrixes.get(0);
    }

    public Matrix getForward() {
        if (current + 5 < matrixes.size()) {
            current += 5;
        }
        return matrixes.get(current);
    }
}
