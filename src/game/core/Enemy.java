package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an enemy object.
 */
public class Enemy extends SpaceObject {

    /**
     * Constructs an Enemy with the specified coordinates.
     *
     * @param x the x-coordinate of the enemy.
     * @param y the y-coordinate of the enemy.
     */
    public Enemy(int x, int y) {
        super(x, y);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override 
    public int getY() {
        return y;
    }

    /**
     * Returns the graphical representation of the enemy.
     *
     * @return an ObjectGraphic representing the enemy.
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Enemy", "src/assets/enemy.png");
    }

    /**
     * Updates the enemy's state on each tick (moves downward).
     *
     * @param tick the current game tick.
     */
    @Override
    public void tick(int tick) {
        y++; // Move downward each tick
    }
}
