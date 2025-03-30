package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a health power-up that increases the ship's health.
 */
public class HealthPowerUp extends PowerUp {
    private int healAmount = 20;
    
    public HealthPowerUp(int x, int y) {
        super(x, y);
    }
    
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("HealthPowerUp", "src/assets/health.png");
    }
    
    @Override
    public void applyEffect(Ship ship) {
        ship.heal(healAmount);
    }
}


