package game;

import game.core.SpaceObject;
import game.core.Asteroid;
import game.core.Bullet;
import game.core.Enemy;
import game.core.HealthPowerUp;
import game.core.ShieldPowerUp;
import game.core.Ship;
import game.utility.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the game information and state. Stores and manipulates the game state.
 */
public class GameModel {
    public static final int GAME_HEIGHT = 20;
    public static final int GAME_WIDTH = 10;
    public static final int START_LEVEL = 1;
    public static final int START_SPAWN_RATE = 2;
    public static final int SCORE_THRESHOLD = 100;
    public static final int ASTEROID_DAMAGE = 10;
    public static final int ENEMY_DAMAGE = 20;
    public static final int SPAWN_RATE_INCREASE = 5;
    public static final double ENEMY_SPAWN_RATE = 0.5;
    public static final double POWER_UP_SPAWN_RATE = 0.25;

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;
    // Ship is stored separately; it is NOT part of objects list.
    private Ship ship;
    private int level = START_LEVEL;
    private int spawnRate = START_SPAWN_RATE;

    /**
     * Constructs a GameModel with the given logger.
     * Creates a default Ship placed at (GAME_WIDTH/2, GAME_HEIGHT-2) with health 100.
     *
     * @param logger the logger instance for logging messages.
     */
    public GameModel(Logger logger) {
        this.logger = logger;
        // 创建默认的 Ship 对象，位置设为屏幕中间偏下
        this.ship = new Ship(GAME_WIDTH / 2, GAME_HEIGHT - 2, 100);
    }

    /**
     * Sets the seed for the Random instance.
     *
     * @param seed the seed value.
     */
    public void setRandomSeed(int seed) {
        random.setSeed(seed);
    }

    /**
     * Adds a SpaceObject to the game.
     *
     * @param object the SpaceObject to be added.
     */
    public void addObject(SpaceObject object) {
        if (object != null) {
            objects.add(object);
        }
    }

    /**
     * Returns the list of all SpaceObjects currently tracked by the game.
     *
     * @return the list of SpaceObjects.
     */
    public List<SpaceObject> getSpaceObjects() {
        return objects;
    }

    /**
     * Updates the game state by moving all objects and removing off-screen objects.
     * An object is off-screen if its y-coordinate > GAME_HEIGHT.
     *
     * @param tick the current tick value.
     */
    public void updateGame(int tick) {
        // Move all objects
        for (SpaceObject obj : new ArrayList<>(objects)) {
            obj.tick(tick);
        }
        // Remove off-screen objects (y > GAME_HEIGHT)
        objects.removeIf(obj -> obj.getY() > GAME_HEIGHT);
        // Spawn new objects, level up, and check collisions if tick > 0
        if (tick > 0) {
            spawnObjects();
            levelUp();
            checkCollisions();
        }
    }

    /**
     * Detects and handles collisions.
     * First, checks collisions between the Ship and any SpaceObject:
     * - If colliding with a PowerUp: apply its effect and log "Power-up collected: <symbol>".
     * - If colliding with an Asteroid: ship takes ASTEROID_DAMAGE and log "Hit by asteroid! Health reduced by ...".
     * - If colliding with an Enemy: ship takes ENEMY_DAMAGE and log "Hit by enemy! Health reduced by ...".
     * Colliding objects are removed.
     * Then, checks collisions between Bullets and Enemies; if colliding, both are removed.
     */
    public void checkCollisions() {
        // List to hold objects to be removed
        List<SpaceObject> toRemove = new ArrayList<>();

        // Check collisions between ship and other objects
        for (SpaceObject obj : new ArrayList<>(objects)) {
            if (obj.getX() == ship.getX() && obj.getY() == ship.getY()) {
                if (obj instanceof HealthPowerUp || obj instanceof ShieldPowerUp) {
                    obj.tick(0); // just in case; not needed normally
                    if (obj instanceof HealthPowerUp) {
                        ((HealthPowerUp) obj).applyEffect(ship);
                    } else {
                        ((ShieldPowerUp) obj).applyEffect(ship);
                    }
                    logger.log("Power-up collected: " + obj.render().toString());
                    toRemove.add(obj);
                } else if (obj instanceof Asteroid) {
                    ship.takeDamage(ASTEROID_DAMAGE);
                    logger.log("Hit by asteroid! Health reduced by " + ASTEROID_DAMAGE + ".");
                    toRemove.add(obj);
                } else if (obj instanceof Enemy) {
                    ship.takeDamage(ENEMY_DAMAGE);
                    logger.log("Hit by enemy! Health reduced by " + ENEMY_DAMAGE + ".");
                    toRemove.add(obj);
                }
            }
        }

        // Check collisions between Bullets and Enemies
        List<SpaceObject> bulletCollisions = new ArrayList<>();
        for (SpaceObject a : new ArrayList<>(objects)) {
            if (a instanceof Bullet) {
                for (SpaceObject b : new ArrayList<>(objects)) {
                    if (b instanceof Enemy && a.getX() == b.getX() && a.getY() == b.getY()) {
                        bulletCollisions.add(a);
                        bulletCollisions.add(b);
                    }
                }
            }
        }
        toRemove.addAll(bulletCollisions);

        // Remove all collided objects
        objects.removeAll(toRemove);

        // If ship is hit and its health is reduced to 0 or below, log game over.
        // (这里未要求移除 ship，但可根据需要扩展)
        if (ship.getHealth() <= 0) {
            logger.log("Game Over: Ship destroyed.");
        }
    }

