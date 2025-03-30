package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an asteroid object.
 */
public class Asteroid extends ObjectWithPosition {
    public Asteroid(int x, int y) {
        super(x, y);
    }

    @Override
    public int getX() {
        return x;
    }
    @Override
    public int getY() {
        return y;
    }
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Asteroid", "src/assets/asteroid.png");
    }
    @Override
    public void tick(int tick) {
        y++; // Move downward each tick
    }
}

        y++; // Move downward each tick
    }
}
