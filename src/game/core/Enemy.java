package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a standard enemy object.
 */
public class Enemy implements SpaceObject {
    protected int x;
    protected int y;
    
    /**
     * Constructs an Enemy with the specified coordinates.
     *
     * @param x the x-coordinate of the enemy.
     * @param y the y-coordinate of the enemy.
     */
    public Enemy(int x, int y) {
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
     * Returns the graphical representation of the enemy.
     * Note: The image file is expected to be at "assets/enemy.png" (relative to the project root).
     *
     * @return an ObjectGraphic representing the enemy.
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Enemy", "assets/enemy.png");
    }
    
    @Override
    public void tick(int tick) {
        y++; // Moves downward each tick.
    }
}
