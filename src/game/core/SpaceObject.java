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
    
    public abstract int getX();
    public abstract int getY();
    public abstract ObjectGraphic render();
    public abstract void tick(int tick);
}


