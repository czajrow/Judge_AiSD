package gui;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import tasks.GameTask;

import java.io.File;

public class Controller {

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Canvas canvas1;
    @FXML
    private Canvas canvas2;
    @FXML
    private ListView<MoveView> listView;
    @FXML
    private Spinner<Integer> spinner;
    @FXML
    private Button okButton;
    @FXML
    private MenuBar menuBar;
    @FXML
    private Label labelWinner;
    @FXML
    private Label labelLooser;
    @FXML
    private Label labelReason;

    private Task<ObservableList<MoveView>> gameTask;
    private File directory;// = new File("programs");
    private File destDirectory;// = new File("out");

    @FXML
    public void initialize() {

        canvas1.getGraphicsContext2D().setFill(Color.BLACK);
        canvas1.getGraphicsContext2D().fillRect(0, 0, canvas1.getWidth(), canvas1.getHeight());

        canvas2.getGraphicsContext2D().setFill(Color.BLACK);
        canvas2.getGraphicsContext2D().fillRect(0, 0, canvas2.getWidth(), canvas2.getHeight());

        progressBar.setVisible(false);
    }

    @FXML
    private void startClicked() {
        if (directory != null && destDirectory != null) {

            okButton.setVisible(false);
            menuBar.setVisible(false);

            gameTask = new GameTask(canvas1, directory, destDirectory, spinner.getValue());
            spinner.setVisible(false);

            progressBar.progressProperty().bind(gameTask.progressProperty());
            progressBar.visibleProperty().bind(gameTask.runningProperty());
            listView.itemsProperty().bind(gameTask.valueProperty());

            Thread thread = new Thread(gameTask);
            thread.setDaemon(true);
            thread.start();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Choose directory with algorithms, please!");
            alert.showAndWait();
        }
    }

    @FXML
    private void listViewClicked() {
        MoveView moveView = listView.getSelectionModel().getSelectedItem();
        moveView.paint(canvas2);
        labelLooser.setText("Zwycięzca: " + moveView.getWinner());
        labelWinner.setText("Przegrany : " + moveView.getLooser());
        labelReason.setText("Powód końca gry: " + moveView.getEndReason());
    }

    @FXML
    private void chooseDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose directory with algorithms");
        directory = directoryChooser.showDialog(new Stage());
    }

    @FXML
    private void chooseDestDir() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose destination directory");
        destDirectory = directoryChooser.showDialog(new Stage());
    }
}
