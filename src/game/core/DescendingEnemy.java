package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an enemy that descends faster.
 */
public class DescendingEnemy extends Enemy {

    public DescendingEnemy(int x, int y) {
        super(x, y);
    }
    
    @Override
    public void tick(int tick) {
        // Descend 2 units per tick instead of 1.
        // 由于 Enemy 已经提供了构造函数，我们重新实现 tick 以加快下降速度。
        // 这里直接操作 y 字段：
        y += 2;
    }
    
    @Override
    public ObjectGraphic render() {
        // 如果有专用图像，可修改路径；否则使用同 Enemy 的图像。
        return new ObjectGraphic("DescendingEnemy", "src/assets/enemy.png");
    }
}
