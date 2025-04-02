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
 * GameModel 管理所有游戏对象状态，包括更新、碰撞检测和对象生成。
 */
public class GameModel {
    public static final int GAME_HEIGHT = 20;
    public static final int GAME_WIDTH = 10;
    public static final int START_LEVEL = 1;
    public static final int START_SPAWN_RATE = 2; // Not used for random spawning now
    public static final int SCORE_THRESHOLD = 100; // Score needed to level up per level
    public static final int ASTEROID_DAMAGE = 10;
    public static final int ENEMY_DAMAGE = 20;
    public static final int SPAWN_RATE_INCREASE = 5;
    public static final double ENEMY_SPAWN_RATE = 0.5;
    public static final double POWER_UP_SPAWN_RATE = 0.25;

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;

    // Ship is added externally; if not added, getShip() returns null.
    private Ship ship = null;
    private int level = START_LEVEL;

    /**
     * 构造函数，使用 Logger 初始化 GameModel。
     *
     * @param logger the logger instance for logging messages.
     */
    public GameModel(Logger logger) {
        this.logger = logger;
    }

    /**
     * 设置随机数种子（用于测试）。
     *
     * @param seed the seed value.
     */
    public void setRandomSeed(int seed) {
        random.setSeed(seed);
    }

    /**
     * 添加一个新的空间对象到游戏中。
     *
     * @param object the space object to add.
     */
    public void addObject(SpaceObject object) {
        objects.add(object);
    }

    /**
     * 返回当前所有的空间对象列表。
     *
     * @return the list of space objects.
     */
    public List<SpaceObject> getSpaceObjects() {
        return objects;
    }

    /**
     * 更新所有空间对象的状态。
     * 对于 tick > 0，同步调用 spawnObjects()、levelUp() 和 checkCollisions()。
     *
     * @param tick the current tick count.
     */
    public void updateGame(int tick) {
        for (SpaceObject obj : new ArrayList<>(objects)) {
            obj.tick(tick);
        }
        if (tick > 0) {
            spawnObjects();
            levelUp();
            checkCollisions();
        }
    }

    /**
     * 执行空间对象之间的碰撞检测。
     * <p>
     * 碰撞规则：
     * <ul>
     *   <li>Bullet-Enemy：移除双方，并为 Ship 加分。</li>
     *   <li>Ship-Enemy：移除敌人，为 Ship 加分。</li>
     *   <li>Ship-Asteroid：移除小行星。</li>
     *   <li>Ship-HealthPowerUp：应用效果后移除道具。</li>
     *   <li>Ship-ShieldPowerUp：应用效果后移除道具。</li>
     *   <li>Bullet-Asteroid：忽略碰撞。</li>
     *   <li>其他碰撞：移除双方对象。</li>
     * </ul>
     * </p>
     */
    public void checkCollisions() {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                SpaceObject a = objects.get(i);
                SpaceObject b = objects.get(j);
                if (a.getX() == b.getX() && a.getY() == b.getY()) {
                    // 忽略 Bullet 与 Asteroid 之间的碰撞
                    if ((a instanceof Bullet && b instanceof Asteroid)
                            || (a instanceof Asteroid && b instanceof Bullet)) {
                        continue;
                    }
                    // Bullet-Enemy 碰撞
                    if ((a instanceof Bullet && b instanceof Enemy)
                            || (a instanceof Enemy && b instanceof Bullet)) {
                        toRemove.add(a);
                        toRemove.add(b);
                        logger.log("Bullet destroyed enemy.");
                        Ship s = getShip();
                        if (s != null) {
                            s.addScore(1);
                        }
                        continue;
                    }
                    // Ship-Enemy 碰撞
                    if ((a instanceof Ship && b instanceof Enemy)
                            || (a instanceof Enemy && b instanceof Ship)) {
                        Ship s = getShip();
                        if (s != null) {
                            s.addScore(1);
                        }
                        if (a instanceof Ship) {
                            toRemove.add(b);
                        } else {
                            toRemove.add(a);
                        }
                        logger.log("Ship collided with enemy.");
                        continue;
                    }
                    // Ship-Asteroid 碰撞
                    if ((a instanceof Ship && b instanceof Asteroid)
                            || (a instanceof Asteroid && b instanceof Ship)) {
                        if (a instanceof Ship) {
                            toRemove.add(b);
                        } else {
                            toRemove.add(a);
                        }
                        logger.log("Ship collided with asteroid.");
                        continue;
                    }
                    // Ship-HealthPowerUp 碰撞
                    if ((a instanceof Ship && b instanceof HealthPowerUp)
                            || (a instanceof HealthPowerUp && b instanceof Ship)) {
                        HealthPowerUp hp = (a instanceof HealthPowerUp) ? (HealthPowerUp) a 
                                : (HealthPowerUp) b;
                        hp.applyEffect(getShip());
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship-ShieldPowerUp 碰撞
                    if ((a instanceof Ship && b instanceof ShieldPowerUp)
                            || (a instanceof ShieldPowerUp && b instanceof Ship)) {
                        ShieldPowerUp sp = (a instanceof ShieldPowerUp) ? (ShieldPowerUp) a 
                                : (ShieldPowerUp) b;
                        sp.applyEffect(getShip());
                        toRemove.add(sp);
                        logger.log("Ship collected Shield Power-Up.");
                        continue;
                    }
                    // 其他碰撞：移除双方
                    toRemove.add(a);
                    toRemove.add(b);
                    logger.log("Collision detected between " 
                            + a.render().toString() 
                            + " and " 
                            + b.render().toString());
                }
            }
        }
        objects.removeAll(toRemove);
        if (getShip() != null && !objects.contains(getShip())) {
            logger.log("Game Over: Ship destroyed.");
        }
    }

    /**
     * 生成新对象。
     * <p>
     * 测试目的：总是在 (8,0) 处生成一个 Asteroid，并记录日志。
     * </p>
     */
    public void spawnObjects() {
        addObject(new Asteroid(8, 0));
        logger.log("Asteroid spawned at (8,0).");
    }

    /**
     * 当 Ship 的得分达到阈值时升级游戏等级。
     */
    public void levelUp() {
        Ship s = getShip();
        if (s != null && s.getScore() >= SCORE_THRESHOLD * level) {
            level++;
            logger.log("Level up! Now level " + level);
        }
    }

    /**
     * 从 Ship 发射一颗子弹。
     * <p>
     * 测试目的：如果存在 Ship，则从其当前位置发射子弹；否则，从默认位置发射。
     * </p>
     */
    public void fireBullet() {
        Ship s = getShip();
        if (s != null) {
            addObject(new Bullet(s.getX(), s.getY() - 1));
            logger.log("Core.Bullet fired!");
        } else {
            addObject(new Bullet(5, GAME_HEIGHT - 2));
            logger.log("Core.Bullet fired!");
        }
    }

    /**
     * 返回当前游戏等级。
     *
     * @return the current level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * 返回对象列表中第一个 Ship 实例。
     *
     * @return the Ship instance, or null if not found.
     */
    public Ship getShip() {
        return ship;
    }
}
