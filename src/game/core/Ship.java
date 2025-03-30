package game.core;

import game.ui.ObjectGraphic;
import game.utility.Direction;
import game.GameModel;

/**
 * 表示玩家控制的飞船。
 */
public class Ship extends SpaceObject {

    public Ship(int x, int y) {
        super(x, y);
    }

    /**
     * 返回飞船的图形表示，使用 "ship.png" 图片。
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Ship", "C:\\Users\\wuzhe\\Desktop\\Study\\Semester 2\\CSSE 2002\\Assignment1\\provided\\CSSE2002_A1\\assets\\ship.png");
    }

    /**
     * 飞船在每个 tick 不会自动移动，只有接收到玩家输入时才改变位置。
     */
    @Override
    public void update() {
        // 玩家控制，不做自动更新
    }

    /**
     * 根据传入的方向移动飞船，每次移动一步，并确保不超出游戏区域。
     * @param direction 移动方向
     */
    public void move(Direction direction) {
        switch (direction) {
            case UP:
                if (y > 0) y--;
                break;
            case DOWN:
                if (y < GameModel.GAME_HEIGHT - 1) y++;
                break;
            case LEFT:
                if (x > 0) x--;
                break;
            case RIGHT:
                if (x < GameModel.GAME_WIDTH - 1) x++;
                break;
        }
    }
}

