package game;

import game.core.*;
import game.utility.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the game state.
 */
public class GameModel {
    public static final int GAME_HEIGHT = 20;
    public static final int GAME_WIDTH = 10;
    public static final int START_LEVEL = 1;
    public static final int START_SPAWN_RATE = 2;
    public static final int SPAWN_RATE_INCREASE = 5;
    public static final int SCORE_THRESHOLD = 100;
    public static final int ASTEROID_DAMAGE = 10;
    public static final int ENEMY_DAMAGE = 20;
    public static final double ENEMY_SPAWN_RATE = 0.5;
    public static final double POWER_UP_SPAWN_RATE = 0.25;
    
    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;
    private Ship ship;
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
    
    public void updateGame(int tick) {
        for (SpaceObject obj : objects) {
            obj.tick(tick);
        }
        spawnObjects();
        levelUp();
    }
    
    /**
     * Collision detection:
     * 1. Bullet and Enemy: remove both, add 1 score.
     * 2. Ship and Enemy: remove enemy, add 1 score.
     * 3. Ship and Asteroid: remove asteroid.
     * 4. Ship and HealthPowerUp/ShieldPowerUp: apply effect and remove the power-up.
     * 5. Other collisions: remove both.
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
                        hp.applyEffect(ship);
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship and ShieldPowerUp collision
                    if ((a instanceof Ship && b instanceof ShieldPowerUp) ||
                        (b instanceof Ship && a instanceof ShieldPowerUp)) {
                        ShieldPowerUp sp = (a instanceof ShieldPowerUp) ? (ShieldPowerUp)a : (ShieldPowerUp)b;
                        sp.applyEffect(ship);
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
    
    /**
     * Spawns new objects based on game level and spawn rate.
     */
    public void spawnObjects() {
        if (random.nextInt(100) < START_SPAWN_RATE) {
            addObject(new Enemy(random.nextInt(GAME_WIDTH), 0));
        }
    }
    
    /**
     * Levels up the game when the ship's score reaches the threshold.
     */
    public void levelUp() {
        if (ship != null && ship.getScore() >= SCORE_THRESHOLD * level) {
            level++;
            logger.log("Level up! Now level " + level);
            // 可添加提高 spawn rate 等逻辑
        }
    }
    
    /**
     * Fires a bullet from the ship.
     */
    public void fireBullet() {
        if (ship != null) {
            addObject(new Bullet(ship.getX(), ship.getY() - 1));
            logger.log("Bullet fired.");
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
    
   public void createShip() {
    ship = new Ship(GAME_WIDTH / 2, GAME_HEIGHT - 1);
    addObject(ship);
}
    
    public Ship getShip() {
        return ship;
    }
}

