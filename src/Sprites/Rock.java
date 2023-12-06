package Sprites;

import java.awt.*;

public class Rock extends Sprite {
    private final Image icon;
    public Rock(int x, int y, int tileSize) {
        super(x, y, tileSize);
        this.icon = loadImage("media/rock.jpg");
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(this.icon, this.getPosition().x * TILE_SIZE, this.getPosition().y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
    }
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
