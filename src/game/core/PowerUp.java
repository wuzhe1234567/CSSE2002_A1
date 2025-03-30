package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing a generic power-up.
 */
public abstract class PowerUp extends SpaceObject {

    public PowerUp(int x, int y) {
        super(x, y);
    }
    
    /**
     * When the power-up is collected by the ship, apply its effect.
     *
     * @param ship the ship that collects the power-up.
     */
    public abstract void apply(Ship ship);
    
    @Override
    public void tick(int tick) {
        // By default, power-ups remain stationary.
    }
}

