package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a standard enemy object.
 */
public class Enemy extends SpaceObject {

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
