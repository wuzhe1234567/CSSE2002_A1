package game;

import game.core.*;
import game.exceptions.BoundaryExceededException;
import game.ui.UI;
import game.utility.Direction;

/**
 * Manages game flow and interactions.
 */
public class GameController {
    private long startTime;
    private UI ui;
    private GameModel model;
    private boolean gameStarted = false;
    private boolean paused = false;

    public GameController(UI ui, GameModel model) {
        this.ui = ui;
        // 注意：此处不再重新构造 GameModel，而使用传入的 model
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
     * Starts the game by registering the game loop and input handling.
     */
    public void startGame() {
        ui.onStep(this::onTick);
        ui.onKey(this::handlePlayerInput);
    }

    /**
     * Game loop: update game state, collisions, spawning, and update stats.
     */
    public void onTick(int tick) {
        renderGame();
        model.updateGame(tick);
        model.checkCollisions();
        model.spawnObjects();

        // 更新统计数据：若 ship 存在则显示 ship 的得分和健康值，否则显示默认值
        Ship ship = model.getShip();
        if (ship != null) {
            ui.setStat("Score", String.valueOf(ship.getScore()));
            ui.setStat("Health", String.valueOf(ship.getHealth()));
        } else {
            ui.setStat("Score", "0");
            ui.setStat("Health", "0");
        }
        // 更新存活时间统计
        long currentTime = System.currentTimeMillis();
        long survivedSeconds = (currentTime - startTime) / 1000;
        ui.setStat("Time Survived", survivedSeconds + " seconds");
        // 更新等级统计
        ui.setStat("Level", String.valueOf(model.getLevel()));
        if (ship == null) {
            pauseGame();
        }
    }

    /**
     * Renders game objects and updates UI.
     */
    public void renderGame() {
        ui.render(model.getSpaceObjects());
    }

    /**
     * Handles player input: W/A/S/D to move, F fires a bullet, P toggles pause.
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
     * Pauses the game and logs "Game paused."
     */
    public void pauseGame() {
        ui.pause();
        ui.log("Game paused.");
    }
}
