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
     * Initial state: creates ship, enemies, asteroids, and power-ups,
     * displays them (stationary), and waits for the player to press Enter.
     */
    public void startGame() {
        model.createShip();
        model.addObject(new Enemy(3, 1));
        model.addObject(new Asteroid(5, 1));
        // Instantiate DescendingEnemy using an anonymous class
        model.addObject(new DescendingEnemy(2, 0) {
            @Override
            public ObjectGraphic render() {
                return new ObjectGraphic("DescendingEnemy", "src/assets/descending_enemy.png");
            }
        });
        model.addObject(new HealthPowerUp(4, 0));
        model.addObject(new ShieldPowerUp(6, 0));
        renderGame();
        ui.onKey(this::preGameInput);
    }
    
    // Pre-game input handling (private method)
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
        if (model.getShip() == null) {
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
                    ui.log("Bullet fired");
                    System.out.println("Bullet fired");
                    break;
                default:
                    break;
            }
        } catch (BoundaryExceededException e) {
            ui.log("Cannot move: " + e.getMessage());
        }
    }
    
    /**
     * Public method to pause the game.
     * In addition to calling ui.pause(), logs "Game paused".
     */
    public void pauseGame() {
        ui.pause();
        ui.log("Game paused");
    }
    
    /**
     * Public method to retrieve the game model.
     *
     * @return the GameModel object.
     */
    public GameModel getModel() {
        return model;
    }
}
