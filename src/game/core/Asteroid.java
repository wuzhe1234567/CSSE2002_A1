package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an asteroid object.
 */
public class Asteroid extends SpaceObject {

    /**
     * Constructs an Asteroid with the specified coordinates.
     *
     * @param x the x-coordinate of the asteroid
     * @param y the y-coordinate of the asteroid
     */
    public Asteroid(int x, int y) {
        super(x, y);
    }

    /**
     * Returns the graphical representation of the asteroid,
     * using the image resource from the relative path "src/assets/asteroid.png".
     *
     * @return an ObjectGraphic representing the asteroid
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Asteroid", "src/assets/asteroid.png");
    }

    /**
     * Updates the asteroid's state on each tick.
     * In this implementation, the asteroid moves downward by increasing its y-coordinate.
     */
    @Override
    public void update() {
        y++; // Move downward each tick
    }
}
