package game.core;

import game.ui.ObjectGraphic;
import game.utility.Direction;
import game.exceptions.BoundaryExceededException;

/**
 * Represents the player's ship.
 * 
 * This class implements the Controllable interface and provides methods for
 * scoring, healing, taking damage, and querying health.
 */
public class Ship extends SpaceObject implements Controllable {
    private int health;
    private int score;

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
     * Updates the ship's state on each tick.
     * In this implementation, the ship does not move automatically.
     *
     * @param tick the current game tick
     */
    @Override
    public void tick(int tick) {
        // Ship's movement is controlled by player input, so no automatic update here.
    }

    /**
     * Moves the ship in the specified direction.
     * Throws a BoundaryExceededException if the move would take the ship out of bounds.
     *
     * @param direction the direction in which to move the ship
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
        // Check boundaries; assume valid x: [0, GameModel.GAME_WIDTH-1], y: [0, GameModel.GAME_HEIGHT-1]
        if (newX < 0 || newX >= GameModel.GAME_WIDTH || newY < 0 || newY >= GameModel.GAME_HEIGHT) {
            throw new BoundaryExceededException("Movement out of boundary: (" + newX + ", " + newY + ")");
        }
        x = newX;
        y = newY;
    }

    /**
     * Adds the specified number of points to the ship's score.
     *
     * @param points the points to add
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Returns the current score of the ship.
     *
     * @return the current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Heals the ship by the specified amount.
     *
     * @param amount the amount of health to add
     */
    public void heal(int amount) {
        health += amount;
    }

    /**
     * Reduces the ship's health by the specified amount.
     *
     * @param amount the damage amount to apply
     */
    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    /**
     * Returns the current health of the ship.
     *
     * @return the current health
     */
    public int getHealth() {
        return health;
    }
}
