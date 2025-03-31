package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing an object with a position.
 * Provides concrete implementations of getX() and getY().
 */
public abstract class ObjectWithPosition implements SpaceObject {
    protected int x;
    protected int y;
    
    /**
     * Constructs an object with the specified coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public ObjectWithPosition(int x, int y) {
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
    
    // render() 和 tick() 由子类实现
    @Override
    public abstract ObjectGraphic render();
    
    @Override
    public abstract void tick(int tick);
}

