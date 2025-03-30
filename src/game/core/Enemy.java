package game.core;

import game.ui.ObjectGraphic;

/**
 * 表示敌人对象。
 */
public class Enemy extends SpaceObject {

    public Enemy(int x, int y) {
        super(x, y);
    }

    /**
     * 返回敌人的图形表示，使用 "enemy.png" 作为图片资源。
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Enemy", "C:\\Users\\wuzhe\\Desktop\\Study\\Semester 2\\CSSE 2002\\Assignment1\\provided\\CSSE2002_A1\\assets\\enemy.png");
    }

    /**
     * 敌人每个 tick 向下移动（y 增加）。
     */
    @Override
    public void update() {
        y++; // 向下移动
    }
}
