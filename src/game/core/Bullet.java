package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a bullet object.
 */
public class Bullet implements SpaceObject {
    private int xpos;
    private int ypos;

    /**
     * Constructs a Bullet with the specified coordinates.
     *
     * @param x the x-coordinate of the bullet.
     * @param y the y-coordinate of the bullet.
     */
    public Bullet(int x, int y) {
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
     * Returns the graphical representation of the bullet.
     * Here we use the image located at "assets/bullet.png".
     *
     * @return an ObjectGraphic representing the bullet.
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Bullet", "assets/bullet.png");
    }

    @Override
    public void tick(int tick) {
        ypos--; // Moves upward each tick
    }
}


