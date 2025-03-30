package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing an object with a position.
 * This class can serve as a common base for game objects.
 */
public abstract class ObjectWithPosition {
    protected int x;
    protected int y;

    /**
     * Constructs an object with the specified coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public ObjectWithPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x-coordinate.
     *
     * @return the x-coordinate.
     */
    public abstract int getX();

    /**
     * Returns the y-coordinate.
     *
     * @return the y-coordinate.
     */
    public abstract int getY();

    /**
     * Returns the graphical representation of the object.
     *
     * @return an ObjectGraphic representing the object.
     */
    public abstract ObjectGraphic render();

    /**
     * Updates the object's state on each game tick.
     *
     * @param tick the current game tick.
     */
    public abstract void tick(int tick);
}
