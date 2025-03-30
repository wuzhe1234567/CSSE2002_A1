package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing a generic power-up.
 */
public abstract class PowerUp extends SpaceObject {

    /**
     * Constructs a PowerUp with the specified coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public PowerUp(int x, int y) {
        super(x, y);
    }
    
    /**
     * When the power-up is collected by the ship, apply its effect.
     *
     * @param ship the ship that collects the power-up.
     */
    public abstract void apply(Ship ship);
    
    /**
     * By default, a power-up remains stationary.
     *
     * @param tick the current game tick.
     */
    @Override
    public void tick(int tick) {
        // No automatic movement by default.
    }
}
