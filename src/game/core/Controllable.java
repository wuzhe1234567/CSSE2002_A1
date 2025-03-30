package game.core;

import game.exceptions.BoundaryExceededException;
import game.utility.Direction;

/**
 * Public interface representing a controllable game object.
 * Extends SpaceObject.
 */
public interface Controllable extends SpaceObject {
    void move(Direction direction) throws BoundaryExceededException;
}

