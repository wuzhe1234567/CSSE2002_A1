package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a standard enemy object.
 */
public class Enemy implements SpaceObject {
    // 为了让 DescendingEnemy 能修改坐标，这里声明为 protected
    protected int x;
    protected int y;
    
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
    
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Enemy", "src/assets/enemy.png");
    }
    
    @Override
    public void tick(int tick) {
        y++; // Moves downward each tick
    }
}
