package game.core;

import game.ui.ObjectGraphic;
import game.utility.Direction;
import game.GameModel;

/**
 * Represents the player's ship.
 */
public class Ship extends SpaceObject {

    /**
     * Constructs a Ship with the specified coordinates.
     *
     * @param x the x-coordinate of the ship.
     * @param y the y-coordinate of the ship.
     */
    public Ship(int x, int y) {
        super(x, y);
    }

    /**
     * Returns the graphical representation of the ship,
     * using the image resource from the relative path "src/assets/ship.png".
     *
     * @return an ObjectGraphic representing the ship.
     */
    @Override
    public ObjectGraphic render() {
        return new ObjectGraphic("Ship", "src/assets/ship.png");
    }

    /**
     * The ship does not move automatically on each tick.
     *
     * @param tick the current game tick.
     */
    @Override
    public void tick(int tick) {
        // Movement is controlled by key input.
    }

    /**
     * Moves the ship in the specified direction by one unit, ensuring it stays within bounds.
     *
     * @param direction the direction to move the ship.
     */
    public void move(Direction direction) {
        switch (direction) {
            case UP:
                if (y > 0) y--;
                break;
            case DOWN:
                if (y < GameModel.GAME_HEIGHT - 1) y++;
                break;
            case LEFT:
                if (x > 0) x--;
                break;
            case RIGHT:
                if (x < GameModel.GAME_WIDTH - 1) x++;
                break;
        }
    }
}
