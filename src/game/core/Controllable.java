package game.core;

import game.exceptions.BoundaryExceededException;

/**
 * Interface for controllable game objects.
 */
public interface Controllable {
    /**
     * Moves the object in the specified direction.
     *
     * @param direction the direction to move
     * @throws BoundaryExceededException if the move would exceed game boundaries
     */
    void move(game.utility.Direction direction) throws BoundaryExceededException;
}

