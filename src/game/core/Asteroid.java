package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an asteroid object.
 */
public class Asteroid implements SpaceObject {
    private int xpos;
    private int ypos;

    /**
     * Constructs an Asteroid with the specified coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public Asteroid(int x, int y) {
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
     * Returns the graphical representation of the asteroid.
     * The text representation is set to "🌑" (a moon emoji) as required.
     *
     * @return an ObjectGraphic representing the asteroid.
     */
    @Override
    public ObjectGraphic render() {
        // 注意：确保图片路径正确，如果图片存放在项目根目录的 assets 文件夹中，则使用 "assets/asteroid.png"
        return new ObjectGraphic("🌑", "assets/asteroid.png");
    }

    @Override
    public void tick(int tick) {
        ypos++; // Moves downward each tick
    }
}

