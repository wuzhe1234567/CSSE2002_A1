package game.core;

import game.exceptions.BoundaryExceededException;
import game.utility.Direction;

/**
 * Abstract class representing a controllable game object.
 * Inherits from SpaceObject.
 */
public abstract class Controllable extends SpaceObject {

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
