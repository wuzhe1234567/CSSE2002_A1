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
     * Constructs a Ship with default position and health.
     * e.g. position (0,0) and health=100
     */
    public Ship() {
        super(0, 0); // Call the superclass's (x,y) initialization
        this.health = 100;
        this.score = 0;
    }

    /**
     * Constructs a Ship with the specified position and health.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @param health the initial health
     */
    public Ship(int x, int y, int health) {
        super(x, y);
        this.health = health;
        this.score = 0;
    }

    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Ship", "src/assets/ship.png");
    }

    @Override
    public void tick(int tick) {
        // Ship does not move automatically.
    }

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
        // Boundary check
        if (newX < 0 
            || newX >= GameModel.GAME_WIDTH
            || newY < 0
            || newY >= GameModel.GAME_HEIGHT) {
            throw new BoundaryExceededException(
                    "Movement out of boundary: (" + newX + ", " + newY + ")");
        }
        x = newX;
        y = newY;
    }

    /**
     * Adds the specified points to the ship's score.
     *
     * @param points the points to add.
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Returns the current score of the ship.
     *
     * @return the score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Increases the ship's health by the specified amount.
     *
     * @param amount the amount of health to add.
     */
    public void heal(int amount) {
        health += amount;
    }

    /**
     * Reduces the ship's health by the specified amount.
     * If the health falls below 0, it is set to 0.
     *
     * @param amount the damage amount.
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
     * @return the health.
     */
    public int getHealth() {
        return health;
    }
}
