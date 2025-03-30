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
     * Initial game state: creates ship, enemies, asteroids, and power-ups,
     * displays them (stationary), and waits for the player to press Enter.
     */
    public void startGame() {
        model.createShip();
        // 添加普通敌人与陨石
        model.addObject(new Enemy(3, 1));
        model.addObject(new Asteroid(5, 1));
        // 添加额外的对象：下降敌人、健康和护盾道具
        model.addObject(new DescendingEnemy(2, 0));
        model.addObject(new HealthPowerUp(4, 0, 20));  // 增加20生命值
        model.addObject(new ShieldPowerUp(6, 0, 50));    // 护盾持续50个tick
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
     * W/A/S/D to move, F to fire, P to toggle pause.
     */
    public void handlePlayerInput(String key) {
        if (key.equalsIgnoreCase("P")) {
            paused = !paused;
            ui.pause();
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
                    break;
                default:
                    break;
            }
        } catch (BoundaryExceededException e) {
            ui.log("Cannot move: " + e.getMessage());
        }
    }
}
