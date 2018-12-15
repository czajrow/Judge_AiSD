package gui;

import court.Matrix;
import enums.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Painter {

    private static final Color DEFAULT_COLOR = Color.GRAY;
    private static final Color RED_PLAYER_COLOR = Color.RED;
    private static final Color GREEN_PLAYER_COLOR = Color.GREEN;
    private static final Color BACKGROUND_COLOR = Color.WHITE;
    private static final Color GRID_COLOR = Color.BLACK;

    public static void paintMatrix(Matrix matrix, Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(BACKGROUND_COLOR);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        int resolution = matrix.DIMENSION;
        int w = (int) (canvas.getWidth() / resolution);

        for (int x = 0; x < resolution; x++) {
            for (int y = 0; y < resolution; y++) {
                gc.setStroke(GRID_COLOR);
                gc.strokeRect(x * w, y * w, w, w);
                Color color = chooseColor(matrix.getCellOwner(x, y));
                gc.setFill(color);
                gc.fillRect(x * w, y * w, w, w);
            }
        }
    }

    private static Color chooseColor(Player player) {
        switch (player) {
            case FIRST:
                return RED_PLAYER_COLOR;
            case SECOND:
                return GREEN_PLAYER_COLOR;
            default:
                return DEFAULT_COLOR;
        }
    }
}
