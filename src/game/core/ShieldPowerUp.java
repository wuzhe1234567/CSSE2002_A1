package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a shield power-up that grants the ship temporary invincibility.
 */
public class ShieldPowerUp extends PowerUp {
    private int duration = 50; // 固定持续时间
    
    /**
     * Constructs a ShieldPowerUp at the given coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public ShieldPowerUp(int x, int y) {
        super(x, y);
    }
    
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("ShieldPowerUp", "src/assets/shield.png");
    }
    
    @Override
    public void applyEffect(Ship ship) {
        // 这里只打印日志，实际中可调用 ship.activateShield(duration) 等方法
        System.out.println("Shield activated for " + duration + " ticks.");
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    @Override 
    public int getY() {
        return y;
    }
}
