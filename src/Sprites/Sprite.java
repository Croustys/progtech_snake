package Sprites;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

/**
 * Abstract class of "Objects" in the project
 */
public abstract class Sprite{
    /**
     * position
     */
    protected final Point position;
    /**
     * global TILE_SIZE
     */
    protected final int TILE_SIZE;

    /**
     * @param x coordinate
     * @param y coordinate
     * @param tileSize global TILE_SIZE
     */
    public Sprite(int x, int y, int tileSize) {
        this.position = new Point(x, y);
        this.TILE_SIZE = tileSize;
    }

    /**
     * @return position of the sprite
     */
    public Point getPosition() {
        return position;
    }

    /**
     * @param g Graphics, used for drawing the sprite
     */
    public abstract void draw(Graphics g);

    /**
     * @param path for the image's location
     * @return the Image of the loaded ImageIcon
     */
    public static Image loadImage(String path) {
        return new ImageIcon(path).getImage();
    }
}