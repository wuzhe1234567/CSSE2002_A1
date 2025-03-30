package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an enemy object.
 */
public class Enemy extends SpaceObject {

    /**
     * Constructs an Enemy with the specified coordinates.
     *
     * @param x the x-coordinate of the enemy.
     * @param y the y-coordinate of the enemy.
     */
    public Enemy(int x, int y) {
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
        return new ObjectGraphic("Enemy", "src/assets/enemy.png");
    }

    @Override
    public void tick(int tick) {
        y++; // Move downward each tick
    }
}
