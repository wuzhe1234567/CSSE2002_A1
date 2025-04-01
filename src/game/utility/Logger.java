package game.utility;

/**
 * A functional interface for logging messages.
 * Implementations should output the log message exactly as provided,
 * without adding extra formatting.
 */
@FunctionalInterface
public interface Logger {
    /**
     * Logs the provided message.
     *
     * @param message the message to log
     */
    void log(String message);
}
