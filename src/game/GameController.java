package game;

import game.core.*;
import game.core.SpaceObject;
import game.core.Ship;
import game.exceptions.BoundaryExceededException;
import game.ui.KeyHandler;
import game.ui.Tickable;
import game.GameModel;
import game.ui.UI;
import game.utility.Direction;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The Controller handling the game flow and interactions.
 *
 * Holds references to the UI and the Model, so it can pass information and references back and forth as necessary.
 * Manages changes to the game, which are stored in the Model, and displayed by the UI.
 */
public class GameController {
    private UI ui;
    private GameModel model;
    private long startTime;

    /**
     * Initializes the game controller with the given UI and Model.
     * Stores the ui, model and start time.
     * The start time System.currentTimeMillis() should be stored as a long.
     *
     * @param ui the UI used to draw the Game
     * @param model the model used to maintain game information
     * @provided
     */
    public GameController(UI ui, GameModel model) {
        this.ui = ui;
        this.model = model;
        this.startTime = System.currentTimeMillis(); // Start the timer
    }

    /**
     * Initializes the game controller with the given UI and a new GameModel (taking ui::log as the logger).
     * This constructor should call the other constructor using the "this()" keyword.
     *
     * @param ui the UI used to draw the Game
     * @provided
     */
    public GameController(UI ui) {
        this(ui, new GameModel(ui::log));
    }

    /**
     * Returns the current game model.
     *
     * @return the current game model.
     */
    public GameModel getModel() {
        return model;
    }

    /**
     * Handles player input and performs actions such as moving the ship or firing bullets.
     * Uppercase and lowercase inputs are treated identically.
     *
     * For movement keys "W", "A", "S" and "D", the ship is moved up, left, down, or right respectively,
     * and logs "Core.Ship moved to (x, y)" with the ship's new coordinates.
     * For input "F", the model's fireBullet() method is called.
     * For input "P", the pauseGame() method is called.
     * For all other inputs, logs "Invalid input. Use W, A, S, D, F, or P."
     *
     * @param input the player's input command.
     */
    public void handlePlayerInput(String input) {
        if (input == null || input.isEmpty()) {
            return;
        }
        String command = input.toUpperCase();
        switch (command) {
            case "W":
            case "A":
            case "S":
            case "D":
                try {
                    Direction direction = null;
                    switch (command) {
                        case "W":
                            direction = Direction.UP;
                            break;
                        case "A":
                            direction = Direction.LEFT;
                            break;
                        case "S":
                            direction = Direction.DOWN;
                            break;
                        case "D":
                            direction = Direction.RIGHT;
                            break;
                    }
                    model.getShip().move(direction);
                    ui.log("Core.Ship moved to ("
                            + model.getShip().getX() + ", "
                            + model.getShip().getY() + ")");
                } catch (BoundaryExceededException e) {
                    ui.log(e.getMessage());
                }
                break;
            case "F":
                model.fireBullet();
                break;
            case "P":
                pauseGame();
                break;
            default:
                ui.log("Invalid input. Use W, A, S, D, F, or P.");
                break;
        }
    }

    /**
     * Starts the main game loop.
     *
     * Passes onTick and handlePlayerInput to ui.onStep and ui.onKey respectively.
     * @provided
     */
    public void startGame() {
        
        ui.onStep(new Tickable() {
            @Override
            public void tick(int tick) {
                onTick(tick);
            }
        });
        ui.onKey(new KeyHandler() {
            @Override
            public void onPress(String key) {
                handlePlayerInput(key);
            }
        });
    }
    /**
     * Uses the provided tick to call and advance the following:
     *      - A call to renderGame() to draw the current state of the game.
     *      - A call to model.updateGame(tick) to advance the game by the given tick.
     *      - A call to model.checkCollisions() to handle game interactions.
     *      - A call to model.spawnObjects() to handle object creation.
     *      - A call to model.levelUp() to check and handle leveling.
     *
     * @param tick the provided tick
     * @provided
     */
    public void onTick(int tick) {
        renderGame(); // Update Visual
        model.updateGame(tick); // Update GameObjects
        model.checkCollisions(); // Check for Collisions
        model.spawnObjects(); // Handles new spawns
        model.levelUp(); // Level up when score threshold is met
    }

    /**
     * Calls ui.pause() to pause the game and logs "Game paused."
     * (Note: the player may still move during paused time.)
     */
    public void pauseGame() {
        ui.pause();
        ui.log("Game paused.");
    }

    /**
     * Renders the current game state, including score, health, level and time survived.
     * Updates the UI stats and renders all SpaceObjects (including the Ship).
     */
    public void renderGame() {
        Ship ship = model.getShip();
        ui.setStat("Score", String.valueOf(ship.getScore()));
        ui.setStat("Health", String.valueOf(ship.getHealth()));
        ui.setStat("Level", String.valueOf(model.getLevel()));
        long timeSurvived = (System.currentTimeMillis() - startTime) / 1000;
        ui.setStat("Time Survived", timeSurvived + " seconds");

        List<SpaceObject> objects = new ArrayList<>(model.getSpaceObjects());
        objects.add(ship);
        ui.render(objects);
    }
}
