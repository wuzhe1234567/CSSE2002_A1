package game.core;

import game.ui.ObjectGraphic;

/**
 * Represents an asteroid object.
 */
public class Asteroid implements SpaceObject {
    private int x;
    private int y;
    
    /**
     * Constructs an Asteroid with the specified coordinates.
     *
     * @param x the x-coordinate.
     * @param y the y-coordinate.
     */
    public Asteroid(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    @Override
    public int getX() {
        return x;
    }
    
    @Override 
    public int getY() {
        return y;
    }
    
    /**
     * Returns the graphical representation of the asteroid.
     * Adjust the image path according to your project structure.
     *
     * @return an ObjectGraphic representing the asteroid.
     */
    @Override
    public ObjectGraphic render() {
        // 如果图片存放在项目根目录的 assets 文件夹中，使用 "assets/asteroid.png"
        return new ObjectGraphic("Asteroid", "assets/asteroid.png");
    }
    
    @Override
    public void tick(int tick) {
        y++; // Move downward each tick
    }
}


