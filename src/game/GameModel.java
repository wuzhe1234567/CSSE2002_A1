package game;

import game.core.*;
import game.utility.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the game information and state.
 * It stores and manages all game objects (ship, enemies, bullets, health, etc.)
 * and handles game updates such as collisions, scoring, and life reduction.
 */
public class GameModel {
    public static final int GAME_HEIGHT = 20;
    public static final int GAME_WIDTH = 10;
    // Other constants can be defined here

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;

    private Ship ship;

    // Player's initial lives (represented by Health objects)
    private int playerLives = 3;
    // Current score, starting from 0
    private int score = 0;

    // Whether the ship is currently under shield protection
    private boolean shipShieldActive = false;
    // The currently active shield power-up object (if any)
    private ShieldPowerUp activeShieldPowerUp = null;

    public GameModel(Logger logger) {
        this.logger = logger;
    }

    public void setRandomSeed(int seed) {
        this.random.setSeed(seed);
    }

    public void addObject(SpaceObject object) {
        objects.add(object);
    }

    public List<SpaceObject> getSpaceObjects() {
        return objects;
    }

    public void updateGame(int tick) {
        // Update all objects
        for (SpaceObject obj : objects) {
            obj.update();
        }
        // Remove all ShieldPowerUp objects that are no longer active
        objects.removeIf(obj -> (obj instanceof ShieldPowerUp) && !((ShieldPowerUp)obj).isActive());
        // Check if the active shield has expired and clear the shield status if so
        if (shipShieldActive && activeShieldPowerUp != null && !activeShieldPowerUp.isActive()) {
            shipShieldActive = false;
            activeShieldPowerUp = null;
            logger.log("Shield expired.");
        }
    }

    /**
     * Collision detection:
     * 1. Ignore Health and Score objects (display only).
     * 2. If a collision involves a ShieldPowerUp, do nothing.
     * 3. If a collision involves a Bullet and an Enemy, increase score and remove both.
     * 4. If a collision involves the Ship and an Enemy, increase score:
     *    - If shield is active, remove only the enemy.
     *    - Otherwise, remove the enemy and call reducePlayerLives().
     * 5. If a collision involves the Ship and an Asteroid, do NOT increase score:
     *    - If shield is active, remove only the asteroid.
     *    - Otherwise, remove the asteroid and call reducePlayerLives().
     * 6. Other collisions (with matching coordinates) remove both objects.
     */
    public void checkCollisions() {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                SpaceObject a = objects.get(i);
                SpaceObject b = objects.get(j);
                // Ignore Health and Score objects
                if (a instanceof Health || b instanceof Health || a instanceof Score || b instanceof Score) {
                    continue;
                }
                // If collision involves a ShieldPowerUp, do nothing
                if (a instanceof ShieldPowerUp || b instanceof ShieldPowerUp) {
                    continue;
                }
                if (a.getX() == b.getX() && a.getY() == b.getY()) {
                    // 1. Bullet and Enemy collision: increase score, remove both
                    if ((a instanceof Bullet && b instanceof Enemy) || (a instanceof Enemy && b instanceof Bullet)) {
                        increaseScore();
                        toRemove.add(a);
                        toRemove.add(b);
                        logger.log("Bullet destroyed enemy. Score increased.");
                        continue;
                    }
                    // 2. Ship and Enemy collision: increase score
                    if ((a instanceof Ship && b instanceof Enemy) || (b instanceof Ship && a instanceof Enemy)) {
                        increaseScore();
                        if (shipShieldActive) {
                            logger.log("Shield protected the ship! Collision has no effect.");
                            // Remove only the enemy object
                            if (a instanceof Ship) {
                                toRemove.add(b);
                            } else {
                                toRemove.add(a);
                            }
                        } else {
                            // No shield: remove enemy and reduce player's life
                            if (a instanceof Ship) {
                                toRemove.add(b);
                            } else {
                                toRemove.add(a);
                            }
                            reducePlayerLives();
                        }
                        continue;
                    }
                    // 3. Ship and Asteroid collision: do NOT increase score
                    if ((a instanceof Ship && b instanceof Asteroid) || (b instanceof Ship && a instanceof Asteroid)) {
                        if (shipShieldActive) {
                            logger.log("Shield protected the ship! Collision with asteroid has no effect.");
                            if (a instanceof Ship) {
                                toRemove.add(b);
                            } else {
                                toRemove.add(a);
                            }
                        } else {
                            if (a instanceof Ship) {
                                toRemove.add(b);
                            } else {
                                toRemove.add(a);
                            }
                            reducePlayerLives();
                        }
                        continue;
                    }
                    // 4. Other collisions (if coordinates match): remove both
                    if (a.getX() == b.getX() && a.getY() == b.getY()) {
                        toRemove.add(a);
                        toRemove.add(b);
                        logger.log("Collision detected between " + a.render().toString() + " and " + b.render().toString());
                    }
                }
            }
        }
        objects.removeAll(toRemove);

        // If player lives have reached zero, remove the ship (game over)
        if (playerLives <= 0 && ship != null) {
            objects.remove(ship);
            logger.log("Game Over: Ship destroyed due to zero health.");
        }
    }

    /**
     * Increases the score by 1 and updates the Score object's display.
     */
    public void increaseScore() {
        score++;
        logger.log("Score increased to " + score);
        // Update Score object display
        for (SpaceObject obj : objects) {
            if (obj instanceof Score) {
                ((Score)obj).setScore(score);
            }
        }
    }

    /**
     * Reduces the player's lives by 1.
     * Removes the rightmost Health object and spawns a ShieldPowerUp at that position (lasting 5 seconds).
     */
    public void reducePlayerLives() {
        if (playerLives > 0) {
            playerLives--;
            logger.log("Player hit! Remaining lives: " + playerLives);
            // Remove the rightmost Health object (representing the rightmost heart)
            Health toRemove = null;
            for (SpaceObject obj : objects) {
                if (obj instanceof Health) {
                    Health h = (Health)obj;
                    if (toRemove == null || h.getX() > toRemove.getX()) {
                        toRemove = h;
                    }
                }
            }
            if (toRemove != null) {
                int shieldX = toRemove.getX();
                int shieldY = toRemove.getY();
                objects.remove(toRemove);
                // Spawn a ShieldPowerUp object lasting 5 seconds (e.g., 50 ticks)
                ShieldPowerUp shieldPowerUp = new ShieldPowerUp(shieldX, shieldY, 50);
                objects.add(shieldPowerUp);
                activeShieldPowerUp = shieldPowerUp;
                shipShieldActive = true;
                logger.log("Shield activated for 5 seconds.");
            }
        }
    }

    public void createShip() {
        ship = new Ship(GAME_WIDTH / 2, GAME_HEIGHT - 1);
        addObject(ship);
    }

    public Ship getShip() {
        return ship;
    }

    public int getPlayerLives() {
        return playerLives;
    }

    public int getScore() {
        return score;
    }
}
