package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a shield power-up that grants the ship temporary invincibility.
 */
public class ShieldPowerUp extends PowerUp {
    private int duration; // Duration in ticks
    
    /**
     * Constructs a ShieldPowerUp at the given coordinates with a specified duration.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     * @param duration the duration of the shield effect in ticks.
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
        // 在此示例中，仅打印日志。实际可调用 ship.activateShield(duration) 之类方法。
        System.out.println("Shield power-up applied for " + duration + " ticks.");
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

