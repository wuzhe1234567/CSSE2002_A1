package game;

import game.core.*;
import game.ui.ObjectGraphic;
import game.ui.UI;
import game.utility.Direction;
import game.exceptions.BoundaryExceededException;

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
        // 注意：如果外部已经创建了 model，则不要重新创建，这里直接使用传入的 model
        this.model = model;
        this.startTime = System.currentTimeMillis();
    }

    public GameController(UI ui) {
        // 如果只传入 UI，则创建一个新的 GameModel
        this(ui, new GameModel(ui::log));
    }

    /**
     * Returns the game model.
     */
    public GameModel getModel() {
        return model;
    }

    /**
     * Initial state: adds enemies, asteroids, and power-ups,
     * displays them (stationary), initializes stats, and waits for the player to press Enter.
     * Note: Ship creation is handled externally.
     */
    public void startGame() {
        model.addObject(new Enemy(3, 1));
        model.addObject(new Asteroid(5, 1));
        model.addObject(new DescendingEnemy(2, 0) {
            @Override
            public ObjectGraphic render() {
                return new ObjectGraphic("DescendingEnemy", "assets/descending_enemy.png");
            }
        });
        model.addObject(new HealthPowerUp(4, 0));
        model.addObject(new ShieldPowerUp(6, 0));
        renderGame();
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

    public void onTick(int tick) {
        renderGame();
        model.updateGame(tick);
        model.checkCollisions();
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

    public void renderGame() {
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
     * Pauses the game and logs "Game paused.".
     */
    public void pauseGame() {
        ui.pause();
        ui.log("Game paused.");
    }
}


