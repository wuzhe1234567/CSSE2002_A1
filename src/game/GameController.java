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
        this.model = model;
        this.startTime = System.currentTimeMillis();
    }
    
    public GameController(UI ui) {
        this(ui, new GameModel(ui::log));
    }
    
    /**
     * Initial state: creates enemies, asteroids, and power-ups,
     * displays them (stationary), initializes stats, and waits for the player to press Enter.
     */
    public void startGame() {
        // Ship creation is handled externally (no createShip() method in GameModel)
        model.addObject(new Enemy(3, 1));
        model.addObject(new Asteroid(5, 1));
        // Instantiate DescendingEnemy using an anonymous class
        model.addObject(new DescendingEnemy(2, 0) {
            @Override
            public ObjectGraphic render() {
                return new ObjectGraphic("DescendingEnemy", "assets/descending_enemy.png");
            }
        });
        model.addObject(new HealthPowerUp(4, 0));
        model.addObject(new ShieldPowerUp(6, 0));
        renderGame();
        // Initialize stats: Score, Health, Level, and Time Survived.
        ui.setStat("Score", "0");
        ui.setStat("Health", "0");
        ui.setStat("Level", "1");
        ui.setStat("Time Survived", "0 seconds");
        ui.onKey(this::preGameInput);
    }
    
    // Pre-game input handling: waits for Enter key.
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
        // Update "Time Survived" stat in seconds, appending " seconds".
        long currentTime = System.currentTimeMillis();
        long survivedSeconds = (currentTime - startTime) / 1000;
        ui.setStat("Time Survived", survivedSeconds + " seconds");
        // Update "Level" stat.
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
                    ui.log("Ship moved");
                    break;
                case "A":
                    ship.move(Direction.LEFT);
                    ui.log("Ship moved");
                    break;
                case "S":
                    ship.move(Direction.DOWN);
                    ui.log("Ship moved");
                    break;
                case "D":
                    ship.move(Direction.RIGHT);
                    ui.log("Ship moved");
                    break;
                case "F":
                    model.addObject(new Bullet(ship.getX(), ship.getY() - 1));
                    ui.log("Bullet fired");
                    break;
                default:
                    break;
            }
        } catch (BoundaryExceededException e) {
            ui.log("Cannot move: " + e.getMessage());
        }
    }
    
    /**
     * Pauses the game and logs "Game paused".
     */
    public void pauseGame() {
        ui.pause();
        ui.log("Game paused");
    }
    
    /**
     * Returns the game model.
     *
     * @return the GameModel.
     */
    public GameModel getModel() {
        return model;
    }
}
