package game.exceptions;

/**
 * Exception thrown when a move exceeds the game boundaries.
 */
public class BoundaryExceededException extends Exception {
    /**
     * Constructs a new BoundaryExceededException with the specified detail message.
     *
     * @param message the detail message.
     */
    public BoundaryExceededException(String message) {
        super(message);
    }
}
