package game.core;

import game.ui.ObjectGraphic;

/**
 * 表示子弹对象。
 */
public class Bullet extends SpaceObject {

    public Bullet(int x, int y) {
        super(x, y);
    }

    /**
     * 返回子弹的图形表示，使用 "bullet.png" 作为图片资源。
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Bullet", "C:\\Users\\wuzhe\\Desktop\\Study\\Semester 2\\CSSE 2002\\Assignment1\\provided\\CSSE2002_A1\\assets\\bullet.png");
    }

    /**
     * 子弹每个 tick 向上移动（y 减小）。
     */
    @Override
    public void update() {
        y--; // 向上移动
    }
}

