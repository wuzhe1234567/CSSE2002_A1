package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing an enemy that descends faster.
 */
public abstract class DescendingEnemy extends Enemy {

    /**
     * Constructs a DescendingEnemy with the specified coordinates.
     *
     * @param x the x-coordinate of the enemy
     * @param y the y-coordinate of the enemy
     */
    public DescendingEnemy(int x, int y) {
        super(x, y);
    }

    /**
     * Updates the enemy's position by moving it downward faster.
     *
     * @param tick the current game tick
     */
    @Override
    public void tick(int tick) {
        y += 2; // Descends 2 units per tick
    }

    /**
     * Returns the graphical representation of the enemy.
     * Concrete subclasses must implement this method.
     *
     * @return an ObjectGraphic representing the enemy
     */
    @Override
    public abstract ObjectGraphic render();
}
