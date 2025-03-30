package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an asteroid object.
 */
public class Asteroid implements SpaceObject {
    private int x;
    private int y;
    
    public Asteroid(int x, int y) {
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
        return new ObjectGraphic("Asteroid", "src/assets/asteroid.png");
    }
    
    @Override
    public void tick(int tick) {
        y++; // Moves downward each tick
    }
}