    /**
     * Spawns new objects at y = 0 using exactly 6 calls to random.nextInt() and 1 call to random.nextBoolean().
     * The order is as follows:
     * 1. Check if an Asteroid should spawn (random.nextInt(100) < spawnRate).
     *    If yes, spawn an Asteroid at x = random.nextInt(GAME_WIDTH), unless that x equals ship's x.
     * 2. Check if an Enemy should spawn (random.nextInt(100) < spawnRate * ENEMY_SPAWN_RATE).
     *    If yes, spawn an Enemy at x = random.nextInt(GAME_WIDTH), unless that x equals ship's x.
     * 3. Check if a PowerUp should spawn (random.nextInt(100) < spawnRate * POWER_UP_SPAWN_RATE).
     *    If yes, spawn a PowerUp at x = random.nextInt(GAME_WIDTH) (unless colliding with ship),
     *    and then use random.nextBoolean() to determine which type:
     *    if true, spawn a ShieldPowerUp; else, spawn a HealthPowerUp.
     */
    public void spawnObjects() {
        // 1. Asteroid spawn
        if (random.nextInt(100) < spawnRate) {
            int x = random.nextInt(GAME_WIDTH);
            if (x != ship.getX()) {
                addObject(new Asteroid(x, 0));
            }
        }
        // 2. Enemy spawn
        if (random.nextInt(100) < spawnRate * ENEMY_SPAWN_RATE) {
            int x = random.nextInt(GAME_WIDTH);
            if (x != ship.getX()) {
                addObject(new Enemy(x, 0));
            }
        }
        // 3. PowerUp spawn
        if (random.nextInt(100) < spawnRate * POWER_UP_SPAWN_RATE) {
            int x = random.nextInt(GAME_WIDTH);
            if (x != ship.getX()) {
                // Determine type using random.nextBoolean()
                if (random.nextBoolean()) {
                    addObject(new ShieldPowerUp(x, 0));
                } else {
                    addObject(new HealthPowerUp(x, 0));
                }
            }
        }
    }

    /**
     * If the Ship's score is greater than or equal to (level * SCORE_THRESHOLD),
     * increases the game level by 1 and increases the spawn rate by SPAWN_RATE_INCREASE.
     * Logs "Level Up! Welcome to Level {new level}. Spawn rate increased to {new spawn rate}%."
     */
    public void levelUp() {
        if (ship.getScore() >= level * SCORE_THRESHOLD) {
            level++;
            spawnRate += SPAWN_RATE_INCREASE;
            logger.log("Level Up! Welcome to Level " + level + ". Spawn rate increased to " + spawnRate + "%.");
        }
    }

    /**
     * Fires a bullet from the Ship's current position.
     * Creates a new Bullet at (ship.getX(), ship.getY() - 1) and logs "Core.Bullet fired!".
     */
    public void fireBullet() {
        if (ship != null) {
            addObject(new Bullet(ship.getX(), ship.getY() - 1));
            logger.log("Core.Bullet fired!");
        } else {
            addObject(new Bullet(5, GAME_HEIGHT - 2));
            logger.log("Core.Bullet fired!");
        }
    }

    /**
     * Returns the current game level.
     *
     * @return the current level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the Ship instance in the game.
     *
     * @return the current Ship.
     */
    public Ship getShip() {
        return ship;
    }
}
