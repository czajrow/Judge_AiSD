package gui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import records.GameShower;
import records.GameView2;

public class GameWindow {

    @FXML
    private Canvas canvas;

    private GameView2 current;

    @FXML
    private void initialize() {
        current = GameShower.current;
        Painter.paintMatrix(current.getFirst(), canvas);
    }

    @FXML
    private void previousClicked() {
        Painter.paintMatrix(current.getPrevMatrix(), canvas);
    }

    @FXML
    private void nextClicked() {
        Painter.paintMatrix(current.getNextMatrix(), canvas);
    }

    @FXML
    private void forwardClicked() {
        Painter.paintMatrix(current.getForward(), canvas);
    }
}
