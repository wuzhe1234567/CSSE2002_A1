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
        // Move downward faster – 2 units per tick instead of 1.
        y += 2;
    }
    
    @Override
    public ObjectGraphic render() {
        // 可选：使用与普通敌人不同的图像，例如 "src/assets/enemy.png" 改为 "src/assets/descending_enemy.png"
        return new ObjectGraphic("DescendingEnemy", "src/assets/enemy.png");
    }
}

