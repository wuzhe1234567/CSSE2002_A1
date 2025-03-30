package game.core;

import game.exceptions.BoundaryExceededException;
import game.utility.Direction;

/**
 * Abstract class representing a controllable game object.
 * (如果需要，Controllable 可作为接口实现，但此处用抽象类方式满足规范要求)
 */
public abstract class Controllable extends Ship {
    /**
     * Constructs a controllable Ship.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public Controllable(int x, int y) {
        super(x, y);
    }

    /**
     * Moves the object in the specified direction.
     * Default implementation throws an UnsupportedOperationException.
     *
     * @param direction the direction to move.
     * @throws BoundaryExceededException if the move exceeds game boundaries.
     */
    public void move(game.utility.Direction direction) throws BoundaryExceededException {
        throw new UnsupportedOperationException("move method not implemented");
    }
}
