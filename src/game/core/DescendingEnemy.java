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
        // Descend 2 units per tick instead of 1.
        y += 2;
    }
    
    /**
     * render() remains abstract, so concrete subclasses (or anonymous classes) must provide an implementation.
     */
    @Override
    public abstract ObjectGraphic render();
}
