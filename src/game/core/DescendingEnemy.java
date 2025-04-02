package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an enemy that descends faster.
 */
public abstract class DescendingEnemy extends Enemy {

    /**
     * Constructs a DescendingEnemy with the specified coordinates.
     *
     * @param x the x-coordinate of the enemy.
     * @param y the y-coordinate of the enemy.
     */
    public DescendingEnemy(int x, int y) {
        super(x, y);
    }

    /**
     * Overrides tick to make the enemy descend 2 units per tick.
     *
     * @param tick the current tick count.
     */
    @Override
    public void tick(int tick) {
        setY(getY() + 2);
    }

    /**
     * Abstract render method to be implemented by concrete subclasses.
     *
     * @return an ObjectGraphic representing the enemy.
     */
    @Override
    public abstract ObjectGraphic render();
}
