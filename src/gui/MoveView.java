package gui;

import court.Matrix;
import enums.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

public class MoveView {

    private Matrix matrix;
    private String message;

    public MoveView(Matrix matrix, String message) {
        this.matrix = matrix;
        this.message = message;
    }

    @Override
    public String toString() {
        return message;
    }

    public void paint(Canvas canvas) {
        Painter.paintMatrix(matrix, canvas);
    }
}
