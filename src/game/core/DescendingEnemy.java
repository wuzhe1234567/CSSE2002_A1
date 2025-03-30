package game.core;

import game.ui.ObjectGraphic;

/**
 * Abstract class representing an enemy that descends faster.
 */
public abstract class DescendingEnemy extends Enemy {

    public DescendingEnemy(int x, int y) {
        super(x, y);
    }
    
    @Override
    public void tick(int tick) {
        // Descend faster: 2 units per tick instead of 1.
        y += 2;
    }
    
    // 将 render 方法声明为 abstract，要求子类提供具体图像
    @Override
    public abstract ObjectGraphic render();
}
