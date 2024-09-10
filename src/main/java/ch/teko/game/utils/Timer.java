package ch.teko.game.utils;

/**
 * The {@code Timer} class is a simple utility class for tracking time intervals.
 */
public class Timer {

    // Tracks the elapsed time since the last reset
    long timer;

    // Stores the last recorded time for comparison
    long lastTime;

    /**
     * Constructs a new {@code Timer} instance and automatically resets the timer.
     */
    public Timer() {
        reset();
    }

    /**
     * Checks if the specified delay has been reached. If the elapsed time
     * since the last reset exceeds the given delay, the method returns {@code true}.
     * It also updates the internal timer to reflect the passage of time.
     *
     * @param delay the delay in milliseconds to check against.
     * @return {@code true} if the specified delay has been reached, otherwise {@code false}.
     */
    boolean reached(long delay) {
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();

        if (timer <= delay) {
            return false;
        }

        return true;
    }

    /**
     * Resets the timer, effectively setting the elapsed time back to zero.
     * This method also updates the last recorded time to the current system time.
     */
    void reset() {
        timer = 0;
        lastTime = System.currentTimeMillis();
    }
}
