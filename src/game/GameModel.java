package game;

import game.core.*;
import game.utility.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the game information and state. Stores and manipulates the game state.
 */
public class GameModel {
    public static final int GAME_HEIGHT = 20;
    public static final int GAME_WIDTH = 10;
    // 其它常量略……

    private final Random random = new Random();
    private List<SpaceObject> objects = new ArrayList<>();
    private Logger logger;

    private Ship ship;

    // 玩家初始生命数（用 Health 对象表示爱心）
    private int playerLives = 3;
    // 当前分数，初始为 0
    private int score = 0;

    // 是否处于护盾状态
    private boolean shipShieldActive = false;
    // 当前激活的护盾对象（若存在）
    private Shield activeShield = null;

    public GameModel(Logger logger) {
        this.logger = logger;
    }

    public void setRandomSeed(int seed) {
        this.random.setSeed(seed);
    }

    public void addObject(SpaceObject object) {
        objects.add(object);
    }

    public List<SpaceObject> getSpaceObjects() {
        return objects;
    }

    public void updateGame(int tick) {
        // 更新所有对象状态
        for (SpaceObject obj : objects) {
            obj.update();
        }
        // 移除所有失效的 Shield 对象
        objects.removeIf(obj -> (obj instanceof Shield) && !((Shield)obj).isActive());
        // 检查护盾是否失效，若 activeShield 已失效，则清除护盾状态
        if (shipShieldActive && activeShield != null && !activeShield.isActive()) {
            shipShieldActive = false;
            activeShield = null;
            logger.log("Shield expired.");
        }
    }

    /**
     * 碰撞检测：
     * 1. 忽略 Health 和 Score 对象（仅用于显示）。
     * 2. 如果碰撞涉及 Shield，则双方无影响。
     * 3. 如果碰撞涉及子弹与敌人，则增加分数，并移除双方。
     * 4. 如果碰撞涉及飞船与敌人，则增加分数：
     *      - 如果处于护盾状态，则只移除敌人；
     *      - 否则，移除敌人并减少玩家生命（调用 reducePlayerLives()）。
     * 5. 如果碰撞涉及飞船与陨石，则不增加分数：
     *      - 如果处于护盾状态，则只移除陨石；
     *      - 否则，移除陨石并减少玩家生命（调用 reducePlayerLives()）。
     * 6. 其他普通对象碰撞（坐标相同）则移除双方。
     */
    public void checkCollisions() {
        List<SpaceObject> toRemove = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                SpaceObject a = objects.get(i);
                SpaceObject b = objects.get(j);
                // 忽略 Health 和 Score 对象
                if (a instanceof Health || b instanceof Health || a instanceof Score || b instanceof Score) {
                    continue;
                }
                // 如果碰撞涉及 Shield，则双方无影响
                if (a instanceof Shield || b instanceof Shield) {
                    continue;
                }
                if (a.getX() == b.getX() && a.getY() == b.getY()) {
                    // 处理子弹与敌人的碰撞：增加分数，移除双方
                    if ((a instanceof Bullet && b instanceof Enemy) || (a instanceof Enemy && b instanceof Bullet)) {
                        increaseScore();
                        toRemove.add(a);
                        toRemove.add(b);
                        logger.log("Bullet destroyed enemy. Score increased.");
                        continue;
                    }
                    // 处理飞船与敌人的碰撞：增加分数
                    if ((a instanceof Ship && b instanceof Enemy) || (b instanceof Ship && a instanceof Enemy)) {
                        increaseScore();
                        if (shipShieldActive) {
                            logger.log("Shield protected the ship! Collision has no effect.");
                            // 移除碰撞中的敌人
                            if (a instanceof Ship) {
                                toRemove.add(b);
                            } else {
                                toRemove.add(a);
                            }
                        } else {
                            // 无护盾状态下：移除敌人，并减少生命
                            if (a instanceof Ship) {
                                toRemove.add(b);
                            } else {
                                toRemove.add(a);
                            }
                            reducePlayerLives();
                        }
                        continue;
                    }
                    // 处理飞船与陨石的碰撞：不增加分数
                    if ((a instanceof Ship && b instanceof Asteroid) || (b instanceof Ship && a instanceof Asteroid)) {
                        if (shipShieldActive) {
                            logger.log("Shield protected the ship! Collision with asteroid has no effect.");
                            if (a instanceof Ship) {
                                toRemove.add(b);
                            } else {
                                toRemove.add(a);
                            }
                        } else {
                            if (a instanceof Ship) {
                                toRemove.add(b);
                            } else {
                                toRemove.add(a);
                            }
                            reducePlayerLives();
                        }
                        continue;
                    }
                    // 其他普通对象碰撞：若坐标相同，则移除双方
                    if (a.getX() == b.getX() && a.getY() == b.getY()) {
                        toRemove.add(a);
                        toRemove.add(b);
                        logger.log("Collision detected between " + a.render().toString() + " and " + b.render().toString());
                    }
                }
            }
        }
        objects.removeAll(toRemove);

        // 如果玩家生命耗尽，则移除飞船（游戏结束）
        if (playerLives <= 0 && ship != null) {
            objects.remove(ship);
            logger.log("Game Over: Ship destroyed due to zero health.");
        }
    }

    /**
     * 增加分数，当飞船摧毁敌人或子弹摧毁敌人时调用，分数加 1，
     * 并更新对象列表中 Score 对象的显示（假设 Score 对象已添加）。
     */
    public void increaseScore() {
        score++;
        logger.log("Score increased to " + score);
        // 更新 Score 对象显示
        for (SpaceObject obj : objects) {
            if (obj instanceof Score) {
                ((Score)obj).setScore(score);
            }
        }
    }

    /**
     * 当飞船受到伤害时调用，减少玩家生命，
     * 并在被移除的 Health 对象位置生成护盾（持续 5 秒）。
     */
    public void reducePlayerLives() {
        if (playerLives > 0) {
            playerLives--;
            logger.log("Player hit! Remaining lives: " + playerLives);
            // 从 objects 中移除最右侧的 Health 对象（代表最右侧的爱心）
            Health toRemove = null;
            for (SpaceObject obj : objects) {
                if (obj instanceof Health) {
                    Health h = (Health) obj;
                    if (toRemove == null || h.getX() > toRemove.getX()) {
                        toRemove = h;
                    }
                }
            }
            if (toRemove != null) {
                int shieldX = toRemove.getX();
                int shieldY = toRemove.getY();
                objects.remove(toRemove);
                // 生成护盾对象，持续 5 秒（例如 50 tick）
                Shield shield = new Shield(shieldX, shieldY, 50);
                objects.add(shield);
                activeShield = shield;
                shipShieldActive = true;
                logger.log("Shield activated for 5 seconds.");
            }
        }
    }

    public void createShip() {
        ship = new Ship(GAME_WIDTH / 2, GAME_HEIGHT - 1);
        addObject(ship);
    }

    public Ship getShip() {
        return ship;
    }

    public int getPlayerLives() {
        return playerLives;
    }

    public int getScore() {
        return score;
    }
}
