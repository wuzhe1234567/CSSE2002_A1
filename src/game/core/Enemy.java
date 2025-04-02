package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents a standard enemy object.
 */
public class Enemy implements SpaceObject {
    private int xpos;
    private int ypos;

    /**
     * Constructs an Enemy with the specified coordinates.
     *
     * @param x the x-coordinate of the enemy.
     * @param y the y-coordinate of the enemy.
     */
    public Enemy(int x, int y) {
        this.xpos = x;
        this.ypos = y;
    }

    @Override
    public int getX() {
        return xpos;
    }

    @Override
    public int getY() {
        return ypos;
    }

    /**
     * Returns the graphical representation of the enemy.
     * The text representation is set to "👾" as required.
     *
     * @return an ObjectGraphic representing the enemy.
     */
    @Override
    public ObjectGraphic render() {
        // 修改图片路径为正确路径
        return new ObjectGraphic("👾", "src/assets/enemy.png");
    }

    @Override
    public void tick(int tick) {
        ypos++; // Moves downward each tick
    }
}
