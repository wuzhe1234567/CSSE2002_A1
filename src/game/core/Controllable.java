package game.core;

import game.exceptions.BoundaryExceededException;
import game.utility.Direction;

/**
 * Public abstract class representing a controllable game object.
 * Implements SpaceObject and stores position.
 */
public abstract class Controllable implements SpaceObject {
    protected int x;
    protected int y;
    
    /**
     * Constructs a controllable object with the given coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public Controllable(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // 提供 getX() 和 getY() 的具体实现
    @Override
    public int getX() {
        return x;
    }
    
    @Override
    public int getY() {
        return y;
    }
    
    /**
     * Moves the object in the specified direction.
     * 此处提供一个默认实现（实际应用中建议在子类中覆盖）。
     *
     * @param direction the direction to move.
     * @throws BoundaryExceededException if the move exceeds game boundaries.
     */
    public void move(game.utility.Direction direction) throws BoundaryExceededException {
        // 默认实现抛出异常，要求子类重写
        throw new UnsupportedOperationException("move method not implemented");
    }
}
