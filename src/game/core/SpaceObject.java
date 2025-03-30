package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing a space object in the game.
 */
public abstract class SpaceObject {
    protected int x;
    protected int y;
    
    /**
     * Constructs a space object with the given coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public SpaceObject(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Returns the x-coordinate.
     *
     * @return the x-coordinate.
     */
    public int getX() {
        return x;
    }
    
    /**
     * Returns the y-coordinate.
     *
     * @return the y-coordinate.
     */
    public int getY() {
        return y;
    }
    
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
