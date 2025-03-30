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

    /**
     * Returns the graphical representation of the enemy,
     * using the image resource from the relative path "src/assets/enemy.png".
     *
     * @return an ObjectGraphic representing the enemy.
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Enemy", "src/assets/enemy.png");
    }

    /**
     * Updates the enemy's state on each tick.
     * In this implementation, the enemy moves downward by increasing its y-coordinate.
     *
     * @param tick the current game tick.
     */
    @Override
    public void tick(int tick) {
        y++; // Move downward each tick
    }
}
