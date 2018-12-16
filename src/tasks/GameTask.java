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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GameTask extends Task<ObservableList<MoveView>> {

    private Canvas canvas;
    //    private GraphicsContext gc;
    private int dimension;

    private List<File> dirs = new ArrayList<>();
    private File dest;
    private List<MoveView> list = new ArrayList<>();
    private StringBuilder record = new StringBuilder();

    public GameTask(Canvas canvas, File dir, File dest, int dimension) {

        this.dest = dest;
        this.dimension = dimension;

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                dirs.add(file);
            }
        }
        System.out.println("Wczytane programy");
        dirs.forEach(directory -> System.out.println("\t" + directory));

        this.canvas = canvas;
    }

    @Override
    protected ObservableList<MoveView> call() throws Exception {

        int progress = 0;
        int maxProgress = dirs.size() * (dirs.size() - 1);
        updateProgress(progress, maxProgress);
        int gameIndex = 0;

        record.append("Gier do rozegrania: ").append(maxProgress).append(System.lineSeparator());

        for (File player : dirs) {
            for (File opponent : dirs) {
                if (!player.equals(opponent)) {
                    try {
                        ProgramManager programManager1 = new ProgramManager(player);
                        ProgramManager programManager2 = new ProgramManager(opponent);
                        programManager1.initializeProcess();
                        programManager2.initializeProcess();

                        Game game = new Game(programManager1, programManager2, dest, dimension, gameIndex++);
                        record.append("Game between ");
                        record.append(programManager1.getAlias()).append(" (").append(programManager1.getName()).append(')');
                        record.append(" and ");
                        record.append(programManager2.getAlias()).append(" (").append(programManager2.getName()).append(')');

                        Matrix matrix = game.getMatrix();
                        Painter.paintMatrix(matrix, canvas);
                        game.initializeGame();
                        while (!game.gameDone) {
                            game.playNextMove();
                            Painter.paintMatrix(matrix, canvas);
                        }
                        record.append(" winner: ").append(game.getWinnerAlias()).append(System.lineSeparator());
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
        File f;
        try {
            String path = dest.getAbsolutePath() + File.separator + "logs" + File.separator + "duels.txt";

            f = new File(path);

            f.getParentFile().mkdirs();
            f.createNewFile();

            try (Writer writer = new BufferedWriter(new FileWriter(f))) {
                writer.write(record.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("koniec");
        return FXCollections.observableArrayList(list);
    }
}
