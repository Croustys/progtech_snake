package Sprites;

import java.awt.*;

/**
 * Rock Sprite
 */
public class Rock extends Sprite {
    /**
     * icon of the rock
     */
    private final Image icon;

    /**
     * @param x coordinate
     * @param y coordinate
     * @param tileSize TILE_SIZE
     */
    public Rock(int x, int y, int tileSize) {
        super(x, y, tileSize);
        this.icon = loadImage("media/rock.jpg");
    }

    /**
     * @param g Graphics, used for drawing the sprite
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(this.icon, this.getPosition().x * TILE_SIZE, this.getPosition().y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
    }

    /**
     * @param obj which will be compared to the rock
     * @return true if equals
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Rock otherRock = (Rock) obj;
        return position.equals(otherRock.position);
    }
}
