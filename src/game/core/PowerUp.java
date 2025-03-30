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
     * 注意：规范要求方法名称为 applyEffect 而非 apply。
     *
     * @param ship the ship that collects the power-up.
     */
    public abstract void applyEffect(Ship ship);
    
    @Override
    public void tick(int tick) {
        // Default: power-ups remain stationary.
    }
}
