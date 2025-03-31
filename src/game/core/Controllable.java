package game.core;

import game.exceptions.BoundaryExceededException;
import game.utility.Direction;

/**
 * Public abstract class representing a controllable game object.
 * Inherits from ObjectWithPosition.
 */
public abstract class Controllable extends ObjectWithPosition {
    
    /**
     * Constructs a controllable object with the given coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public Controllable(int x, int y) {
        super(x, y);
    }
    
    /**
     * Moves the object in the specified direction.
     *
     * @param direction the direction to move.
     * @throws BoundaryExceededException if the move exceeds game boundaries.
     */
    public abstract void move(Direction direction) throws BoundaryExceededException;
}
