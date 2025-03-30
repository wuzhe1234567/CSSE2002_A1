package game.core;

import game.ui.ObjectGraphic;
import game.utility.Direction;
import game.exceptions.BoundaryExceededException;
import game.GameModel;  // 加入此 import 以引用 GAME_WIDTH, GAME_HEIGHT

/**
 * Represents the player's ship.
 * Implements the Controllable interface.
 */
public class Ship extends SpaceObject implements Controllable {
    private int health;
    private int score; // 如果需要管理分数，可以保留

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
        this.score = 0;
    }

    /**
     * Returns the graphical representation of the ship,
     * using the image resource from the relative path "src/assets/ship.png".
     *
     * @return an ObjectGraphic representing the ship
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Ship", "src/assets/ship.png");
    }

    /**
     * The ship does not move automatically on each tick.
     *
     * @param tick the current game tick
     */
    @Override
    public void tick(int tick) {
        // No automatic movement; movement is controlled by key input.
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
        // Check boundaries using GameModel constants
        if (newX < 0 || newX >= GameModel.GAME_WIDTH || newY < 0 || newY >= GameModel.GAME_HEIGHT) {
            throw new BoundaryExceededException("Movement out of boundary: (" + newX + ", " + newY + ")");
        }
        x = newX;
        y = newY;
    }

    // 以下方法可根据需要添加，例如 addScore, heal, takeDamage, getScore, getHealth 等
    // 这里省略，因本次重点不在此
}
