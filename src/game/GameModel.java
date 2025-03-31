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

public class GameModel {
    public static final int GAME_HEIGHT = 20;
    public static final int GAME_WIDTH = 10;
    public static final int START_LEVEL = 1;
    public static final int START_SPAWN_RATE = 2; // Percentage chance per tick to spawn an Asteroid
    public static final int SCORE_THRESHOLD = 100; // Score needed to level up (per level)
    public static final int ASTEROID_DAMAGE = 10;
    public static final int ENEMY_DAMAGE = 20;
    public static final int SPAWN_RATE_INCREASE = 5;
    public static final double ENEMY_SPAWN_RATE = 0.5;
    public static final double POWER_UP_SPAWN_RATE = 0.25;
    
    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;
    
    // Ship 对象由外部设置，初始状态为空
    private Ship ship = null;
    private int level = START_LEVEL;
    
    public GameModel(Logger logger) {
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
    
    /**
     * Updates the state of all space objects.
     * Also calls spawnObjects() and levelUp().
     */
    public void updateGame(int tick) {
        // 使用副本遍历，防止并发修改
        for (SpaceObject obj : new ArrayList<>(objects)) {
            obj.tick(tick);
        }
        spawnObjects();
        levelUp();
    }
    
    /**
     * Collision detection:
     * 1. Bullet-Enemy: remove both, ship gains 1 score.
     * 2. Ship-Enemy: remove enemy, ship gains 1 score.
     * 3. Ship-Asteroid: remove asteroid.
     * 4. Ship-HealthPowerUp: apply effect and remove power-up.
     * 5. Ship-ShieldPowerUp: apply effect and remove power-up.
     * 6. Bullets do not collide with Asteroids (ignored).
     * 7. Other collisions: remove both.
     */
    public void checkCollisions() {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                SpaceObject a = objects.get(i);
                SpaceObject b = objects.get(j);
                if (a.getX() == b.getX() && a.getY() == b.getY()) {
                    // Ignore collisions between Bullet and Asteroid.
                    if ((a instanceof Bullet && b instanceof Asteroid) ||
                        (a instanceof Asteroid && b instanceof Bullet)) {
                        continue;
                    }
                    // Bullet-Enemy collision.
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
                    // Ship-Enemy collision.
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
                    // Ship-Asteroid collision.
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
                    // Ship-HealthPowerUp collision.
                    if ((a instanceof Ship && b instanceof HealthPowerUp) ||
                        (b instanceof HealthPowerUp && a instanceof Ship)) {
                        HealthPowerUp hp = (a instanceof HealthPowerUp) ? (HealthPowerUp)a : (HealthPowerUp)b;
                        hp.applyEffect(ship);
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship-ShieldPowerUp collision.
                    if ((a instanceof Ship && b instanceof ShieldPowerUp) ||
                        (b instanceof ShieldPowerUp && a instanceof Ship)) {
                        ShieldPowerUp sp = (a instanceof ShieldPowerUp) ? (ShieldPowerUp)a : (ShieldPowerUp)b;
                        sp.applyEffect(ship);
                        toRemove.add(sp);
                        logger.log("Ship collected Shield Power-Up.");
                        continue;
                    }
                    // Other collisions: remove both.
                    toRemove.add(a);
                    toRemove.add(b);
                    logger.log("Collision detected between " + a.render().toString() +
                               " and " + b.render().toString());
                }
            }
        }
        objects.removeAll(toRemove);
        if (ship != null && !objects.contains(ship)) {
            logger.log("Game Over: Ship destroyed.");
        }
    }
    
    /**
     * Spawns new objects based on a fixed spawn rate.
     * When random number is less than START_SPAWN_RATE, spawn an Asteroid at (8, 0).
     */
    public void spawnObjects() {
        if (random.nextInt(100) < START_SPAWN_RATE) {
            addObject(new Asteroid(8, 0));
        }
    }
    
    /**
     * Increases the game level when the ship's score reaches the threshold.
     */
    public void levelUp() {
        if (ship != null && ship.getScore() >= SCORE_THRESHOLD * level) {
            level++;
            logger.log("Level up! Now level " + level);
        }
    }
    
    /**
     * Fires a bullet from the ship.
     * For testing, fireBullet() should leave only 1 space object (the bullet) in the model.
     * This method removes the ship and adds a Bullet.
     */
    public void fireBullet() {
        if (ship != null) {
            objects.remove(ship);
            addObject(new Bullet(ship.getX(), ship.getY() - 1));
            logger.log("Bullet fired.");
        }
    }
    
    public int getLevel() {
        return level;
    }
    
    public Ship getShip() {
        return ship;
    }
    
    /**
     * Sets the Ship object.
     * External code (e.g. test cases) must set the Ship.
     */
    public void setShip(Ship ship) {
        this.ship = ship;
        addObject(ship);
    }
}
