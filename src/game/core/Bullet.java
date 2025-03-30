package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a bullet object.
 */
public class Bullet implements SpaceObject {
    private int x;
    private int y;
    
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
    
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Bullet", "src/assets/bullet.png");
    }
    
    @Override
    public void tick(int tick) {
        y--; // Moves upward each tick
    }
}
