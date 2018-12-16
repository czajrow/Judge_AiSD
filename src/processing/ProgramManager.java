package processing;

import exceptions.InfoFileReadFailException;
import tools.InfoReader;
import tools.Timer;

import java.io.*;
import java.util.HashMap;

public class ProgramManager {

    private ProcessBuilder processBuilder;
    private Process process;

    private String name;
    private String alias;

    private BufferedReader input;
    private PrintWriter output;

    private boolean timeIsUp = false;

    public ProgramManager(File directory) throws InfoFileReadFailException {

        HashMap<String, String> info = InfoReader.readInfo(directory);
        name = info.get(InfoReader.NAME);
        alias = info.get(InfoReader.ALIAS);
        String executeLine = info.get(InfoReader.EXECUTE_LINE);

        processBuilder = new ProcessBuilder();

        processBuilder.command(executeLine.split(" "));
        processBuilder.directory(directory.getAbsoluteFile());
    }


    public void initializeProcess() {
        try {
            process = processBuilder.start();
            input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            output = new PrintWriter(process.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean hasInput() throws IOException {
        return input.ready();
    }

    public String getMessage() throws IOException {
        if (hasInput()) {
            return input.readLine();
        } else {
            throw new IOException("There is no line to read");
        }
    }


    public void sendMessage(String line) {
        output.println(line);
    }

    public void finalizeProcess() {
        sendMessage("stop");
        long start = System.nanoTime();
        while (process.isAlive()) {
        }
        System.out.println("\t\t\t\t\t" + (System.nanoTime() - start) / 100000 + "ms to finalize");
        process.destroy();
        output.close();
        try {
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isTimeIsUp() {
        boolean result = timeIsUp;
        timeIsUp = false;
        return result;
    }

    public void waitForMove(String message) {

//        this.sendMessage(message);
//        Timer timer = new Timer();
//
//        timer.initTimer();
//        try {
//            while (!hasInput() && !timer.timeExceeded()) { // while (!hasInput()) {
//                Timer.waitInterval();
//            }
//
//            if (timer.timeExceeded()) {
//                System.out.println("time's up");
//                timeIsUp = true;
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        Thread wait = new Thread(() -> {
            this.sendMessage(message);
            Timer timer = new Timer();

            timer.initTimer();
            try {
                while (!hasInput() && !timer.timeExceeded()) { // while (!hasInput()) {
                    Timer.waitInterval();
                }

                if (timer.timeExceeded()) {
                    System.out.println("time's up");
                    timeIsUp = true;
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        wait.start();

        try {
            wait.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        while (wait.isAlive() && !wait.isInterrupted()) {
//            Timer.waitInterval();
//        }
    }
}
