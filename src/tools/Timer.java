package tools;

public class Timer {

    private final long MAX_MOVE_TIME = 500000000;

    private long startTime;
    private long elapsedTime;

    public void initTimer() {
        startTime = System.nanoTime();
        elapsedTime = 0;
    }

    private void updateTimer() {
        elapsedTime = (System.nanoTime() - startTime);
    }

    public boolean timeExceeded() {
        updateTimer();
        return elapsedTime > MAX_MOVE_TIME;
    }

    public long getElapsedTime() {
        updateTimer();
        return elapsedTime / 1000000;
    }

    public static synchronized void waitInterval() {
    }
}
