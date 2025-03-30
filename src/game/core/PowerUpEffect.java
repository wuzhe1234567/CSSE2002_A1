package game.core;

/**
 * Interface for a power-up effect.
 */
public interface PowerUpEffect {
    /**
     * Applies the effect to the given ship.
     *
     * @param ship the ship to apply the effect on.
     */
    void applyEffect(Ship ship);
}

