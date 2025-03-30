package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a health power-up that increases the ship's health.
 */
public class HealthPowerUp extends PowerUp {
    private int healAmount;
    
    /**
     * Constructs a HealthPowerUp at the given coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param healAmount the amount of health to add when applied.
     */
    public HealthPowerUp(int x, int y, int healAmount) {
        super(x, y);
        this.healAmount = healAmount;
    }
    
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("HealthPowerUp", "src/assets/health.png");
    }
    
    @Override
    public void apply(Ship ship) {
        ship.heal(healAmount);
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    @Override
    public int getY() {
        return y;
    }
}

