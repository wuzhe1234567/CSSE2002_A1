package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a shield power-up that grants the ship temporary invincibility.
 */
public class ShieldPowerUp extends PowerUp {
    private int duration; // Duration in ticks
    
    /**
     * Constructs a ShieldPowerUp.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param duration the duration in ticks.
     */
    public ShieldPowerUp(int x, int y, int duration) {
        super(x, y);
        this.duration = duration;
    }
    
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("ShieldPowerUp", "src/assets/shield.png");
    }
    
    @Override
    public void apply(Ship ship) {
        // 这里仅打印日志，实际可调用 ship.activateShield(duration) 实现无敌效果
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
