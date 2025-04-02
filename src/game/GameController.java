package game;

import game.core.*;
import game.ui.ObjectGraphic;
import game.ui.UI;
import game.utility.Direction;
import game.exceptions.BoundaryExceededException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Controller handling the game flow and interactions.
 * Holds references to the UI and the Model, so it can pass information and references back and forth as necessary.
 * Manages changes to the game, which are stored in the Model, and displayed by the UI.
 */
public class GameController {
    private long startTime;
    private UI ui;
    private GameModel model;
    private boolean gameStarted = false;
    private boolean paused = false;

    /**
     * Initializes the game controller with the given UI and GameModel.
     * Stores the ui, model and start time.
     *
     * @param ui the UI used to draw the game.
     * @param model the model used to maintain game information.
     */
    public GameController(UI ui, GameModel model) {
        this.ui = ui;
        this.model = model;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Initializes the game controller with the given UI and a new GameModel (taking ui::log as the logger).
     * This constructor calls the other constructor using the "this()" keyword.
     *
     * @param ui the UI used to draw the game.
     */
    public GameController(UI ui) {
        this(ui, new GameModel(ui::log));
    }

    /**
     * Starts the main game loop.
     * Passes onTick and handlePlayerInput to ui.onStep and ui.onKey respectively.
     */
    public void startGame() {
        // 添加外部对象（例如敌人、小行星、降落型敌人、健康及盾牌道具）。
        model.addObject(new Enemy(3, 1));
        model.addObject(new Asteroid(5, 1));
        // 使用匿名类实例化一个 DescendingEnemy 对象
        model.addObject(new DescendingEnemy(2, 0) {
            @Override
            public ObjectGraphic render() {
                return new ObjectGraphic("DescendingEnemy", "assets/descending_enemy.png");
            }
        });
        model.addObject(new HealthPowerUp(4, 0));
        model.addObject(new ShieldPowerUp(6, 0));
        renderGame();
        // 初始化统计数据：Score, Health, Level, Time Survived
        ui.setStat("Score", "0");
        ui.setStat("Health", "100");
        ui.setStat("Level", "1");
        ui.setStat("Time Survived", "0 seconds");
        ui.onKey(this::preGameInput);
    }

    // Pre-game input handling: wait for Enter key to start game loop.
    private void preGameInput(String key) {
        if (key.equals("\n") || key.equalsIgnoreCase("ENTER")) {
            gameStarted = true;
            ui.onKey(this::handlePlayerInput);
            ui.onStep(this::onTick);
        }
    }

    /**
     * Uses the provided tick to advance the game state.
     * Calls renderGame(), model.updateGame(tick), model.checkCollisions(),
     * model.spawnObjects(), and model.levelUp(), then updates UI stats.
     *
     * @param tick the provided tick value.
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
     * Renders the current game state.
     * Combines all SpaceObjects from the model with the Ship, and passes them to ui.render().
     */
    public void renderGame() {
        List<SpaceObject> allObjects = new ArrayList<>(model.getSpaceObjects());
        if (model.getShip() != null) {
            allObjects.add(model.getShip());
        }
        ui.render(allObjects);
    }

    /**
     * Handles player input and performs actions:
     * - For movement keys "W", "A", "S", "D": moves the ship and logs the new position.
     * - For "F": fires a bullet via model.fireBullet().
     * - For "P": toggles pause and calls pauseGame().
     * - For invalid input: logs "Invalid input. Use W, A, S, D, F, or P."
     *
     * @param input the player's input command.
     */
    public void handlePlayerInput(String input) {
        if (input.equalsIgnoreCase("P")) {
            paused = !paused;
            pauseGame();
            return;
        }
        if (paused) return;
        Ship ship = model.getShip();
        if (ship == null) return;
        try {
            switch (input.toUpperCase()) {
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
                default:
                    ui.log("Invalid input. Use W, A, S, D, F, or P.");
                    break;
            }
        } catch (BoundaryExceededException e) {
            ui.log("Cannot move: " + e.getMessage());
        }
    }

    /**
     * Pauses the game by calling ui.pause() and logs "Game paused.".
     */
    public void pauseGame() {
        ui.pause();
        ui.log("Game paused.");
    }

    /**
     * Returns the current game model.
     *
     * @return the current GameModel.
     */
    public GameModel getModel() {
        return model;
    }
}

