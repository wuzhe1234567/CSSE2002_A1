package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a bullet object.
 */
public class Bullet implements SpaceObject {
    private int x;
    private int y;
    
    /**
     * Constructs a Bullet with the specified coordinates.
     *
     * @param x the x-coordinate of the bullet.
     * @param y the y-coordinate of the bullet.
     */
    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
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
        y--; // Moves upward each tick
    }
}

