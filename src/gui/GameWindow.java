package gui;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import records.GameShower;
import records.GameView;

public class GameWindow {

    @FXML
    private Canvas canvas;
    @FXML
    private Label redPlayerLabel;
    @FXML
    private Label greenPlayerLabel;
    @FXML
    private Label winnerLabel;
    @FXML
    private Label endReasonLabel;

    private GameView current;

    @FXML
    private void initialize() {
        current = GameShower.current;
        Painter.paintMatrix(current.getFirst(), canvas);
        redPlayerLabel.setText("Red player: " + current.getFirstPlayer());
        greenPlayerLabel.setText("Green player: " + current.getSecondPlayer());
        winnerLabel.setText("Winner: " + current.getWinner());
        endReasonLabel.setText("End game reason: " + current.getEndReason());
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
