package game.core;

import game.ui.ObjectGraphic;

/**
 * 表示陨石对象。
 */
public class Asteroid extends SpaceObject {

    public Asteroid(int x, int y) {
        super(x, y);
    }

    /**
     * 返回陨石的图形表示，使用 "asteroid.png" 作为图片资源。
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Asteroid", "C:\\Users\\wuzhe\\Desktop\\Study\\Semester 2\\CSSE 2002\\Assignment1\\provided\\CSSE2002_A1\\assets\\asteroid.png");
    }

    /**
     * 陨石每个 tick 向下移动（y 增加）。
     */
    @Override
    public void update() {
        y++; // 向下移动
    }
}
