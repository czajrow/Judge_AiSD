package gui;

import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import records.GameShower;
import records.GameView;
import tasks.GameTask;

import java.io.File;
import java.io.IOException;

public class MainWindow {

    @FXML
    private ProgressBar progressBar;
    @FXML
    private ListView<GameView> listView;
    @FXML
    private Spinner<Integer> spinner;
    @FXML
    private Button okButton;
    @FXML
    private MenuBar menuBar;

    private Task<ObservableList<GameView>> gameTask;
    private File directory;// = new File("programs3");
    private File destDirectory;// = new File("out");

    @FXML
    public void initialize() {
        progressBar.setVisible(false);
    }

    @FXML
    private void startClicked() {

        if (directory != null && destDirectory != null) {

            okButton.setVisible(false);
            menuBar.setVisible(false);

            gameTask = new GameTask(directory, destDirectory, spinner.getValue());

            spinner.setVisible(false);

            progressBar.progressProperty().bind(gameTask.progressProperty());
            progressBar.visibleProperty().bind(gameTask.runningProperty());
            listView.itemsProperty().bind(gameTask.valueProperty());

            Thread thread = new Thread(gameTask);
            thread.setDaemon(true);
            thread.start();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Choose directory with algorithms and directory for logs, please!");
            alert.showAndWait();
        }
    }

    @FXML
    private void seeGame() throws IOException {

        GameShower.current = listView.getSelectionModel().getSelectedItem();

        if (GameShower.current == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Choose game to show, please!");
            alert.showAndWait();
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameWindow.fxml"));
        Parent root1 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root1, 400, 500));
        stage.showAndWait();
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
