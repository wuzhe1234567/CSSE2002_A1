package game.core;

import game.exceptions.BoundaryExceededException;
import game.utility.Direction;
import game.ui.ObjectGraphic;

/**
 * Abstract class representing a controllable game object.
 */
public abstract class Controllable extends SpaceObject {

    /**
     * Constructs a Controllable object with the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Controllable(int x, int y) {
        super(x, y);
    }

    /**
     * Moves the object in the specified direction.
     *
     * @param direction the direction to move
     * @throws BoundaryExceededException if the move exceeds game boundaries
     */
    public abstract void move(Direction direction) throws BoundaryExceededException;
}
