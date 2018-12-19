package gui;

import court.Matrix;
import javafx.scene.canvas.Canvas;

public class MoveView {

    private Matrix matrix;
    private String message;
    private String winner;
    private String looser;
    private String endReason;


    public MoveView(Matrix matrix, String message, String winner, String looser, String endReason) {
        this.matrix = matrix;
        this.message = message;
        this.winner = winner;
        this.looser = looser;
        this.endReason = endReason;
    }

    @Override
    public String toString() {
        return message + " " + endReason;
    }

    public void paint(Canvas canvas) {
        Painter.paintMatrix(matrix, canvas);
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
}
