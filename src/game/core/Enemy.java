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
     * The text representation is set to "👾" as required.
     *
     * @return an ObjectGraphic representing the enemy.
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("👾", "assets/enemy.png");
    }
    
    /**
     * Updates the enemy's state each tick.
     * In this case, the enemy moves downward by increasing its y-coordinate.
     *
     * @param tick the current game tick.
     */
    @Override
    public void tick(int tick) {
        y++; // Moves downward each tick
    }
}

