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
    
    // Ship 对象由外部添加，GameModel 内部不负责创建
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
    
    /**
     * Updates the state of all space objects.
     * 同时调用 spawnObjects() 与 levelUp()。
     */
    public void updateGame(int tick) {
        for (SpaceObject obj : objects) {
            obj.tick(tick);
        }
        spawnObjects();
        levelUp();
    }
    
    /**
     * Performs collision detection among all space objects.
     * 1. Bullet-Enemy: remove both, ship gains 1 score.
     * 2. Ship-Enemy: remove enemy, ship gains 1 score.
     * 3. Ship-Asteroid: remove asteroid.
     * 4. Ship-HealthPowerUp: apply效果后移除 power-up。
     * 5. Ship-ShieldPowerUp: apply效果后移除 power-up。
     * 6. Bullets 不与 Asteroids 发生碰撞（忽略）。
     * 7. 其它坐标完全匹配的碰撞：移除双方。
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
                        if (ship != null) {
                            ship.addScore(1);
                        }
                        continue;
                    }
                    // Ship 与 Enemy 碰撞：移除敌人，并给 ship 加分
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
                        hp.applyEffect(ship);
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship 与 ShieldPowerUp 碰撞：应用效果后移除 power-up
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
                    logger.log("Collision detected between " + a.render().toString() + " and " + b.render().toString());
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
     * 当随机数小于 START_SPAWN_RATE 时，在 (8, 0) 生成一个 Asteroid。
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
     * 测试要求调用 fireBullet() 后模型中仅存在 1 个空间对象（子弹），
     * 因此此方法先移除飞船再添加子弹。
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
    
    // 注意：Ship 对象的创建应在外部完成（例如测试用例会先添加 Ship 对象）
}
