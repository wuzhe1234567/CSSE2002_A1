package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing an enemy that descends faster.
 */
public abstract class DescendingEnemy extends Enemy {

    public DescendingEnemy(int x, int y) {
        super(x, y);
    }
    
    @Override
    public void tick(int tick) {
        y += 2; // Descends 2 units per tick
    }
    
    // render() remains abstract to force concrete subclass to provide its own implementation.
    @Override
    public abstract ObjectGraphic render();
}
