package game.core;

import game.ui.ObjectGraphic;

/**
 * Public interface representing a space object in the game.
 */
public interface SpaceObject {

    /**
     * Gets the x-coordinate of the space object.
     *
     * @return the x-coordinate.
     */
    int getX();

    /**
     * Gets the y-coordinate of the space object.
     *
     * @return the y-coordinate.
     */
    int getY();

    /**
     * Renders the space object into its graphical representation.
     *
     * @return the graphical representation of the space object.
     */
    ObjectGraphic render();

    /**
     * Updates the space object based on the given tick.
     *
     * @param tick the tick count.
     */
    void tick(int tick);
}
