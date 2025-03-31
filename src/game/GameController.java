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
     * Initial state: displays pre-game objects and initializes stats.
     * Ship creation is handled externally.
     */
    public void startGame() {
        // 预先由外部添加 Ship 对象
        model.addObject(new Enemy(3, 1));
        model.addObject(new Asteroid(5, 1));
        // 使用匿名类实例化 DescendingEnemy
        model.addObject(new DescendingEnemy(2, 0) {
            @Override
            public ObjectGraphic render() {
                return new ObjectGraphic("DescendingEnemy", "assets/descending_enemy.png");
            }
        });
        model.addObject(new HealthPowerUp(4, 0));
        model.addObject(new ShieldPowerUp(6, 0));
        renderGame();
        // 初始化统计数据
        ui.setStat("Score", "0");
        ui.setStat("Health", "100");
        ui.setStat("Level", "1");
        ui.setStat("Time Survived", "0 seconds");
        ui.onKey(this::preGameInput);
    }
    
    // 等待玩家按下 Enter 开始游戏循环
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
     * Handles player input: W/A/S/D to move, F fires a bullet, P pauses the game.
     */
    public void handlePlayerInput(String key) {
        if (key.equalsIgnoreCase("P")) {
            // 直接暂停游戏，不切换回未暂停状态
            paused = true;
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
        ui.log("Game paused");
        ui.pause();
        // 为确保输出能被捕捉，可再打印到标准输出
        System.out.println("Game paused");
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
