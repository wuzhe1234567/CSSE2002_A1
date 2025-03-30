package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a shield power-up for the ship, granting temporary invincibility.
 * The power-up lasts for a specified number of ticks, after which it expires.
 */
public class ShieldPowerUp extends SpaceObject {
    private int duration; // Duration in ticks

    /**
     * Constructs a new ShieldPowerUp.
     *
     * @param x the x-coordinate of the power-up
     * @param y the y-coordinate of the power-up (usually the position of the removed Health object)
     * @param duration the duration in ticks for which the power-up remains active
     */
    public ShieldPowerUp(int x, int y, int duration) {
        super(x, y);
        this.duration = duration;
    }

    /**
     * Returns the graphical representation of the shield power-up,
     * using the image resource from the given relative or absolute path.
     *
     * @return an ObjectGraphic representing the shield power-up
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("ShieldPowerUp", "src/assets/shield.png");
    }

    /**
     * Updates the power-up's duration by decreasing it each tick.
     */
    @Override
    public void update() {
        if (duration > 0) {
            duration--;
        }
    }

    /**
     * Checks if the shield power-up is still active.
     *
     * @return true if active; false otherwise
     */
    public boolean isActive() {
        return duration > 0;
    }

    /**
     * Returns the remaining duration in ticks.
     *
     * @return the remaining duration
     */
    public int getDuration() {
        return duration;
    }
}
