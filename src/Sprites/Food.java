package Sprites;

import java.awt.*;

public class Food extends Sprite {
    private final Image icon;
    public Food(int x, int y, int tileSize) {
        super(x, y, tileSize);
        this.icon = loadImage("media/food.jpg");
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.icon, this.getPosition().x * TILE_SIZE, this.getPosition().y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
    }
}
