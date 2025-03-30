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
     * 此处要求方法名称为 applyEffect 而非 apply。
     *
     * @param ship the ship that collects the power-up.
     */
    public abstract void applyEffect(Ship ship);
    
    @Override
    public void tick(int tick) {
        // By default, power-ups remain stationary.
    }
}
