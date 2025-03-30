package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a bullet object.
 */
public class Bullet extends SpaceObject {

    public Bullet(int x, int y) {
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
        return new ObjectGraphic("Bullet", "src/assets/bullet.png");
    }
    
    @Override
    public void tick(int tick) {
        y--; // Move upward each tick
    }
}


