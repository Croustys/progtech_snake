import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Snake {
    private final LinkedList<Point> body;
    private int direction;
    private ImageIcon headIcon;
    private ImageIcon bodyIcon;

    public Snake() {
        body = new LinkedList<>();
        body.add(new Point(GUI.GRID_SIZE / 2, GUI.GRID_SIZE / 2));
        body.add(new Point((GUI.GRID_SIZE / 2) - 1, GUI.GRID_SIZE / 2));
        direction = KeyEvent.VK_D;

        try {
            headIcon = new ImageIcon("media/snake.jpg");

            bodyIcon = new ImageIcon("media/snake_body.jpg");
        } catch (Exception e) {
            Logger.getLogger(Snake.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public Point getHead() {
        return body.getFirst();
    }

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

    public void setDirection(int newDirection) {
        if (Math.abs(newDirection - direction) != 180) {
            direction = newDirection;
        }
    }

    public boolean contains(int x, int y) {
        return body.contains(new Point(x, y));
    }

    public void grow() {
        body.addLast(new Point(-1, -1));
    }

    public boolean collidesWithSelf() {
        for (int i = 1; i < body.size(); i++) {
            if (getHead().equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean isOutOfBounds() {
        Point head = getHead();
        return head.x < 0 || head.x >= GUI.GRID_SIZE || head.y < 0 || head.y >= GUI.GRID_SIZE;
    }

    public void draw(Graphics g) {
        g.drawImage(headIcon.getImage(), getHead().x * GUI.TILE_SIZE, getHead().y * GUI.TILE_SIZE, GUI.TILE_SIZE, GUI.TILE_SIZE, null);

        for (int i = 1; i < body.size(); i++) {
            Point segment = body.get(i);
            g.drawImage(bodyIcon.getImage(), segment.x * GUI.TILE_SIZE, segment.y * GUI.TILE_SIZE, GUI.TILE_SIZE, GUI.TILE_SIZE, null);
        }
    }

    public boolean collidesWithRock(List<Rock> rocks) {
        Point head = getHead();
        for (Point rock : rocks) {
            if (head.equals(rock)) {
                return true;
            }
        }
        return false;
    }

    public void reset() {
        body.clear();
        body.add(new Point(GUI.GRID_SIZE / 2, GUI.GRID_SIZE / 2));
        body.add(new Point((GUI.GRID_SIZE / 2) - 1, GUI.GRID_SIZE / 2));
        direction = KeyEvent.VK_D;
    }
}
