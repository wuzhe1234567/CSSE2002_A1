package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an asteroid object.
 */
public class Asteroid implements SpaceObject {
    private int xpos;
    private int ypos;

    /**
     * Constructs an Asteroid with the specified coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public Asteroid(int x, int y) {
        this.xpos = x;
        this.ypos = y;
    }

    @Override
    public int getX() {
        return xpos;
    }

    @Override
    public int getY() {
        return ypos;
    }

    /**
     * Returns the graphical representation of the asteroid.
     * The text representation is set to "🌑" (a moon emoji) as required.
     *
     * @return an ObjectGraphic representing the asteroid.
     */
    @Override
    public ObjectGraphic render() {
        // Note: Ensure the image path is correct.
        // If stored in the project's root assets folder, use "assets/asteroid.png"
        return new ObjectGraphic("🌑", "assets/asteroid.png");
    }

    @Override
    public void tick(int tick) {
        ypos++; // Moves downward each tick
    }
}

