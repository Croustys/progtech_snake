package Sprites;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

public abstract class Sprite{
    protected final Point position;

    protected final int TILE_SIZE;

    public Sprite(int x, int y, int tileSize) {
        this.position = new Point(x, y);
        this.TILE_SIZE = tileSize;
    }

    public Point getPosition() {
        return position;
    }

    public abstract void draw(Graphics g);
    public static Image loadImage(String path) {
        return new ImageIcon(path).getImage();
    }
}