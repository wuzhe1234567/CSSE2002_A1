package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a shield power-up that grants the ship temporary invincibility.
 */
public class ShieldPowerUp extends PowerUp {
    private int duration = 50;
    
    public ShieldPowerUp(int x, int y) {
        super(x, y);
    }
    
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("ShieldPowerUp", "src/assets/shield.png");
    }
    
    @Override
    public void applyEffect(Ship ship) {
        System.out.println("Shield activated for " + duration + " ticks.");
    }
}

