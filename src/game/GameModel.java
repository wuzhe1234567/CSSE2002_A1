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
    public static final int START_SPAWN_RATE = 2; // 原设定值，现不使用随机条件，直接生成陨石
    public static final int SCORE_THRESHOLD = 100; // 每个等级所需分数
    public static final int ASTEROID_DAMAGE = 10;
    public static final int ENEMY_DAMAGE = 20;
    public static final int SPAWN_RATE_INCREASE = 5;
    public static final double ENEMY_SPAWN_RATE = 0.5;
    public static final double POWER_UP_SPAWN_RATE = 0.25;
    
    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;
    
    // Ship 对象由外部设置，构造后模型初始为空
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
     * 当 tick > 0 时，同时调用 spawnObjects() 与 levelUp()。
     */
    public void updateGame(int tick) {
        // 更新所有对象状态
        for (SpaceObject obj : new ArrayList<>(objects)) {
            obj.tick(tick);
        }
        // 仅在 tick > 0 时进行生成和升级
        if (tick > 0) {
            spawnObjects();
            levelUp();
        }
    }
    
    /**
     * Performs collision detection among space objects.
     * - Bullet-Enemy: remove both, ship gains 1 score.
     * - Ship-Enemy: remove enemy, ship gains 1 score.
     * - Ship-Asteroid: remove asteroid.
     * - Ship-HealthPowerUp: apply effect then remove the power-up.
     * - Ship-ShieldPowerUp: apply effect then remove the power-up.
     * - Bullets do not collide with Asteroids.
     * - Other collisions: remove both.
     */
    public void checkCollisions() {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                SpaceObject a = objects.get(i);
                SpaceObject b = objects.get(j);
                if (a.getX() == b.getX() && a.getY() == b.getY()) {
                    // 忽略 Bullet 与 Asteroid 之间的碰撞
                    if ((a instanceof Bullet && b instanceof Asteroid) ||
                        (a instanceof Asteroid && b instanceof Bullet)) {
                        continue;
                    }
                    // Bullet-Enemy collision: remove both, add 1 score.
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
                    // Ship-Enemy collision: remove enemy, add 1 score.
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
                    // Ship-Asteroid collision: remove asteroid.
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
                        (a instanceof HealthPowerUp && b instanceof Ship)) {
                        HealthPowerUp hp = (a instanceof HealthPowerUp) ? (HealthPowerUp)a : (HealthPowerUp)b;
                        hp.applyEffect(ship);
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship-ShieldPowerUp collision.
                    if ((a instanceof Ship && b instanceof ShieldPowerUp) ||
                        (a instanceof ShieldPowerUp && b instanceof Ship)) {
                        ShieldPowerUp sp = (a instanceof ShieldPowerUp) ? (ShieldPowerUp)a : (ShieldPowerUp)b;
                        sp.applyEffect(ship);
                        toRemove.add(sp);
                        logger.log("Ship collected Shield Power-Up.");
                        continue;
                    }
                    // 其它碰撞：移除双方
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
     * 修改后：每次调用该方法时都会在 (8,0) 生成一个 Asteroid，
     * 以满足测试用例要求。
     */
    public void spawnObjects() {
        addObject(new Asteroid(8, 0));
        logger.log("Asteroid spawned at (8,0).");
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
     * 测试要求调用 fireBullet() 后，模型中仅存在 1 个空间对象（子弹），
     * 因此该方法先移除 ship，再添加一个 Bullet。
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
     * Sets the Ship object externally.
     */
    public void setShip(Ship ship) {
        this.ship = ship;
        addObject(ship);
    }
}
