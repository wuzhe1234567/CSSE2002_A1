package game;

import game.core.*;
import game.exceptions.BoundaryExceededException;
import game.ui.ObjectGraphic;
import game.ui.UI;
import game.utility.Direction;

/**
 * Manages game flow and interactions.
 * Holds references to the UI and the GameModel.
 * Updates model state and displays statistics on the UI.
 */
public class GameController {
    private long startTime;
    private UI ui;
    private GameModel model;
    private boolean gameStarted = false;
    private boolean paused = false;

    // 构造函数：如果外部未传入 GameModel，则内部构造一个新的模型（日志方法由 UI 提供）
    public GameController(UI ui) {
        this.ui = ui;
        this.model = new GameModel(ui::log);
        this.startTime = System.currentTimeMillis();
    }

    // 构造函数：使用外部提供的 GameModel
    public GameController(UI ui, GameModel model) {
        this.ui = ui;
        this.model = model;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Returns the current game model.
     */
    public GameModel getModel() {
        return model;
    }

    /**
     * Starts the game loop by registering the tick and key callbacks.
     */
    public void startGame() {
        ui.onStep(this::onTick);
        ui.onKey(this::handlePlayerInput);
    }

    /**
     * Game loop: called on each tick.
     * Updates the game model, collisions, spawns objects, and refreshes the UI.
     */
    public void onTick(int tick) {
        renderGame();
        model.updateGame(tick);
        model.checkCollisions();
        model.spawnObjects();

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
     * Renders the game objects on the UI and updates the survival time stat.
     */
    public void renderGame() {
        long secondsSurvived = (System.currentTimeMillis() - startTime) / 1000;
        ui.setStat("Time Survived", secondsSurvived + " seconds");
        ui.render(model.getSpaceObjects());
    }

    /**
     * Handles player input: W/A/S/D to move, F fires a bullet, P toggles pause.
     */
    public void handlePlayerInput(String key) {
        if (key.equalsIgnoreCase("P")) {
            paused = !paused;
            pauseGame();
            return;
        }
        if (paused) return;
        Ship ship = model.getShip();
        if (ship == null) return;
        try {
            switch (key.toUpperCase()) {
                case "W":
                    ship.move(Direction.UP);
                    ui.log("Ship moved to (" + ship.getX() + ", " + ship.getY() + ")");
                    break;
                case "A":
                    ship.move(Direction.LEFT);
                    ui.log("Ship moved to (" + ship.getX() + ", " + ship.getY() + ")");
                    break;
                case "S":
                    ship.move(Direction.DOWN);
                    ui.log("Ship moved to (" + ship.getX() + ", " + ship.getY() + ")");
                    break;
                case "D":
                    ship.move(Direction.RIGHT);
                    ui.log("Ship moved to (" + ship.getX() + ", " + ship.getY() + ")");
                    break;
                case "F":
                    model.fireBullet();
                    break;
                default:
                    break;
            }
        } catch (BoundaryExceededException e) {
            ui.log("Cannot move: " + e.getMessage());
        }
    }

    /**
     * Pauses the game and logs "Game paused." on the UI.
     */
    public void pauseGame() {
        ui.pause();
        ui.log("Game paused.");
    }
}
