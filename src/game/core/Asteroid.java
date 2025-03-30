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

    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Asteroid", "src/assets/asteroid.png");
    }

    @Override
    public void tick(int tick) {
        y++; // Move downward each tick
    }
}


