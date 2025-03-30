package game.exceptions;

/**
 * Exception thrown when a move exceeds the game boundaries.
 */
public class BoundaryExceededException extends Exception {
    public BoundaryExceededException(String message) {
        super(message);
    }
}

