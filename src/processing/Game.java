package processing;


import court.Matrix;
import enums.EndGameReason;
import enums.Player;
import tools.MoveValidator;

import java.io.*;

import static enums.EndGameReason.*;

public class Game {

    private ProgramManager firstPlayer;
    private ProgramManager secondPlayer;

    private Player winner;
    public boolean gameDone = false;

    private Matrix matrix;
    private Player currentPlayer;

    private int gameIndex;
    private StringBuilder record = new StringBuilder();

    private EndGameReason endGameReason = NORMAL;
    private File destinationFile;

    public Game(ProgramManager firstPlayer, ProgramManager secondPlayer, File dest, int dimension, int gameIndex) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.destinationFile = dest;
        currentPlayer = Player.SECOND; // first is last in initializeGame()
        winner = Player.DEFAULT;
        matrix = new Matrix(dimension);
        this.gameIndex = gameIndex;

        record.append("Game between ");
        record.append(firstPlayer.getAlias()).append(" (").append(firstPlayer.getName()).append(')');
        record.append(" and ");
        record.append(secondPlayer.getAlias()).append(" (").append(secondPlayer.getName()).append(')');
        record.append(System.lineSeparator()).append(System.lineSeparator());

    }

    public void playNextMove() {
        generateMove();
        if (matrix.isFull()) {
            System.out.println("GRA ZAKONCZONA");
            endGame();
        }
    }

    public void initializeGame() {

        if (!sendMessageToConfirm(String.valueOf(matrix.DIMENSION), firstPlayer)) {
            endGame();
            return;
        }
        if (!sendMessageToConfirm(String.valueOf(matrix.DIMENSION), secondPlayer)) {
            endGame();
            return;
        }
        if (!sendMessageToConfirm(matrix.getFixed(), firstPlayer)) {
            endGame();
            return;
        }
        if (!sendMessageToConfirm(matrix.getFixed(), secondPlayer)) {
            endGame();
            return;
        }
        if (sendMessage("start", firstPlayer) == null) {
            endGame();
        }
    }

    private String sendMessage(String messageToSend, ProgramManager programManager) {

        programManager.waitForMove(messageToSend);

        if (programManager.isTimeIsUp()) {
            endGameReason = TIME_EXCEEDED;
            return null;
        }

        String message;
        try {
            if (programManager.hasInput()) {
                message = programManager.getMessage();

                if (!MoveValidator.isMoveValid(message)) {
                    endGameReason = PROTOCOL_ERROR;
                    return null;
                } else if (!matrix.applyMove(message, currentPlayer)) {
                    endGameReason = INCORRECT_DATA;
                    endGame();
                    return null;
                }
                record.append(programManager.getAlias()).append(": ").append(message).append(System.lineSeparator());
                return message;
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            endGameReason = IO_EXCEPTION;
            return null;
        }
    }

    private boolean sendMessageToConfirm(String messageToSend, ProgramManager programManager) {

        programManager.waitForInit(messageToSend);

        if (programManager.isTimeIsUp()) {
            endGameReason = TIME_EXCEEDED;
            return false;
        }

        String message;
        try {
            if (programManager.hasInput()) {
                message = programManager.getMessage();

                if (!MoveValidator.isConfirmationMessageValid(message)) {
                    endGameReason = PROTOCOL_ERROR;
                    return false;
                }
                record.append(programManager.getAlias()).append(": ").append(message).append(System.lineSeparator());
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            endGameReason = IO_EXCEPTION;
            return false;
        }
    }

    private void generateMove() {
        ProgramManager player = pickPlayerProgramManager();

        String message = sendMessage(matrix.getLastMove(), player);
        if (message == null) {
            endGame();
        } else {
            changeCurrentPlayer();
        }
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

        record.append("End game reason: ").append(endGameReason);

        File f;
        try {
            String path = destinationFile.getAbsolutePath() + File.separator + "logs" + File.separator + "duels" + File.separator + gameIndex + ".txt";

            f = new File(path);

            f.getParentFile().mkdirs();
            f.createNewFile();

            try (Writer writer = new BufferedWriter(new FileWriter(f))) {
                writer.write(record.toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String getLooserAlias() {
        if (winner == Player.FIRST) {
            return secondPlayer.getAlias();
        } else if (winner == Player.SECOND) {
            return firstPlayer.getAlias();
        } else {
            return "ERROR";
        }
    }

    public String getEndGameReason() {
        return endGameReason.toString();
    }

    @Override
    public String toString() {
        return "" + firstPlayer.getAlias() + " vs " + secondPlayer.getAlias() + ": ";
    }
}
