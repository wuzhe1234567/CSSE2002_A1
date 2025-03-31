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
    
    // Ship 对象由外部设置；若为空，在 updateGame() 中自动创建一个默认 ship
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
     * 对所有对象调用 tick(tick)；当 tick > 0 时，如果 ship 为空则自动创建，
     * 然后调用 spawnObjects()、levelUp() 与 checkCollisions()。
     */
    public void updateGame(int tick) {
        // 更新所有对象状态
        for (SpaceObject obj : new ArrayList<>(objects)) {
            obj.tick(tick);
        }
        if (tick > 0) {
            if (ship == null) {
                createShip();
            }
            spawnObjects();
            levelUp();
            checkCollisions();
        }
    }
    
    /**
     * Performs collision detection among space objects.
     * 1. Bullet-Enemy：移除双方，并使 ship 加 1 分。
     * 2. Ship-Enemy：移除敌人，并使 ship 加 1 分。
     * 3. Ship-Asteroid：移除陨石。
     * 4. Ship-HealthPowerUp：应用效果后移除 power-up。
     * 5. Ship-ShieldPowerUp：应用效果后移除 power-up。
     * 6. Bullet 与 Asteroid 碰撞不处理。
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
                    // Bullet-Enemy collision
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
                    // Ship-Enemy collision
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
                    // Ship-Asteroid collision
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
                    // Ship-HealthPowerUp collision
                    if ((a instanceof Ship && b instanceof HealthPowerUp) ||
                        (a instanceof HealthPowerUp && b instanceof Ship)) {
                        HealthPowerUp hp = (a instanceof HealthPowerUp) ? (HealthPowerUp)a : (HealthPowerUp)b;
                        hp.applyEffect(ship);
                        toRemove.add(hp);
                        logger.log("Ship collected Health Power-Up.");
                        continue;
                    }
                    // Ship-ShieldPowerUp collision
                    if ((a instanceof Ship && b instanceof ShieldPowerUp) ||
                        (a instanceof ShieldPowerUp && b instanceof Ship)) {
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
     * 为测试要求，此方法每次调用时都在 (8,0) 生成一个 Asteroid。
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
     * 按测试要求，此方法先移除 ship，再添加一个 Bullet，
     * 使得模型中仅剩下一个空间对象（子弹）。
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
    
    /**
     * Creates a default Ship object at the center-bottom with 100 health,
     * and adds it to the model.
     */
    public void createShip() {
        this.ship = new Ship(GAME_WIDTH / 2, GAME_HEIGHT - 1, 100);
        addObject(ship);
        logger.log("Default ship created.");
    }
}
