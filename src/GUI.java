import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

public class GUI extends JFrame {
    public static final int TILE_SIZE = 30;
    public static final int GRID_SIZE = 30;

    private final Snake snake;
    private Point food;
    private BufferedImage bufferImage;
    private BufferedImage foodImage;

    public GUI() {
        setTitle("Snake Game");
        setSize(TILE_SIZE * GRID_SIZE, TILE_SIZE * GRID_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        this.snake = new Snake();
        try {
            // Load the food image
            URL imageUrl = getClass().getResource("food.jpg");  // Assuming food.png is in the same directory as your class
            if (imageUrl != null) {
                foodImage = ImageIO.read(imageUrl);
            } else {
                throw new IOException("Image not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        spawnFood();
        bufferImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        Timer timer = new Timer(200, e -> {
            snake.move();
            checkCollision();
            repaint();
        });
        timer.start();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                snake.setDirection(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        setFocusable(true);
        requestFocus();
    }

    private void spawnFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
        } while (snake.contains(x, y));

        food = new Point(x, y);
    }

    private void checkCollision() {
        if (snake.getHead().equals(food)) {
            snake.grow();
            spawnFood();
        } else if (snake.collidesWithSelf() || snake.isOutOfBounds()) {
            JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    @Override
    public void paint(Graphics g) {
        Graphics bufferGraphics = bufferImage.getGraphics();
        bufferGraphics.clearRect(0, 0, getWidth(), getHeight());

        snake.draw(bufferGraphics);

        // Draw the food image
        bufferGraphics.drawImage(foodImage, food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);

        g.drawImage(bufferImage, 0, 0, this);
    }

}
