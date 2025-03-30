package game.core;

import game.ui.ObjectGraphic;

/**
 * Public interface representing a space object in the game.
 */
public interface SpaceObject {
    int getX();
    int getY();
    ObjectGraphic render();
    void tick(int tick);
}

