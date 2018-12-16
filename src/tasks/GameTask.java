package tasks;

import court.Matrix;
import exceptions.InfoFileReadFailException;
import gui.MoveView;
import gui.Painter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import processing.Game;
import processing.ProgramManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameTask extends Task<ObservableList<MoveView>> {

    private Canvas canvas;
    //    private GraphicsContext gc;
    private int dimension;

    private List<File> dirs = new ArrayList<>();
    private List<MoveView> list = new ArrayList<>();

    public GameTask(Canvas canvas, File dir, int dimension) {

        this.dimension = dimension;

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                dirs.add(file);
            }
        }
        System.out.println("Wczytane programy");
        dirs.forEach(directory -> System.out.println("\t" + directory));

        this.canvas = canvas;
//        this.gc = canvas.getGraphicsContext2D();
    }

    @Override
    protected ObservableList<MoveView> call() throws Exception {

        int progress = 0;
        int maxProgress = dirs.size() * (dirs.size() - 1);
        updateProgress(progress, maxProgress);
        int gameIndex = 0;

        for (File player : dirs) {
            for (File opponent : dirs) {
                if (!player.equals(opponent)) {
                    try {
                        ProgramManager programManager1 = new ProgramManager(player);
                        ProgramManager programManager2 = new ProgramManager(opponent);
                        programManager1.initializeProcess();
                        programManager2.initializeProcess();

                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Game game = new Game(programManager1, programManager2, dimension, gameIndex++);

                        Matrix matrix = game.getMatrix();
                        Painter.paintMatrix(matrix, canvas);
                        game.initializeGame();
                        while (!game.gameDone) {
                            game.playNextMove();
                            Painter.paintMatrix(matrix, canvas);
                        }
                        updateProgress(++progress, maxProgress);
                        String message = "" + game + game.getWinnerAlias();
                        list.add(new MoveView(matrix, message));
                        updateValue(FXCollections.observableArrayList(list));

                    } catch (InfoFileReadFailException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("koniec");
        return FXCollections.observableArrayList(list);
    }
}
