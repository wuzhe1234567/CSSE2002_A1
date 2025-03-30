package game.core;

import game.ui.ObjectGraphic;
import game.utility.Direction;
import game.exceptions.BoundaryExceededException;
import game.GameModel; // 用于获取 GAME_WIDTH, GAME_HEIGHT

/**
 * Represents the player's ship.
 */
public class Ship extends Controllable {
    private int health;

    /**
     * Constructs a Ship with the specified coordinates and initial health.
     *
     * @param x the x-coordinate of the ship
     * @param y the y-coordinate of the ship
     * @param health the initial health of the ship
     */
    public Ship(int x, int y, int health) {
        super(x, y);
        this.health = health;
    }

    /**
     * Returns the graphical representation of the ship,
     * using the image from the relative path "src/assets/ship.png".
     *
     * @return an ObjectGraphic representing the ship
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Ship", "src/assets/ship.png");
    }

    /**
     * Updates the ship's state on each tick.
     * In this implementation, the ship does not move automatically.
     *
     * @param tick the current game tick
     */
    @Override
    public void tick(int tick) {
        // Movement is controlled by key input, so no automatic update.
    }

    /**
     * Moves the ship in the specified direction by one unit.
     * Throws BoundaryExceededException if the move would exceed game boundaries.
     *
     * @param direction the direction to move the ship
     * @throws BoundaryExceededException if the move exceeds game boundaries
     */
    @Override
    public void move(Direction direction) throws BoundaryExceededException {
        int newX = x;
        int newY = y;
        switch (direction) {
            case UP:
                newY = y - 1;
                break;
            case DOWN:
                newY = y + 1;
                break;
            case LEFT:
                newX = x - 1;
                break;
            case RIGHT:
                newX = x + 1;
                break;
        }
        // Use GameModel constants for boundary checking
        if (newX < 0 || newX >= GameModel.GAME_WIDTH || newY < 0 || newY >= GameModel.GAME_HEIGHT) {
            throw new BoundaryExceededException("Movement out of boundary: (" + newX + ", " + newY + ")");
        }
        x = newX;
        y = newY;
    }

    // 可以根据需要添加其他方法，比如 takeDamage, heal, etc.
}
