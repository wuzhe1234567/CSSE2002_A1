package game;

import game.core.*;
import game.exceptions.BoundaryExceededException;
import game.ui.UI;
import game.utility.Direction;

/**
 * 管理游戏流程和交互。
 * 本类持有 UI 和 GameModel 的引用，更新模型状态并在 UI 上显示统计数据。
 */
public class GameController {
    private UI ui;
    private GameModel model;
    private long startTime;
    private boolean gameStarted = false;
    private boolean paused = false;

    // 使用指定 UI 创建控制器，内部构造一个新的 GameModel（日志方法由 UI 提供）
    public GameController(UI ui) {
        this.ui = ui;
        this.model = new GameModel(ui::log);
        this.startTime = System.currentTimeMillis();
    }

    // 使用指定 UI 和外部提供的 GameModel 创建控制器
    public GameController(UI ui, GameModel model) {
        this.ui = ui;
        this.model = model;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * 返回当前游戏模型。
     */
    public GameModel getModel() {
        return model;
    }

    /**
     * 启动游戏：注册游戏循环和玩家输入处理。
     */
    public void startGame() {
        ui.onStep(this::onTick);
        ui.onKey(this::handlePlayerInput);
    }

    /**
     * 游戏循环：每个 tick 调用此方法。
     * 更新画面、模型状态、碰撞检测和生成新对象。
     */
    public void onTick(int tick) {
        renderGame();
        model.updateGame(tick);
        model.checkCollisions();
        model.spawnObjects();

        // 更新 Ship 相关统计数据（得分、健康值）
        Ship ship = model.getShip();
        if (ship != null) {
            ui.setStat("Score", String.valueOf(ship.getScore()));
            ui.setStat("Health", String.valueOf(ship.getHealth()));
        }
        // 更新存活时间统计（以秒计）
        long currentTime = System.currentTimeMillis();
        long survivedSeconds = (currentTime - startTime) / 1000;
        ui.setStat("Time Survived", survivedSeconds + " seconds");
        // 更新等级统计
        ui.setStat("Level", String.valueOf(model.getLevel()));

        // 如果 ship 被移除，则暂停游戏
        if (ship == null) {
            pauseGame();
        }
    }

    /**
     * 渲染游戏：调用 UI 的 render 方法显示所有空间对象，并更新存活时间统计。
     */
    public void renderGame() {
        long secondsSurvived = (System.currentTimeMillis() - startTime) / 1000;
        ui.setStat("Time Survived", secondsSurvived + " seconds");
        ui.render(model.getSpaceObjects());
    }

    /**
     * 处理玩家输入：W/A/S/D 移动，F 发射子弹，P 切换暂停。
     */
    public void handlePlayerInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Invalid input. Use W, A, S, D, F, or P.");
            return;
        }
        String command = input.trim().toUpperCase();
        switch (command) {
            case "W":
                try {
                    model.getShip().move(Direction.UP);
                    ui.log("Ship moved to (" + model.getShip().getX() + ", " + model.getShip().getY() + ")");
                } catch (BoundaryExceededException e) {
                    ui.log("Cannot move: " + e.getMessage());
                }
                break;
            case "A":
                try {
                    model.getShip().move(Direction.LEFT);
                    ui.log("Ship moved to (" + model.getShip().getX() + ", " + model.getShip().getY() + ")");
                } catch (BoundaryExceededException e) {
                    ui.log("Cannot move: " + e.getMessage());
                }
                break;
            case "S":
                try {
                    model.getShip().move(Direction.DOWN);
                    ui.log("Ship moved to (" + model.getShip().getX() + ", " + model.getShip().getY() + ")");
                } catch (BoundaryExceededException e) {
                    ui.log("Cannot move: " + e.getMessage());
                }
                break;
            case "D":
                try {
                    model.getShip().move(Direction.RIGHT);
                    ui.log("Ship moved to (" + model.getShip().getX() + ", " + model.getShip().getY() + ")");
                } catch (BoundaryExceededException e) {
                    ui.log("Cannot move: " + e.getMessage());
                }
                break;
            case "F":
                model.fireBullet();
                break;
            case "P":
                paused = !paused;
                pauseGame();
                break;
            default:
                System.out.println("Invalid input. Use W, A, S, D, F, or P.");
                break;
        }
    }

    /**
     * 暂停游戏，并在 UI 中记录日志 "Game paused."
     */
    public void pauseGame() {
        ui.pause();
        ui.log("Game paused.");
    }
}
