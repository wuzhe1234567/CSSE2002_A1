package game;

import game.core.*;
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

    public void handlePlayerInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            System.out.println("Invalid input. Use W, A, S, D, F, or P.");
            return;
        }

        String command = input.trim().toUpperCase();
        switch (command) {
            case "W":
                try {
                    model.getShip().move(Direction.UP);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ui.log(e.getMessage());


                }
                break;
            case "A":
                try {
                    model.getShip().move(Direction.LEFT);


                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ui.log(e.getMessage());
                }
                break;
            case "S":
                try {
                    model.getShip().move(Direction.DOWN);


                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    ui.log(e.getMessage());
                }
                break;
            case "D":
                try {
                    model.getShip().move(Direction.RIGHT);


                } catch (Exception e) {
                    System.out.println(e.getMessage());
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
                System.out.println("Invalid input. Use W, A, S, D, F, or P.");
                break;
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
