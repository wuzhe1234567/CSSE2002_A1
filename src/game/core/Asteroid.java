package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an asteroid object.
 */
public class Asteroid implements SpaceObject {
    private int xPos;
    private int yPos;

    /**
     * Constructs an Asteroid with the specified coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public Asteroid(int x, int y) {
        this.xPos = x;
        this.yPos = y;
    }

    @Override
    public int getX() {
        return xPos;
    }

    @Override
    public int getY() {
        return yPos;
    }

    /**
     * Returns the graphical representation of the asteroid.
     * The text representation is set to "🌑" (a moon emoji) as required.
     *
     * @return an ObjectGraphic representing the asteroid.
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("🌑", "assets/asteroid.png");
    }

    @Override
    public void tick(int tick) {
        yPos++; // Moves downward each tick
    }
}
