package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a bullet object.
 */
public class Bullet extends AbstractSpaceObject {

    /**
     * Constructs a Bullet with the specified coordinates.
     *
     * @param x the x-coordinate of the bullet.
     * @param y the y-coordinate of the bullet.
     */
    public Bullet(int x, int y) {
        super(x, y);
    }

    /**
     * Returns the graphical representation of the bullet,
     * using the image from "src/assets/bullet.png".
     *
     * @return an ObjectGraphic representing the bullet.
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Bullet", "src/assets/bullet.png");
    }

    /**
     * Updates the bullet's state on each tick.
     * In this implementation, the bullet moves upward.
     *
     * @param tick the current game tick.
     */
    @Override
    public void tick(int tick) {
        y--; // Move upward each tick
    }
}
