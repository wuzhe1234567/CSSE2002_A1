package game;

import game.core.*;
import game.ui.ObjectGraphic;
import game.ui.UI;
import game.utility.Direction;
import game.exceptions.BoundaryExceededException;

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
     * Initializes the game controller with the given UI and a new GameModel.
     * Calls the other constructor using the "this()" keyword.
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
        // These objects are added externally. The Ship should already exist (or be created automatically in updateGame).
        model.addObject(new Enemy(3, 1));
        model.addObject(new Asteroid(5, 1));
        // Instantiate a DescendingEnemy using an anonymous class.
        model.addObject(new DescendingEnemy(2, 0) {
            @Override
            public ObjectGraphic render() {
                return new ObjectGraphic("DescendingEnemy", "assets/descending_enemy.png");
            }
        });
        model.addObject(new HealthPowerUp(4, 0));
        model.addObject(new ShieldPowerUp(6, 0));
        renderGame();
        // Initialize stats: Score, Health, Level, Time Survived.
        ui.setStat("Score", "0");
        ui.setStat("Health", "100");
        ui.setStat("Level", "1");
        ui.setStat("Time Survived", "0 seconds");
        ui.onKey(this::preGameInput);
    }

    // Pre-game input handling: wait for Enter key to start the game loop.
    private void preGameInput(String key) {
        if (key.equals("\n") || key.equalsIgnoreCase("ENTER")) {
            gameStarted = true;
            ui.onKey(this::handlePlayerInput);
            ui.onStep(this::onTick);
        }
    }

    /**
     * Uses the provided tick to advance the game state.
     * Calls renderGame() to draw the current state, then model.updateGame(tick) to update game objects,
     * and finally updates UI stats.
     *
     * @param tick the provided tick value.
     */
    public void onTick(int tick) {
        renderGame();
        model.updateGame(tick);
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
     * Renders the current game state by calling ui.render() with all SpaceObjects.
     */
    public void renderGame() {
        ui.render(model.getSpaceObjects());
    }

    /**
     * Handles player input:
     * - For W/A/S/D, moves the ship and logs its new position.
     * - For F, calls the fireBullet() method of the model.
     * - For P, toggles pause and calls pauseGame().
     * - For invalid input, logs an error message.
     *
     * @param key the player's input command.
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
     * @return the game model.
     */
    public GameModel getModel() {
        return model;
    }
}

