package processing;


import court.Matrix;
import enums.Player;
import tools.MoveValidator;

import java.io.IOException;

public class Game {

    private ProgramManager firstPlayer;
    private ProgramManager secondPlayer;

    private Player winner;
    public boolean gameDone = false;

    private Matrix matrix;
    private Player currentPlayer;

    public Game(ProgramManager firstPlayer, ProgramManager secondPlayer, int dimension) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        currentPlayer = Player.SECOND; // first is last in initializeGame()
        winner = Player.DEFAULT;
        matrix = new Matrix(dimension);
    }

    public void playGame() {
//        initializeGame();

//        while (!gameDone) {
            generateMove();
            if (matrix.isFull()) {
                System.out.println("GRA ZAKONCZONA");
                endGame();
            }
//            System.out.println(matrix.toString());
//        }
    }

    public void initializeGame() {
        String message = "";

        firstPlayer.waitForMove(String.valueOf(matrix.DIMENSION));

        try {
            if (firstPlayer.hasInput()) {
                message = firstPlayer.getMessage();
                if (!MoveValidator.isConfirmationMessageValid(message)) {
                    ///TODO
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        secondPlayer.waitForMove(String.valueOf(matrix.DIMENSION));

        try {
            if (secondPlayer.hasInput()) {
                message = secondPlayer.getMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        firstPlayer.waitForMove("start");

        try {
            if (firstPlayer.hasInput()) {
                message = firstPlayer.getMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!MoveValidator.isMoveValid(message)) {
            System.out.println("\u001B[31m" + "'" + message + "' nie validuje");
            endGame();
        } else if (!matrix.applyMove(message, Player.FIRST)) {
            System.out.println("\u001B[31m" + "'" + message + "' sie nie podoba matrixowi");
            endGame();
        }
    }

    private void generateMove() {
        ProgramManager player = pickPlayerProgramManager();
        player.waitForMove(matrix.getLastMove());
        String move = "";
        try {
            if (player.hasInput()) {
                move = player.getMessage();
            } else {
                //TODO
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!MoveValidator.isMoveValid(move)) {
            System.out.println("\u001B[31m" + "'" + move + "' nie validuje");
            endGame();
            return;
        } else if (!matrix.applyMove(move, currentPlayer)) {
            System.out.println("\u001B[31m" + "'" + move + "' sie nie podoba matrixowi");
            endGame();
            return;
        }
        changeCurrentPlayer();
    }

    private void changeCurrentPlayer() {
        currentPlayer = currentPlayer == Player.FIRST ? Player.SECOND : Player.FIRST;
    }

    private ProgramManager pickPlayerProgramManager() {
        return currentPlayer == Player.FIRST ? firstPlayer : secondPlayer;
    }

    private void endGame() {
        winner = currentPlayer == Player.FIRST ? Player.SECOND : Player.FIRST;
        gameDone = true;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public String getWinnerAlias() {
        if (winner == Player.FIRST) {
            return firstPlayer.getAlias();
        } else if (winner == Player.SECOND) {
            return secondPlayer.getAlias();
        } else {
            return "ERROR";
        }
    }

    @Override
    public String toString() {
        return "" + firstPlayer.getAlias() + " vs " + secondPlayer.getAlias() + ": ";
    }
}
