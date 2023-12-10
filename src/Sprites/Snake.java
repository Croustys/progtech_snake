package Sprites;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main character class of the game
 */
public class Snake extends Sprite {
    /**
     * body of the snake
     */
    private final LinkedList<Point> body;
    /**
     * direction of the snakes movement
     */
    private int direction;
    /**
     * head icon
     */
    private ImageIcon headIcon;
    /**
     * body icon
     */
    private ImageIcon bodyIcon;
    /**
     * global grid size
     */
    private final int GRID_SIZE;
    /**
     * global tile size
     */
    private final int TILE_SIZE;

    /**
     * @param gs GRID_SIZE
     * @param ts TILE_SIZE
     */
    public Snake(final int gs, final int ts) {
        super(gs / 2, gs / 2, ts);
        this.GRID_SIZE = gs;
        this.TILE_SIZE = ts;
        
        body = new LinkedList<>();
        body.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        body.add(new Point((GRID_SIZE / 2) - 1, GRID_SIZE / 2));
        direction = KeyEvent.VK_D;

        try {
            headIcon = new ImageIcon("media/snake.jpg");

            bodyIcon = new ImageIcon("media/snake_body.jpg");
        } catch (Exception e) {
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /**
     * @return the head of the snake
     */
    public Point getHead() {
        return body.getFirst();
    }

    /**
     * moves the snake in the correct direction
     */
    public void move() {
        Point newHead = getHead();
        newHead = switch (direction) {
            case KeyEvent.VK_W -> new Point(newHead.x, newHead.y - 1);
            case KeyEvent.VK_S -> new Point(newHead.x, newHead.y + 1);
            case KeyEvent.VK_A -> new Point(newHead.x - 1, newHead.y);
            case KeyEvent.VK_D -> new Point(newHead.x + 1, newHead.y);
            default -> body.getFirst();
        };
        body.addFirst(newHead);
        if (body.size() > 2) {
            body.removeLast();
        }
    }

    /**
     * @param newDirection of the snake's movement
     */
    public void setDirection(int newDirection) {
        if (Math.abs(newDirection - direction) != 180) {
            direction = newDirection;
        }
    }

    /**
     * @param x coordinate
     * @param y coordinate
     * @return true if body touches point
     */
    public boolean contains(int x, int y) {
        return body.contains(new Point(x, y));
    }

    /**
     * grows the snake
     */
    public void grow() {
        body.addLast(new Point(-1, -1));
    }

    /**
     * @return true if head touches the snake's body
     */
    public boolean collidesWithSelf() {
        for (int i = 1; i < body.size(); i++) {
            if (getHead().equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if snake tries to escape the map
     */
    public boolean isOutOfBounds() {
        Point head = getHead();
        return head.x < 0 || head.x >= GRID_SIZE || head.y < 0 || head.y >= GRID_SIZE;
    }

    /**
     * @param g Graphics, used for drawing the sprite
     */
    @Override
    public void draw(Graphics g) {
        g.drawImage(headIcon.getImage(), getHead().x * TILE_SIZE, getHead().y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);

        for (int i = 1; i < body.size(); i++) {
            Point segment = body.get(i);
            g.drawImage(bodyIcon.getImage(), segment.x * TILE_SIZE, segment.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        }
    }

    /**
     * @param rocks list of rocks on map
     * @return true if head touches any of the rocks
     */
    public boolean collidesWithRock(List<Rock> rocks) {
        Point head = getHead();
        for (Rock rock : rocks) {
            if (head.equals(rock.getPosition())) {
                return true;
            }
        }
        return false;
    }

    /**
     * resets the position of the snake and direction
     * sets body length to default
     */
    public void reset() {
        body.clear();
        body.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        body.add(new Point((GRID_SIZE / 2) - 1, GRID_SIZE / 2));
        direction = KeyEvent.VK_D;
    }
}
