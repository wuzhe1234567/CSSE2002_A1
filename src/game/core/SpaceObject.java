package game.core;

import game.ui.ObjectGraphic;

/**
 * 抽象基类，表示游戏中的空间对象。
 */
public abstract class SpaceObject {
    protected int x;
    protected int y;

    public SpaceObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * 返回该对象的图形表示，用于渲染。
     */
    public abstract ObjectGraphic render();

    /**
     * 每个游戏 tick 更新对象状态（例如位置）。
     */
    public abstract void update();
}

