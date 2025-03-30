package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract base class representing a space object in the game.
 */
public abstract class SpaceObject {
    protected int x;
    protected int y;

    public SpaceObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { 
        return x; 
    }

    public int getY() { 
        return y; 
    }

    /**
     * Returns the graphical representation of the object for rendering.
     *
     * @return an ObjectGraphic representing the object
     */
    public abstract ObjectGraphic render();

    /**
     * Updates the object's state on each game tick.
     *
     * @param tick the current game tick
     */
    public abstract void tick(int tick);
}
