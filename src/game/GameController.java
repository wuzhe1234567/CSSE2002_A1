package game;

import game.core.*;
import game.ui.UI;
import game.utility.Direction;

/**
 * Manages the game flow and interactions, connecting the UI with the GameModel.
 */
public class GameController {
    private long startTime;
    private UI ui;
    private GameModel model;

    // Flag indicating whether the game has officially started
    private boolean gameStarted = false;
    // Flag indicating if the game is currently paused
    private boolean paused = false;

    public GameController(UI ui, GameModel model) {
        this.ui = ui;
        this.model = model;
        this.startTime = System.currentTimeMillis();
    }

    public GameController(UI ui) {
        this(ui, new GameModel(ui::log));
    }

    /**
     * Initial game state:
     * Creates the ship, enemies, and asteroids, displays them (stationary),
     * and waits for the player to press Enter to start the game.
     */
    public void startGame() {
        model.createShip();
        model.addObject(new Enemy(3, 1));
        model.addObject(new Asteroid(5, 1));
        renderGame();
        // Register pre-game key handler to listen for Enter
        ui.onKey(this::handlePreGameInput);
    }

    public void handlePreGameInput(String key) {
        if (key.equals("\n") || key.equalsIgnoreCase("ENTER")) {
            gameStarted = true;
            // Switch to regular key handler
            ui.onKey(this::handlePlayerInput);
            // Start the game loop
            ui.onStep(this::onTick);
        }
    }

    public void onTick(int tick) {
        renderGame();
        model.updateGame(tick);
        model.checkCollisions();
        if (model.getShip() == null) {
            ui.pause();
        }
    }

    public void renderGame() {
        ui.render(model.getSpaceObjects());
    }

    /**
     * Handles player input during the game:
     * W/A/S/D to move the ship, F to fire a bullet, P to toggle pause.
     */
    public void handlePlayerInput(String key) {
        if (key.equalsIgnoreCase("P")) {
            paused = !paused;
            ui.pause();
            return;
        }
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
                // Fire bullet from one unit above the ship
                model.addObject(new Bullet(ship.getX(), ship.getY() - 1));
                break;
            default:
                break;
        }
    }
}
