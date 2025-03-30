package game;

import game.core.*;
import game.GameModel;
import game.core.Ship;
import game.core.SpaceObject;
import game.ui.UI;
import game.utility.Direction;

/**
 * 负责处理游戏流程和交互，将 UI 与 GameModel 连接起来，
 * 并管理游戏的更新、渲染和玩家输入。
 */
public class GameController {
    private long startTime;
    private UI ui;
    private GameModel model;

    // 标志游戏是否已经正式开始
    private boolean gameStarted = false;
    // 标志当前游戏是否处于暂停状态
    private boolean paused = false;

    /**
     * 使用指定 UI 和 GameModel 构造 GameController。
     *
     * @param ui    用于显示游戏的用户界面
     * @param model 用于维护游戏状态的模型
     */
    public GameController(UI ui, GameModel model) {
        this.ui = ui;
        this.model = model;
        this.startTime = System.currentTimeMillis(); // 记录开始时间
    }

    /**
     * 使用 ui::log 构造一个新的 GameModel 的构造器。
     *
     * @param ui 用于显示游戏的用户界面
     */
    public GameController(UI ui) {
        this(ui, new GameModel(ui::log));
    }

    /**
     * 游戏初始状态：
     * 只创建飞船、敌人和陨石，并显示在屏幕上（静止状态）。
     * 不启动游戏循环，也不响应正式的键盘输入，
     * 直到玩家按下 Enter 键后才正式开始游戏。
     */
    public void startGame() {
        // 创建飞船、敌人和陨石（初始状态下均静止显示）
        model.createShip();
        model.addObject(new Enemy(3, 1));
        model.addObject(new Asteroid(5, 1));
        model.addObject(new Health(0, 0)); // 第1个爱心
        model.addObject(new Health(1, 0)); // 第2个爱心
        model.addObject(new Health(2, 0)); // 第3个爱心
        model.addObject(new Score(1, 1, 0)); // 初始分数为 0，坐标 (0,1)
        renderGame();
        // 注册预启动阶段的键盘输入处理器，只监听 Enter 键
        ui.onKey(this::handlePreGameInput);
    }

    /**
     * 预启动阶段的键盘输入处理方法。
     * 当检测到 Enter 键时，切换到正式游戏状态：
     * 注册正式的键盘输入处理器，并启动游戏循环。
     *
     * @param key 玩家输入的键
     */
    public void handlePreGameInput(String key) {
        if (key.equals("\n") || key.equalsIgnoreCase("ENTER")) {
            gameStarted = true;
            // 切换到正式的键盘输入处理器
            ui.onKey(this::handlePlayerInput);
            // 启动游戏循环（正式开始游戏）
            ui.onStep(this::onTick);
        }
    }

    /**
     * 游戏每个 tick 调用：
     * - 渲染游戏画面
     * - 更新游戏中所有对象的状态
     * - 检查对象间的碰撞
     *
     * @param tick 当前游戏 tick 值
     */
    public void onTick(int tick) {
        renderGame();               // 更新画面显示
        model.updateGame(tick);     // 更新所有对象状态
        model.checkCollisions();    // 检查碰撞并处理
        // 如果飞船已被移除，则暂停游戏（游戏结束）
        if (model.getShip() == null) {
            ui.pause();
        }
    }

    /**
     * 调用 UI 的 render 方法，将 GameModel 中所有的 SpaceObject 传递给 UI 进行渲染。
     */
    public void renderGame() {
        ui.render(model.getSpaceObjects());
    }

    /**
     * 正式游戏阶段的键盘输入处理方法：
     * - W/A/S/D 控制飞船移动，
     * - F 发射子弹，
     * - P 切换暂停状态（并使所有对象静止）。
     *
     * @param key 玩家输入的键
     */
    public void handlePlayerInput(String key) {
        // 如果按下的是 P 键，则切换暂停状态
        if (key.equalsIgnoreCase("P")) {
            paused = !paused;
            ui.pause();
            return;
        }
        // 如果处于暂停状态，不响应其他按键
        if (paused) {
            return;
        }

        Ship ship = model.getShip();
        if (ship == null) return;
        switch (key.toUpperCase()) {
            case "W":
                ship.move(Direction.UP);
                break;
            case "A":
                ship.move(Direction.LEFT);
                break;
            case "S":
                ship.move(Direction.DOWN);
                break;
            case "D":
                ship.move(Direction.RIGHT);
                break;
            case "F":
                // 发射子弹：子弹从飞船上方生成
                model.addObject(new Bullet(ship.getX(), ship.getY() - 1));
                break;
            default:
                break;
        }
    }
}
