package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a bullet object.
 */
public class Bullet extends SpaceObject {

    /**
     * Constructs a Bullet with the specified coordinates.
     *
     * @param x the x-coordinate of the bullet.
     * @param y the y-coordinate of the bullet.
     */
    public Bullet(int x, int y) {
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
     * Returns the graphical representation of the bullet.
     *
     * @return an ObjectGraphic representing the bullet.
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Bullet", "src/assets/bullet.png");
    }

    /**
     * Updates the bullet's state on each tick (moves upward).
     *
     * @param tick the current game tick.
     */
    @Override
    public void tick(int tick) {
        y--; // Move upward each tick
    }
}
