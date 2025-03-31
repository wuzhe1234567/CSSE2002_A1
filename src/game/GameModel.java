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
    public static final int START_SPAWN_RATE = 2; // not necessarily used
    public static final int SCORE_THRESHOLD = 100;
    public static final int ASTEROID_DAMAGE = 10;
    public static final int ENEMY_DAMAGE = 20;
    public static final int SPAWN_RATE_INCREASE = 5;
    public static final double ENEMY_SPAWN_RATE = 0.5;
    public static final double POWER_UP_SPAWN_RATE = 0.25;

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;

    // 存储当前的 Ship 引用
    private Ship ship;
    private int level = START_LEVEL;

    public GameModel(Logger logger) {
        this.logger = logger;
    }

    /**
     * 在测试或外部需要时设置随机种子
     */
    public void setRandomSeed(int seed) {
        random.setSeed(seed);
    }

    /**
     * 添加任何 SpaceObject 到模型中。
     * 如果添加的是 Ship，就保存到 this.ship 以便 getShip() 返回。
     */
    public void addObject(SpaceObject object) {
        objects.add(object);
        if (object instanceof Ship) {
            this.ship = (Ship) object;
        }
    }

    /**
     * 返回所有空间对象的列表
     */
    public List<SpaceObject> getSpaceObjects() {
        return objects;
    }

    /**
     * 返回 Ship 对象（若从未添加过 Ship，则返回 null）
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * 游戏更新逻辑：先对所有对象调用 tick(tick)，
     * 若 tick > 0，则再调用 spawnObjects()、levelUp()、checkCollisions()。
     */
    public void updateGame(int tick) {
        // 先更新
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
     * 检测并处理碰撞
     */
    public void checkCollisions() {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                SpaceObject a = objects.get(i);
                SpaceObject b = objects.get(j);
                if (a.getX() == b.getX() && a.getY() == b.getY()) {
                    // 忽略子弹和陨石的碰撞
                    if ((a instanceof Bullet && b instanceof Asteroid) ||
                        (a instanceof Asteroid && b instanceof Bullet)) {
                        continue;
                    }
                    // Bullet-Enemy
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
                    // Ship-Enemy
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
                    // Ship-Asteroid
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
                    // Ship-HealthPowerUp
                    if ((a instanceof Ship && b instanceof HealthPowerUp) ||
                        (a instanceof HealthPowerUp && b instanceof Ship)) {
                        HealthPowerUp hp = (a instanceof HealthPowerUp)
                                ? (HealthPowerUp) a : (HealthPowerUp) b;
                        if (ship != null) {
                            hp.applyEffect(ship);
                        }
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship-ShieldPowerUp
                    if ((a instanceof Ship && b instanceof ShieldPowerUp) ||
                        (a instanceof ShieldPowerUp && b instanceof Ship)) {
                        ShieldPowerUp sp = (a instanceof ShieldPowerUp)
                                ? (ShieldPowerUp) a : (ShieldPowerUp) b;
                        if (ship != null) {
                            sp.applyEffect(ship);
                        }
                        toRemove.add(sp);
                        logger.log("Ship collected Shield Power-Up.");
                        continue;
                    }
                    // 其他碰撞
                    toRemove.add(a);
                    toRemove.add(b);
                    logger.log("Collision detected between " + a.render().toString()
                               + " and " + b.render().toString());
                }
            }
        }
        objects.removeAll(toRemove);
        // 如果 ship 已经被移除出列表，则说明游戏结束
        if (ship != null && !objects.contains(ship)) {
            logger.log("Game Over: Ship destroyed.");
        }
    }

    /**
     * 每次调用都在 (8,0) 生成一个陨石，并记录日志
     */
    public void spawnObjects() {
        objects.add(new Asteroid(8, 0));
        logger.log("Asteroid spawned at (8,0).");
    }

    /**
     * 若 ship 的分数 >= SCORE_THRESHOLD * level 则升级
     */
    public void levelUp() {
        if (ship != null && ship.getScore() >= SCORE_THRESHOLD * level) {
            level++;
            logger.log("Level up! Now level " + level);
        }
    }

    /**
     * fireBullet：先移除 ship，再添加子弹
     */
    public void fireBullet() {
        if (ship != null && objects.contains(ship)) {
            objects.remove(ship);
            objects.add(new Bullet(ship.getX(), ship.getY() - 1));
            logger.log("Bullet fired.");
        }
    }

    public int getLevel() {
        return level;
    }
}
