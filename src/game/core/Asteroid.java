package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an asteroid object.
 */
public class Asteroid extends SpaceObject {

    /**
     * Constructs an Asteroid object with the given coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
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
     * Updates the asteroid's position.
     * In each tick, the asteroid moves downward (y increases).
     */
    @Override
    public void update() {
        y++; // Move downward
    }
}

