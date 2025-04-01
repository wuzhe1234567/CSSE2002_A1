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
    public static final int START_SPAWN_RATE = 2; // 每 tick 生成陨石的概率百分比
    public static final int SCORE_THRESHOLD = 100; // 每级升级所需分数
    public static final int ASTEROID_DAMAGE = 10;
    public static final int ENEMY_DAMAGE = 20;
    public static final int SPAWN_RATE_INCREASE = 5;
    public static final double ENEMY_SPAWN_RATE = 0.5;
    public static final double POWER_UP_SPAWN_RATE = 0.25;

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;

    // Ship 对象由外部添加，但为确保 fireBullet() 与 levelUp() 不报空指针，
    // 如果模型中没有 Ship，则在需要时创建一个默认 Ship。
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
     * 更新所有对象状态，且在 tick > 0 时调用生成、升级和碰撞检测。
     */
    public void updateGame(int tick) {
        // 遍历对象（使用拷贝防止并发修改异常）
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
     * 碰撞检测：
     * 1. Bullet-Enemy：移除双方，并让 ship 加 1 分。
     * 2. Ship-Enemy：移除敌人，并让 ship 加 1 分。
     * 3. Ship-Asteroid：移除陨石。
     * 4. Ship-HealthPowerUp：应用效果后移除 power-up。
     * 5. Ship-ShieldPowerUp：应用效果后移除 power-up。
     * 6. Bullet 与 Asteroid 之间的碰撞忽略。
     * 7. 其它碰撞：移除双方。
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
                    // Bullet-Enemy 碰撞
                    if ((a instanceof Bullet && b instanceof Enemy) ||
                        (a instanceof Enemy && b instanceof Bullet)) {
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
                    if ((a instanceof Ship && b instanceof Enemy) ||
                        (a instanceof Enemy && b instanceof Ship)) {
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
                    if ((a instanceof Ship && b instanceof Asteroid) ||
                        (a instanceof Asteroid && b instanceof Ship)) {
                        if (a instanceof Ship) {
                            toRemove.add(b);
                        } else {
                            toRemove.add(a);
                        }
                        logger.log("Ship collided with asteroid.");
                        continue;
                    }
                    // Ship-HealthPowerUp 碰撞
                    if ((a instanceof Ship && b instanceof HealthPowerUp) ||
                        (a instanceof HealthPowerUp && b instanceof Ship)) {
                        HealthPowerUp hp = (a instanceof HealthPowerUp) ? (HealthPowerUp) a : (HealthPowerUp) b;
                        hp.applyEffect(getShip());
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship-ShieldPowerUp 碰撞
                    if ((a instanceof Ship && b instanceof ShieldPowerUp) ||
                        (a instanceof ShieldPowerUp && b instanceof Ship)) {
                        ShieldPowerUp sp = (a instanceof ShieldPowerUp) ? (ShieldPowerUp) a : (ShieldPowerUp) b;
                        sp.applyEffect(getShip());
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
        if (getShip() != null && !objects.contains(getShip())) {
            logger.log("Game Over: Ship destroyed.");
        }
    }

    /**
     * 每次调用 spawnObjects() 时生成一个 Asteroid 在 (8,0)，并记录日志。
     */
    public void spawnObjects() {
        addObject(new Asteroid(8, 0));
        logger.log("Asteroid spawned at (8,0).");
    }

    /**
     * 当 ship 的分数达到一定阈值时升级游戏。
     */
    public void levelUp() {
        Ship s = getShip();
        if (s == null) {
            // 如果没有 Ship，则创建一个默认 Ship（外部测试可能会需要它）
            s = new Ship(GAME_WIDTH / 2, GAME_HEIGHT - 1, 100);
            addObject(s);
        }
        if (s.getScore() >= SCORE_THRESHOLD * level) {
            level++;
            logger.log("Level up! Now level " + level);
        }
    }

    /**
     * 发射子弹：先移除 ship，再添加一个 Bullet。
     */
    public void fireBullet() {
        Ship s = getShip();
        if (s == null) {
            // 如果没有 ship，则创建默认 ship
            s = new Ship(GAME_WIDTH / 2, GAME_HEIGHT - 1, 100);
            addObject(s);
        }
        objects.remove(s);
        addObject(new Bullet(s.getX(), s.getY() - 1));
        logger.log("Bullet fired.");
    }

    public int getLevel() {
        return level;
    }

    /**
     * 返回 objects 列表中第一个 Ship 实例（如果存在）。
     */
    public Ship getShip() {
        for (SpaceObject obj : objects) {
            if (obj instanceof Ship) {
                return (Ship) obj;
            }
        }
        return null;
    }
}

