package tasks;

import enums.EndGameReason;
import exceptions.InfoFileReadFailException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import processing.Game;
import processing.ProgramManager;
import records.GameView;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameTask extends Task<ObservableList<GameView>> {

    private int dimension;

    private List<File> dirs = new ArrayList<>();
    private Map<String, Integer> winScoreMap = new HashMap<>();
    private Map<String, Integer> loseScoreMap = new HashMap<>();
    private Map<String, Integer> disqualificationScoreMap = new HashMap<>();
    private File dest;
    private List<GameView> list = new ArrayList<>();
    private StringBuilder record = new StringBuilder();

    public GameTask(File dir, File dest, int dimension) {

        this.dest = dest;
        this.dimension = dimension;

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                dirs.add(file);
            }
        }
        System.out.println("Wczytane programy");
        dirs.forEach(directory -> System.out.println("\t" + directory));
    }

    @Override
    protected ObservableList<GameView> call() {

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

                        record.append("GameView between ");
                        record.append(programManager1.getAlias()).append(" (").append(programManager1.getName()).append(')');
                        record.append(" and ");
                        record.append(programManager2.getAlias()).append(" (").append(programManager2.getName()).append(')');

                        GameView currentGameView = new GameView();
                        game.initializeGame();
                        String lastMoveEver = "";
                        while (!game.gameDone) {
                            currentGameView.addMove(game.getMatrix().getLastMove(), game.getPrevPlayer());
                            lastMoveEver = game.playNextMove();
                        }
                        currentGameView.addMove(game.getMatrix().getLastMove(), game.getPrevPlayer());
                        currentGameView.addMove(lastMoveEver, game.getCurrentPlayer());
                        String winner = game.getWinnerToString();
                        String looser = game.getLooserToString();
                        String firstPlayer = game.getFirstPlayerToString();
                        String secondPlayer = game.getSecondPlayerToString();
                        countScore(winner, looser, game.getEndGameReason());
                        programManager1.finalizeProcess();
                        programManager2.finalizeProcess();
                        record.append(" winner: ").append(winner).append(", end game reason: ").append(game.getEndGameReason()).append(System.lineSeparator());
                        updateProgress(++progress, maxProgress);
                        String message = "" + game + " " + winner;
                        currentGameView.set(message, winner, firstPlayer, secondPlayer, game.getEndGameReason(), dimension, game.getFixedCells());
                        list.add(currentGameView);
                        updateValue(FXCollections.observableArrayList(list));
                        scheduled();

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

        File ff;
        try {
            String path = dest.getAbsolutePath() + File.separator + "logs" + File.separator + "score.txt";

            ff = new File(path);

            ff.getParentFile().mkdirs();
            ff.createNewFile();

            try (Writer writer = new BufferedWriter(new FileWriter(ff))) {
                writer.append("wins : loses : disqualifications").append(System.lineSeparator());
                winScoreMap.forEach((k, v) -> {
                    try {
                        writer.append(k).append(": ")
                                .append(v.toString())
                                .append('-')
                                .append(loseScoreMap.get(k).toString())
                                .append('-')
                                .append(disqualificationScoreMap.get(k).toString())
                                .append(System.lineSeparator());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("koniec");
        return FXCollections.observableArrayList(list);
    }

    private void countScore(String winner, String looser, String endReason) {
        if (winScoreMap.containsKey(winner)) {
            int score = winScoreMap.get(winner);
            winScoreMap.replace(winner, ++score);
        } else {
            winScoreMap.put(winner, 1);
        }
        if (!winScoreMap.containsKey(looser)) {
            winScoreMap.put(looser, 0);
        }

        if (loseScoreMap.containsKey(looser)) {
            int score = loseScoreMap.get(looser);
            loseScoreMap.replace(looser, ++score);
        } else {
            loseScoreMap.put(looser, 1);
        }
        if (!loseScoreMap.containsKey(winner)) {
            loseScoreMap.put(winner, 0);
        }

        if (disqualificationScoreMap.containsKey(looser)) {
            if (!endReason.equals(EndGameReason.NORMAL.toString())) {
                int score = disqualificationScoreMap.get(looser);
                disqualificationScoreMap.put(looser, ++score);
            }
        } else {
            if (!endReason.equals(EndGameReason.NORMAL.toString())) {
                disqualificationScoreMap.put(looser, 1);
            } else {
                disqualificationScoreMap.put(looser, 0);
            }
        }
    }
}
