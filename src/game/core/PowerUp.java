package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing a generic power-up.
 * Implements SpaceObject and PowerUpEffect.
 */
public abstract class PowerUp implements SpaceObject, PowerUpEffect {
    private int xpos;
    private int ypos;

    /**
     * Constructs a PowerUp with the specified coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public PowerUp(int x, int y) {
        this.xpos = x;
        this.ypos = y;
    }

    @Override
    public int getX() {
        return xpos;
    }

    @Override
    public int getY() {
        return ypos;
    }

    @Override
    public abstract ObjectGraphic render();

    @Override
    public void tick(int tick) {
        // Default: power-ups remain stationary.
    }

    /**
     * Applies the power-up effect to the given ship.
     *
     * @param ship the ship to apply the effect on.
     */
    @Override
    public abstract void applyEffect(Ship ship);
}


