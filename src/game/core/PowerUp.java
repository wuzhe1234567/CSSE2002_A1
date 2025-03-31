package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing a generic power-up.
 * Implements SpaceObject and PowerUpEffect.
 */
public abstract class PowerUp implements SpaceObject, PowerUpEffect {
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
        // Default: power-ups remain stationary.
    }
    
    // 此方法必须实现，因为 PowerUp 实现了 PowerUpEffect 接口
    @Override
    public abstract void applyEffect(Ship ship);
}

