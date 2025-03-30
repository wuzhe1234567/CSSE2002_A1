package game;

import game.core.*;
import game.utility.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the game information and state.
 * It stores and manages all game objects (ship, enemies, bullets, power-ups, etc.)
 * and handles game updates such as collisions.
 */
public class GameModel {
    public static final int GAME_HEIGHT = 20;
    public static final int GAME_WIDTH = 10;

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;
    private Ship ship;

    public GameModel(game.utility.Logger logger) {
        this.logger = logger;
    }

    public void setRandomSeed(int seed) {
        random.setSeed(seed);
    }

    public void addObject(SpaceObject object) {
        objects.add(object);
    }

    public List<SpaceObject> getSpaceObjects() {
        return objects;
    }

    public void updateGame(int tick) {
        for (SpaceObject obj : objects) {
            obj.tick(tick);
        }
    }

    /**
     * Collision detection:
     * 1. If a collision involves a Bullet and an Enemy, remove both and increase score.
     * 2. If a collision involves the Ship and an Enemy, remove the enemy and increase score.
     * 3. If a collision involves the Ship and an Asteroid, remove the asteroid.
     * 4. If the Ship collides with a HealthPowerUp or ShieldPowerUp, apply its effect and remove the power-up.
     * 5. Other collisions (with matching coordinates) remove both objects.
     */
    public void checkCollisions() {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                SpaceObject a = objects.get(i);
                SpaceObject b = objects.get(j);
                if (a.getX() == b.getX() && a.getY() == b.getY()) {
                    // Bullet and Enemy collision
                    if ((a instanceof Bullet && b instanceof Enemy) ||
                        (a instanceof Enemy && b instanceof Bullet)) {
                        toRemove.add(a);
                        toRemove.add(b);
                        logger.log("Bullet destroyed enemy.");
                        if (ship != null) {
                            ship.addScore(1);
                        }
                        continue;
                    }
                    // Ship and Enemy collision
                    if ((a instanceof Ship && b instanceof Enemy) ||
                        (b instanceof Ship && a instanceof Enemy)) {
                        if (ship != null) {
                            ship.addScore(1);
                        }
                        if (a instanceof Ship) {
                            toRemove.add(b);
                        } else {
                            toRemove.add(a);
                        }
                        logger.log("Ship collided with enemy.");
                        continue;
                    }
                    // Ship and Asteroid collision
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
                    // Ship and HealthPowerUp collision
                    if ((a instanceof Ship && b instanceof HealthPowerUp) ||
                        (b instanceof Ship && a instanceof HealthPowerUp)) {
                        HealthPowerUp hp = (a instanceof HealthPowerUp) ? (HealthPowerUp)a : (HealthPowerUp)b;
                        hp.apply(ship);
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship and ShieldPowerUp collision
                    if ((a instanceof Ship && b instanceof ShieldPowerUp) ||
                        (b instanceof Ship && a instanceof ShieldPowerUp)) {
                        ShieldPowerUp sp = (a instanceof ShieldPowerUp) ? (ShieldPowerUp)a : (ShieldPowerUp)b;
                        sp.apply(ship);
                        toRemove.add(sp);
                        logger.log("Ship collected Shield Power-Up.");
                        continue;
                    }
                    // Other collisions: remove both
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
        ship = new Ship(GAME_WIDTH / 2, GAME_HEIGHT - 1);
        addObject(ship);
    }

    public Ship getShip() {
        return ship;
    }
}
