package game;

import game.core.*;
import game.utility.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the game information and state.
 * It stores and manages all game objects (ship, enemies, bullets, etc.)
 * and handles game updates such as collisions.
 *
 * Score and Health mechanisms have been removed.
 */
public class GameModel {
    public static final int GAME_HEIGHT = 20;
    public static final int GAME_WIDTH = 10;
    // Other constants can be defined here

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;

    private Ship ship;

    // Score and Health mechanisms removed:
    // private int playerLives = 3;
    // private int score = 0;
    // private boolean shipShieldActive = false;
    // private ShieldPowerUp activeShieldPowerUp = null;

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
        // Remove all expired ShieldPowerUp objects
        objects.removeIf(obj -> (obj instanceof ShieldPowerUp) && !((ShieldPowerUp)obj).isActive());
    }

    /**
     * Collision detection:
     * 1. Ignore ShieldPowerUp objects.
     * 2. If a collision involves a Bullet and an Enemy, remove both.
     * 3. If a collision involves the Ship and an Enemy, remove the enemy.
     * 4. If a collision involves the Ship and an Asteroid, remove the asteroid.
     * 5. Other collisions with matching coordinates remove both objects.
     */
    public void checkCollisions() {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                SpaceObject a = objects.get(i);
                SpaceObject b = objects.get(j);
                // If a ShieldPowerUp is involved, do nothing
                if (a instanceof ShieldPowerUp || b instanceof ShieldPowerUp) {
                    continue;
                }
                if (a.getX() == b.getX() && a.getY() == b.getY()) {
                    // Bullet and Enemy collision: remove both
                    if ((a instanceof Bullet && b instanceof Enemy) || (a instanceof Enemy && b instanceof Bullet)) {
                        toRemove.add(a);
                        toRemove.add(b);
                        logger.log("Bullet destroyed enemy.");
                        continue;
                    }
                    // Ship and Enemy collision: remove the enemy
                    if ((a instanceof Ship && b instanceof Enemy) || (b instanceof Ship && a instanceof Enemy)) {
                        if (a instanceof Ship) {
                            toRemove.add(b);
                        } else {
                            toRemove.add(a);
                        }
                        logger.log("Ship collided with enemy.");
                        continue;
                    }
                    // Ship and Asteroid collision: remove the asteroid
                    if ((a instanceof Ship && b instanceof Asteroid) || (b instanceof Ship && a instanceof Asteroid)) {
                        if (a instanceof Ship) {
                            toRemove.add(b);
                        } else {
                            toRemove.add(a);
                        }
                        logger.log("Ship collided with asteroid.");
                        continue;
                    }
                    // Other collisions: if coordinates match, remove both
                    if (a.getX() == b.getX() && a.getY() == b.getY()) {
                        toRemove.add(a);
                        toRemove.add(b);
                        logger.log("Collision detected between " + a.render().toString() + " and " + b.render().toString());
                    }
                }
            }
        }
        objects.removeAll(toRemove);
    }

    public void createShip() {
        ship = new Ship(GAME_WIDTH / 2, GAME_HEIGHT - 1);
        addObject(ship);
    }

    public Ship getShip() {
        return ship;
    }
}
