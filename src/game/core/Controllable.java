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
     * Returns the x-coordinate of this object.
     *
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this object.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Updates the object's state on each game tick.
     *
     * @param tick the current game tick
     */
    public abstract void tick(int tick);

    /**
     * Returns the graphical representation of the object.
     *
     * @return an ObjectGraphic representing the object
     */
    public abstract ObjectGraphic render();

    /**
     * Moves the object in the specified direction.
     * Subclasses should override this method.
     *
     * @param direction the direction to move
     * @throws BoundaryExceededException if the move exceeds game boundaries
     */
    public void move(Direction direction) throws BoundaryExceededException {
        throw new UnsupportedOperationException("move method not implemented");
    }
}
