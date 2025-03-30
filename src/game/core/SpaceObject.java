package game.core;

import game.ui.ObjectGraphic;

/**
 * Public abstract interface representing a space object in the game.
 */
public abstract interface SpaceObject {
    /**
     * Returns the x-coordinate of the object.
     *
     * @return the x-coordinate.
     */
    public abstract int getX();

    /**
     * Returns the y-coordinate of the object.
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
