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
    public static final int START_SPAWN_RATE = 2; // 固定每次 spawnObjects() 调用生成 Asteroid
    public static final int SCORE_THRESHOLD = 100; // 每级升级所需分数
    public static final int ASTEROID_DAMAGE = 10;
    public static final int ENEMY_DAMAGE = 20;
    public static final int SPAWN_RATE_INCREASE = 5;
    public static final double ENEMY_SPAWN_RATE = 0.5;
    public static final double POWER_UP_SPAWN_RATE = 0.25;

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;

    // Ship 对象由外部设置，通过 setShip() 方法；初始时模型中不包含任何 Ship
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
     * 设置 Ship 对象，并将其加入对象列表（如果尚未存在）。
     */
    public void setShip(Ship s) {
        this.ship = s;
        if (!objects.contains(s)) {
            objects.add(s);
        }
    }

    /**
     * 返回当前设置的 Ship 对象。
     */
    public Ship getShip() {
        return ship;
    }

    /**
     * 更新所有空间对象的状态，并在 tick > 0 时调用 spawnObjects()、levelUp() 与 checkCollisions()。
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
     * 进行碰撞检测：
     * 1. Bullet-Enemy：移除双方，并让 Ship 加 1 分。
     * 2. Ship-Enemy：移除敌人，并让 Ship 加 1 分。
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
                        if (ship != null) {
                            ship.addScore(1);
                        }
                        continue;
                    }
                    // Ship-Enemy 碰撞
                    if ((a instanceof Ship && b instanceof Enemy) ||
                        (a instanceof Enemy && b instanceof Ship)) {
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
                        hp.applyEffect(ship);
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship-ShieldPowerUp 碰撞
                    if ((a instanceof Ship && b instanceof ShieldPowerUp) ||
                        (a instanceof ShieldPowerUp && b instanceof Ship)) {
                        ShieldPowerUp sp = (a instanceof ShieldPowerUp) ? (ShieldPowerUp) a : (ShieldPowerUp) b;
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
     * 每次调用时在 (8,0) 生成一个 Asteroid，并记录日志。
     */
    public void spawnObjects() {
        addObject(new Asteroid(8, 0));
        logger.log("Asteroid spawned at (8,0).");
    }

    /**
     * 当 Ship 的得分达到升级阈值时，提升游戏等级。
     */
    public void levelUp() {
        if (ship != null && ship.getScore() >= SCORE_THRESHOLD * level) {
            level++;
            logger.log("Level up! Now level " + level);
        }
    }

    /**
     * 发射子弹：如果 Ship 存在，则移除 Ship 并添加一个 Bullet 对象，然后将 Ship 设为 null。
     * 这样模型中仅剩下一个空间对象（子弹）。
     */
    public void fireBullet() {
        if (ship != null) {
            objects.remove(ship);
            addObject(new Bullet(ship.getX(), ship.getY() - 1));
            logger.log("Bullet fired!");
            ship = null;
        }
    }

    public int getLevel() {
        return level;
    }
}

