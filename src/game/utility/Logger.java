package game.utility;

/**
 * A simple logger implementation that writes log messages to standard output.
 */
public class ConsoleLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
