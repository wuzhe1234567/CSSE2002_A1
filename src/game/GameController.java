package game;

import game.core.*;
import game.exceptions.BoundaryExceededException;
import game.ui.UI;
import game.utility.Direction;

/**
 * Manages game flow and interactions.
 * Holds references to the UI and the Model, so it can pass information and references
 * back and forth as necessary. Manages changes to the game, which are stored in the Model,
 * and displayed by the UI.
 */
public class GameController {
    private long startTime;
    private UI ui;
    private GameModel model;
    private boolean gameStarted = false;
    private boolean paused = false;

    // 构造函数：由外部传入 UI，如果外部没有传入 GameModel，则内部使用 new GameModel(ui::log)
    public GameController(UI ui, GameModel model) {
        this.ui = ui;
        this.model = model;
        this.startTime = System.currentTimeMillis();
    }

    public GameController(UI ui) {
        this(ui, new GameModel(ui::log));
    }

    /**
     * Returns the game model.
     */
    public GameModel getModel() {
        return model;
    }

    /**
     * Starts the game: 注册周期更新（onStep）和键盘输入处理。
     */
    public void startGame() {
        ui.onStep(this::onTick);
        ui.onKey(this::handlePlayerInput);
    }

    /**
     * 每个 tick 调用：更新状态、检测碰撞、生成新对象，并渲染界面。
     */
    public void onTick(int tick) {
        renderGame();
        model.updateGame(tick);
        model.checkCollisions();
        model.spawnObjects();
    }

    /**
     * 渲染游戏：更新统计数据（存活时间）并调用 UI 的 render 方法。
     */
    public void renderGame() {
        long secondsSurvived = (System.currentTimeMillis() - startTime) / 1000;
        ui.setStat("Time Survived", secondsSurvived + " seconds");
        ui.render(model.getSpaceObjects());
    }

    /**
     * 处理玩家输入：W/A/S/D 控制移动，F 发射子弹，P 暂停游戏。
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
