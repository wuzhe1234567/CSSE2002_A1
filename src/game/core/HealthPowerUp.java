package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a health power-up that increases the ship's health.
 */
public class HealthPowerUp extends PowerUp implements PowerUpEffect {
    private int healAmount = 20; // Fixed bonus value

    /**
     * Constructs a HealthPowerUp at the given coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public HealthPowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("HealthPowerUp", "src/assets/health.png");
    }

    @Override
    public void applyEffect(Ship ship) {
        ship.heal(healAmount);
    }
}

