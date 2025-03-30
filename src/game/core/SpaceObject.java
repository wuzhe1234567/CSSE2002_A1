package game.core;

import game.ui.ObjectGraphic;

/**
 * Public abstract interface representing a space object in the game.
 */
public interface SpaceObject {
    public abstract int getX();
    public abstract int getY();
    public abstract ObjectGraphic render();
    public abstract void tick(int tick);
}

