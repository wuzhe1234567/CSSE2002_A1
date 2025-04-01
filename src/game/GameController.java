package game;

import game.core.*;
import game.ui.ObjectGraphic;
import game.exceptions.BoundaryExceededException;
import game.ui.UI;
import game.utility.Direction;


/**
 * The Controller handling the game flow and interactions.
 *
 * Holds references to the UI and the Model, so it can pass information and references back and forth as necessary.
 * Manages changes to the game, which are stored in the Model, and displayed by the UI.
 */
public class GameController {
    /**
     * 使用指定的 UI 和游戏模型创建一个新的 GameController 实例。
     *
     * @param ui    游戏的用户界面组件
     * @param model 游戏的模型，包含游戏数据和业务逻辑
     */


    private UI ui;
    private GameModel model;
    private long startTime;
    /**
     * 使用指定的 UI 和游戏模型创建一个新的 GameController 实例。
     */

    public GameController(UI ui) {

        this.ui = ui;
        this.model = new GameModel(ui::log);
        this.startTime = System.currentTimeMillis();
    }
    /**
     * 使用指定的 UI 和游戏模型创建一个新的 GameController 实例。
     *
     * @param ui    游戏的用户界面组件
     * @param model 游戏的模型，包含游戏数据和业务逻辑
     */

    public GameController(UI ui, GameModel model) {
        this.model = model;
        this.ui = null;
        this.startTime = System.currentTimeMillis();

    }


    /**
     * 使用指定的 UI 和游戏模型创建一个新的 GameController 实例。
     *
     */

    public GameModel getModel() {
        return model;
    }

    private void preGameInput(String key) {
        if (key.equals("\n") || key.equalsIgnoreCase("ENTER")) {
            gameStarted = true;
            ui.onKey(this::handlePlayerInput);
            ui.onStep(this::onTick);
        }
    }
    /**
     * 使用指定的 UI 和游戏模型创建一个新的 GameController 实例。
     *
     */

    public void startGame() {
        ui.onStep(this::onTick);
        ui.onKey(this::handlePlayerInput);
    }

    /**
     * 使用指定的 UI 和游戏模型创建一个新的 GameController 实例。
     *
     */

    public void onTick(int tick) {
        renderGame();
        model.updateGame(tick);
        model.checkCollisions();
        model.spawnObjects();
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
     * 使用指定的 UI 和游戏模型创建一个新的 GameController 实例。
     *
     */

    public void renderGame() {

        long secondsSurvived = (System.currentTimeMillis() - startTime) / 1000;
        ui.setStat("Time Survived", secondsSurvived + " seconds");
        ui.render(model.getSpaceObjects());
    }

    /**
     * 使用指定的 UI 和游戏模型创建一个新的 GameController 实例。
     *
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
                    ui.log("Core.Ship moved to (" + model.getShip().getX() + ", " + model.getShip().getY() + ")");
                    break;
                case "A":
                    ship.move(Direction.LEFT);
                    ui.log("Core.Ship moved to (" + model.getShip().getX() + ", " + model.getShip().getY() + ")");
                    break;
                case "S":
                    ship.move(Direction.DOWN);
                    ui.log("Core.Ship moved to (" + model.getShip().getX() + ", " + model.getShip().getY() + ")");
                    break;
                case "D":
                    ship.move(Direction.RIGHT);
                    ui.log("Core.Ship moved to (" + model.getShip().getX() + ", " + model.getShip().getY() + ")");
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




    }
    /**
     * 使用指定的 UI 和游戏模型创建一个新的 GameController 实例。

     */

    public void pauseGame() {
        ui.pause();
        ui.log("Game paused.");
    }
}
