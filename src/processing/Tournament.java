package processing;

import exceptions.InfoFileReadFailException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tournament {

    //TODO  gracze graja ze soba kazdy z kazdym po dwa razy
    //TODO  i raz zaczyna jeden a raz drugi, ale trzeba zrobic
    //TODO  tak, zeby grali na planszy tej samej wielkosci


    private final int MAX_DIMENSION = 5;
    private final int MIN_DIMENSION = 3;

    private List<File> dirs = new ArrayList<>();

    public Tournament(File dir) {

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                dirs.add(file);
            }
        }
        System.out.println("Wczytane programy");
        dirs.forEach(directory -> System.out.println("\t" + directory));
    }

//    public void play() {
//        dirs.forEach(player -> dirs.forEach(opponent -> {
//            if (!player.equals(opponent)) {
//                try {
//                    ProgramManager programManager1 = new ProgramManager(player);
//                    ProgramManager programManager2 = new ProgramManager(opponent);
//                    programManager1.initializeProcess();
//                    programManager2.initializeProcess();
//
//                    int max = MAX_DIMENSION / 2;
//                    int min = MIN_DIMENSION / 2;
//                    int dimension = 2 * (new Random().nextInt(max + 1 - min) + min) + 1;
//                    Game game = new Game(programManager1, programManager2, dimension);
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    game.playGame();
//
//                } catch (InfoFileReadFailException e) {
//                    e.printStackTrace();
//                }
//            }
//        }));
//    }
}
