package game.core;

import game.ui.ObjectGraphic;

/**
 * Public interface representing a space object in the game.
 */
public interface SpaceObject {
    public int getX();
    public int getY();
    public ObjectGraphic render();
    public void tick(int tick);
}
