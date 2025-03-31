package game;

import game.core.*;
import game.utility.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {
    public static final int GAME_HEIGHT = 20;
    public static final int GAME_WIDTH = 10;
    public static final int START_LEVEL = 1;
    public static final int START_SPAWN_RATE = 2; // 初始生成概率
    public static final int SCORE_THRESHOLD = 100; // 每级升级所需分数
    public static final int ASTEROID_DAMAGE = 10;
    public static final int ENEMY_DAMAGE = 20;
    public static final int SPAWN_RATE_INCREASE = 5; // 升级时生成概率的增量
    public static final double ENEMY_SPAWN_RATE = 0.5;
    public static final double POWER_UP_SPAWN_RATE = 0.25;

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;

    private Ship ship = null;
    private int level = START_LEVEL;
    // 新增 spawnRate，用于跟踪当前生成概率，初始值为 START_SPAWN_RATE
    private int spawnRate = START_SPAWN_RATE;

    public GameModel(Logger logger) {
        this.logger = logger;
    }

    public void setRandomSeed(int seed) {
        random.setSeed(seed);
    }

    /**
     * 让外部可以设置 ship，并添加到 objects 列表
     */
    public void setShip(Ship ship) {
        this.ship = ship;
        addObject(ship);
    }

    public void addObject(SpaceObject object) {
        objects.add(object);
    }

    public List<SpaceObject> getSpaceObjects() {
        return objects;
    }

    /**
     * 每次 updateGame 时：
     * - 对所有对象执行 tick(tick)
     * - 若 tick > 0，则 spawnObjects()、levelUp()、checkCollisions()
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
     * 生成对象的逻辑：根据当前 spawnRate，每次都在 (8,0) 生成 Asteroid
     */
    public void spawnObjects() {
        if (random.nextInt(100) < spawnRate) {
            addObject(new Asteroid(8, 0));
            logger.log("Asteroid spawned at (8,0).");
        }
    }

    /**
     * levelUp：当 ship 的分数 >= SCORE_THRESHOLD * level 时：
     * 1. level++
     * 2. spawnRate += SPAWN_RATE_INCREASE
     * 3. 打印日志 "Level Up! Welcome to Level X. Spawn rate increased to Y."
     */
    public void levelUp() {
        if (ship != null && ship.getScore() >= SCORE_THRESHOLD * level) {
            level++;
            spawnRate += SPAWN_RATE_INCREASE;
            logger.log("Level Up! Welcome to Level " + level
                    + ". Spawn rate increased to " + spawnRate + ".");
        }
    }

    /**
     * 检测碰撞逻辑
     */
    public void checkCollisions() {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                SpaceObject a = objects.get(i);
                SpaceObject b = objects.get(j);
                if (a.getX() == b.getX() && a.getY() == b.getY()) {
                    // Bullet 与 Asteroid 碰撞忽略
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
                        hp.applyEffect(ship);
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship-ShieldPowerUp
                    if ((a instanceof Ship && b instanceof ShieldPowerUp) ||
                        (a instanceof ShieldPowerUp && b instanceof Ship)) {
                        ShieldPowerUp sp = (a instanceof ShieldPowerUp)
                                ? (ShieldPowerUp) a : (ShieldPowerUp) b;
                        sp.applyEffect(ship);
                        toRemove.add(sp);
                        logger.log("Ship collected Shield Power-Up.");
                        continue;
                    }
                    // 其他碰撞：移除双方
                    toRemove.add(a);
                    toRemove.add(b);
                    logger.log("Collision detected between "
                            + a.render().toString() + " and "
                            + b.render().toString());
                }
            }
        }
        objects.removeAll(toRemove);
        if (ship != null && !objects.contains(ship)) {
            logger.log("Game Over: Ship destroyed.");
        }
    }

    /**
     * fireBullet：移除 Ship，添加 Bullet
     */
    public void fireBullet() {
        if (ship != null && objects.contains(ship)) {
            objects.remove(ship);
            addObject(new Bullet(ship.getX(), ship.getY() - 1));
            logger.log("Bullet fired.");
        }
    }

    public int getLevel() {
        return level;
    }

    /**
     * 返回外部通过 setShip() 添加的 Ship 实例
     */
    public Ship getShip() {
        return ship;
    }
}
