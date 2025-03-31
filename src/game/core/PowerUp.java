package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing a generic power-up.
 */
public abstract class PowerUp implements SpaceObject {
    protected int x;
    protected int y;
    
    /**
     * Constructs a PowerUp with the specified coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public PowerUp(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    @Override
    public int getY() {
        return y;
    }
    
    @Override
    public abstract ObjectGraphic render();
    
    @Override
    public void tick(int tick) {
        // Power-ups remain stationary by default.
    }
}
