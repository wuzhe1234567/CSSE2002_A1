package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a shield power-up that grants the ship temporary invincibility.
 */
public class ShieldPowerUp extends PowerUp implements PowerUpEffect {
    private int duration = 50; // Fixed duration

    /**
     * Constructs a ShieldPowerUp at the given coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public ShieldPowerUp(int x, int y) {
        super(x, y);
    }

    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("ShieldPowerUp", "src/assets/shield.png");
    }

    @Override
    public void applyEffect(Ship ship) {
        // Here, only a message is printed.
        // In practice, you could call ship.activateShield(duration)
        // to implement the invincibility effect.
        System.out.println("Shield activated for " + duration + " ticks.");
    }
}
