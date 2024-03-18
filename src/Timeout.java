import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Tyler James
 * Timeout class changed timedout flag when a certain amount of time has passed
 */
public class Timeout {
    protected boolean timedout = false;
    protected long timeout;
    private TimerTask task;

    /**
     * Constructor for timeout class
     * @param timeout How long this class should wait for a result before flipping the timedout flag
     */
    public Timeout(long timeout) {
        this.timeout = timeout;
    }

    /**
     * Starts the timer
     */
    protected void startTimeoutClock() {
        Timer timer = new Timer();
        long startTime = System.currentTimeMillis();
        task = new TimerTask() {
            @Override
            public void run() {
                // This code will run after 5 seconds
                long endTime = System.currentTimeMillis();
                long elapsedTime = endTime - startTime;
                System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
                System.out.println("Timed out");
                timedout = true;
            }
        };
        timer.schedule(task, timeout);
    }

    /**
     * Resets the timer so it can be started again
     */
    protected void cancelAndResetClock() {
        task.cancel();
        timedout = false;
    }
}
