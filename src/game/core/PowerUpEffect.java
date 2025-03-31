package game.core;

/**
 * Interface defining the effect of a power-up.
 */
public interface PowerUpEffect {
    /**
     * Applies the effect of the power-up to the given ship.
     *
     * @param ship the ship that collects the power-up.
     */
    void applyEffect(Ship ship);
}
