package game;

import game.core.*;
import game.exceptions.BoundaryExceededException;
import game.ui.UI;
import game.utility.Direction;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 使用指定 UI 创建控制器，内部构造一个新的 GameModel（日志方法由 UI 提供）。
     *
     * @param ui the UI instance.
     */
    public GameController(UI ui) {
        this(ui, new GameModel(ui::log));
    }

    /**
     * 使用指定 UI 和外部提供的 GameModel 创建控制器。
     *
     * @param ui the UI instance.
     * @param model the GameModel instance.
     */
    public GameController(UI ui, GameModel model) {
        this.ui = ui;
        this.model = model;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * 返回当前游戏模型。
     *
     * @return the current GameModel.
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
     *
     * @param tick the current tick count.
     */
    public void onTick(int tick) {
        renderGame();
        model.updateGame(tick);
        model.checkCollisions();
        model.spawnObjects();
        model.levelUp();

        Ship ship = model.getShip();
        if (ship != null) {
            ui.setStat("Score", String.valueOf(ship.getScore()));
            ui.setStat("Health", String.valueOf(ship.getHealth()));
        }
        long currentTime = System.currentTimeMillis();
        long survivedSeconds = (currentTime - startTime) / 1000;
        ui.setStat("Time Survived", survivedSeconds + " seconds");
        ui.setStat("Level", String.valueOf(model.getLevel()));

        if (ship == null) {
            pauseGame();
        }
    }

    /**
     * 渲染游戏：更新统计数据并调用 UI 的 render 方法显示所有空间对象，
     * 包括将 Ship 添加到渲染列表中。
     */
    public void renderGame() {
        Ship ship = model.getShip();
        // 更新统计数据：Score、Health、Level 和 Time Survived
        if (ship != null) {
            ui.setStat("Score", String.valueOf(ship.getScore()));
            ui.setStat("Health", String.valueOf(ship.getHealth()));
        }
        ui.setStat("Level", String.valueOf(model.getLevel()));
        long secondsSurvived = (System.currentTimeMillis() - startTime) / 1000;
        ui.setStat("Time Survived", secondsSurvived + " seconds");

        List<SpaceObject> objectsToRender = new ArrayList<>(model.getSpaceObjects());
        if (ship != null) {
            objectsToRender.add(ship);
        }
        ui.render(objectsToRender);
    }

    /**
     * 处理玩家输入：W/A/S/D 移动，F 发射子弹，P 切换暂停。
     *
     * @param input the player input command.
     */
    public void handlePlayerInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Invalid input. Use W, A, S, D, F, or P.");
            return;
        }
        String command = input.trim().toUpperCase();
        Ship ship = model.getShip();
        if (ship == null) {
            return;
        }
        try {
            switch (command) {
                case "W":
                    ship.move(Direction.UP);
                    ui.log("Core.Ship moved to (" + ship.getX() + ", " + ship.getY() + ")");
                    break;
                case "A":
                    ship.move(Direction.LEFT);
                    ui.log("Core.Ship moved to (" + ship.getX() + ", " + ship.getY() + ")");
                    break;
                case "S":
                    ship.move(Direction.DOWN);
                    ui.log("Core.Ship moved to (" + ship.getX() + ", " + ship.getY() + ")");
                    break;
                case "D":
                    ship.move(Direction.RIGHT);
                    ui.log("Core.Ship moved to (" + ship.getX() + ", " + ship.getY() + ")");
                    break;
                case "F":
                    model.fireBullet();
                    break;
                case "P":
                    paused = !paused;
                    pauseGame();
                    break;
                default:
                    ui.log("Invalid input. Use W, A, S, D, F, or P.");
                    break;
            }
        } catch (BoundaryExceededException e) {
            ui.log("Cannot move: " + e.getMessage());
        }
    }

    /**
     * 暂停游戏，并在 UI 中记录日志 "Game paused."。
     */
    public void pauseGame() {
        ui.pause();
        ui.log("Game paused.");
    }
}
