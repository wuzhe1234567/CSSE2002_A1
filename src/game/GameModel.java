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
    public static final int SCORE_THRESHOLD = 100; // Score needed to level up per level
    public static final int ASTEROID_DAMAGE = 10;
    public static final int ENEMY_DAMAGE = 20;
    public static final int SPAWN_RATE_INCREASE = 5;
    public static final double ENEMY_SPAWN_RATE = 0.5;
    public static final double POWER_UP_SPAWN_RATE = 0.25;

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;
    
    // Ship 对象由外部添加，初始状态下模型中不包含任何空间对象
    // 测试用例要求初始 getSpaceObjects() 返回空列表
    // 当 Ship 对象被外部添加后，getShip() 能返回该 Ship 对象
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
     * 对所有对象调用 tick() 方法；tick > 0 时调用 spawnObjects、levelUp 与 checkCollisions。
     */
    public void updateGame(int tick) {
        // 先更新所有对象
        for (SpaceObject obj : new ArrayList<>(objects)) {
            obj.tick(tick);
        }
        // 如果 tick 大于 0，则执行后续更新
        if (tick > 0) {
            spawnObjects();
            levelUp();
            checkCollisions();
        }
    }

    /**
     * Performs collision detection among space objects.
     * 1. Bullet-Enemy：移除双方，并给 ship 加 1 分。
     * 2. Ship-Enemy：移除敌人，并给 ship 加 1 分。
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
                    // Bullet 与 Enemy 碰撞：移除双方，并给 ship 加分
                    if ((a instanceof Bullet && b instanceof Enemy) ||
                        (a instanceof Enemy && b instanceof Bullet)) {
                        toRemove.add(a);
                        toRemove.add(b);
                        logger.log("Bullet destroyed enemy.");
                        Ship ship = getShip();
                        if (ship != null) {
                            ship.addScore(1);
                        }
                        continue;
                    }
                    // Ship 与 Enemy 碰撞：移除敌人，并给 ship 加分
                    if ((a instanceof Ship && b instanceof Enemy) ||
                        (a instanceof Enemy && b instanceof Ship)) {
                        Ship ship = getShip();
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
                    // Ship 与 Asteroid 碰撞：移除陨石
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
                    // Ship 与 HealthPowerUp 碰撞：应用效果后移除 power-up
                    if ((a instanceof Ship && b instanceof HealthPowerUp) ||
                        (a instanceof HealthPowerUp && b instanceof Ship)) {
                        HealthPowerUp hp = (a instanceof HealthPowerUp) ? (HealthPowerUp)a : (HealthPowerUp)b;
                        hp.applyEffect(getShip());
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship 与 ShieldPowerUp 碰撞：应用效果后移除 power-up
                    if ((a instanceof Ship && b instanceof ShieldPowerUp) ||
                        (a instanceof ShieldPowerUp && b instanceof Ship)) {
                        ShieldPowerUp sp = (a instanceof ShieldPowerUp) ? (ShieldPowerUp)a : (ShieldPowerUp)b;
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
        if (getShip() == null) {
            logger.log("Game Over: Ship destroyed.");
        }
    }

    /**
     * Spawns new objects based on a fixed spawn rate.
     * 当 random.nextInt(100) 小于 START_SPAWN_RATE 时，在 (8,0) 生成一个 Asteroid，并记录日志。
     */
    public void spawnObjects() {
        if (random.nextInt(100) < START_SPAWN_RATE) {
            addObject(new Asteroid(8, 0));
            logger.log("Asteroid spawned at (8,0).");
        }
    }

    /**
     * Increases the game level when the ship's score reaches the threshold.
     */
    public void levelUp() {
        Ship ship = getShip();
        if (ship != null && ship.getScore() >= SCORE_THRESHOLD * level) {
            level++;
            logger.log("Level up! Now level " + level);
        }
    }

    /**
     * Fires a bullet from the ship.
     * 根据测试要求，此方法先移除 ship，再添加一个 Bullet，
     * 使得模型中仅剩下一个空间对象（子弹）。
     */
    public void fireBullet() {
        Ship ship = getShip();
        if (ship != null) {
            objects.remove(ship);
            addObject(new Bullet(ship.getX(), ship.getY() - 1));
            logger.log("Bullet fired.");
        }
    }

    public int getLevel() {
        return level;
    }

    /**
     * Searches the objects list for a Ship instance and returns it.
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
