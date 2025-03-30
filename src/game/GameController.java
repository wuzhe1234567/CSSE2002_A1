package game;

import game.core.*;
import game.ui.UI;
import game.utility.Direction;
import game.exceptions.BoundaryExceededException;

/**
 * Manages the game flow and interactions.
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
     * Initial game state: creates ship, enemies, and asteroids,
     * displays them (stationary) and waits for the player to press Enter.
     */
    public void startGame() {
        model.createShip();
        model.addObject(new Enemy(3, 1));
        model.addObject(new Asteroid(5, 1));
        // 假设 Health 和 Score 机制已删除
        renderGame();
        ui.onKey(this::handlePreGameInput);
    }

    public void handlePreGameInput(String key) {
        if (key.equals("\n") || key.equalsIgnoreCase("ENTER")) {
            gameStarted = true;
            ui.onKey(this::handlePlayerInput);
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
     * Handles player input:
     * W/A/S/D move the ship, F fires a bullet, P toggles pause.
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
        try {
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
                    model.addObject(new Bullet(ship.getX(), ship.getY() - 1));
                    break;
                default:
                    break;
            }
        } catch (BoundaryExceededException e) {
            ui.log("Cannot move: " + e.getMessage());
        }
    }
}
