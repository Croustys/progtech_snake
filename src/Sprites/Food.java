package Sprites;

import java.awt.*;

/**
 * Food Sprite
 */
public class Food extends Sprite {
    /**
     * icon
     */
    private final Image icon;

    /**
     * @param x coordinate
     * @param y coordinate
     * @param tileSize TILE_SIZE
     */
    public Food(int x, int y, int tileSize) {
        super(x, y, tileSize);
        this.icon = loadImage("media/food.jpg");
    }

    /**
     * @param g Graphics, used for drawing the sprite
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(this.icon, this.getPosition().x * TILE_SIZE, this.getPosition().y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
    }
}
