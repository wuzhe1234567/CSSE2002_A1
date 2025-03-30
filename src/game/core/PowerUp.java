package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing a generic power-up.
 */
public abstract class PowerUp implements SpaceObject {
    protected int x;
    protected int y;
    
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
        // By default, power-ups remain stationary.
    }
    
    /**
     * Applies the effect of the power-up to the given ship.
     *
     * @param ship the ship that collects the power-up.
     */
    public abstract void applyEffect(Ship ship);
}
