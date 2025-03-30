package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an asteroid object.
 */
public class Asteroid extends SpaceObject {

    /**
     * Constructs an Asteroid with the specified coordinates.
     *
     * @param x the x-coordinate of the asteroid.
     * @param y the y-coordinate of the asteroid.
     */
    public Asteroid(int x, int y) {
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
     * Returns the graphical representation of the asteroid.
     *
     * @return an ObjectGraphic representing the asteroid.
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Asteroid", "src/assets/asteroid.png");
    }

    /**
     * Updates the asteroid's state on each tick.
     * In this implementation, the asteroid moves downward.
     *
     * @param tick the current game tick.
     */
    @Override
    public void tick(int tick) {
        y++; // Move downward each tick
    }
}

