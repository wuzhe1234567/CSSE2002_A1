package game.core;

import game.exceptions.BoundaryExceededException;
import game.utility.Direction;

/**
 * Public abstract interface representing a controllable game object.
 * Extends SpaceObject.
 */
public interface Controllable extends SpaceObject {
    /**
     * Moves the object in the specified direction.
     *
     * @param direction the direction to move.
     * @throws BoundaryExceededException if the move exceeds game boundaries.
     */
    public abstract void move(Direction direction) throws BoundaryExceededException;
}
