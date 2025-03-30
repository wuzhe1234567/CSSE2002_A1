package game.core;

import game.ui.ObjectGraphic;
import game.utility.Direction;
import game.exceptions.BoundaryExceededException;
import game.GameModel;

/**
 * Represents the player's ship.
 */
public class Ship extends Controllable {
    private int health;
    private int score;

    /**
     * Constructs a Ship with the specified coordinates.
     * The ship's initial health is set to 100 and score to 0.
     *
     * @param x the x-coordinate of the ship.
     * @param y the y-coordinate of the ship.
     */
    public Ship(int x, int y) {
        super(x, y);
        this.health = 100;
        this.score = 0;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override 
    public int getY() {
        return y;
    }

    /**
     * Returns the graphical representation of the ship.
     *
     * @return an ObjectGraphic representing the ship.
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Ship", "src/assets/ship.png");
    }

    /**
     * Updates the ship's state on each tick.
     * In this implementation, the ship does not move automatically.
     *
     * @param tick the current game tick.
     */
    @Override
    public void tick(int tick) {
        // Movement is controlled by player input.
    }

    /**
     * Moves the ship in the specified direction by one unit.
     * Throws a BoundaryExceededException if the move exceeds game boundaries.
     *
     * @param direction the direction to move the ship.
     * @throws BoundaryExceededException if the move exceeds game boundaries.
     */
    @Override
    public void move(Direction direction) throws BoundaryExceededException {
        int newX = x;
        int newY = y;
        switch(direction) {
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
        if(newX < 0 || newX >= GameModel.GAME_WIDTH || newY < 0 || newY >= GameModel.GAME_HEIGHT) {
            throw new BoundaryExceededException("Movement out of boundary: (" + newX + ", " + newY + ")");
        }
        x = newX;
        y = newY;
    }

    // 以下方法用于管理分数与健康
    public void addScore(int points) {
        score += points;
    }

    public int getScore() {
        return score;
    }

    public void heal(int amount) {
        health += amount;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if(health < 0) {
            health = 0;
        }
    }

    public int getHealth() {
        return health;
    }
}
