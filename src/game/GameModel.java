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
 */
public class GameModel {
    public static final int GAME_HEIGHT = 20;
    public static final int GAME_WIDTH = 10;

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;

    private Ship ship;

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
        // Update all objects by calling tick(int tick)
        for (SpaceObject obj : objects) {
            obj.tick(tick);
        }
    }

    /**
     * Collision detection:
     * 1. If a collision involves a Bullet and an Enemy, remove both.
     * 2. If a collision involves the Ship and an Enemy, remove the enemy.
     * 3. If a collision involves the Ship and an Asteroid, remove the asteroid.
     * 4. Other collisions with matching coordinates remove both objects.
     */
    public void checkCollisions() {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                SpaceObject a = objects.get(i);
                SpaceObject b = objects.get(j);
                if (a.getX() == b.getX() && a.getY() == b.getY()) {
                    // 1. Bullet and Enemy collision: remove both
                    if ((a instanceof Bullet && b instanceof Enemy) ||
                        (a instanceof Enemy && b instanceof Bullet)) {
                        toRemove.add(a);
                        toRemove.add(b);
                        logger.log("Bullet destroyed enemy.");
                        continue;
                    }
                    // 2. Ship and Enemy collision: remove enemy
                    if ((a instanceof Ship && b instanceof Enemy) ||
                        (b instanceof Ship && a instanceof Enemy)) {
                        if (a instanceof Ship) {
                            toRemove.add(b);
                        } else {
                            toRemove.add(a);
                        }
                        logger.log("Ship collided with enemy.");
                        continue;
                    }
                    // 3. Ship and Asteroid collision: remove asteroid
                    if ((a instanceof Ship && b instanceof Asteroid) ||
                        (b instanceof Ship && a instanceof Asteroid)) {
                        if (a instanceof Ship) {
                            toRemove.add(b);
                        } else {
                            toRemove.add(a);
                        }
                        logger.log("Ship collided with asteroid.");
                        continue;
                    }
                    // 4. Other collisions: remove both
                    toRemove.add(a);
                    toRemove.add(b);
                    logger.log("Collision detected between " + a.render().toString() +
                               " and " + b.render().toString());
                }
            }
        }
        objects.removeAll(toRemove);
        if (ship == null || !objects.contains(ship)) {
            logger.log("Game Over: Ship destroyed.");
        }
    }

    public void createShip() {
        // Use the two-parameter constructor for Ship.
        ship = new Ship(GAME_WIDTH / 2, GAME_HEIGHT - 1);
        addObject(ship);
    }

    public Ship getShip() {
        return ship;
    }
}
