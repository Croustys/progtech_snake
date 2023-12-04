import db.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GUI extends JFrame {
    public static final int TILE_SIZE = 30;
    public static final int GRID_SIZE = 30;
    private final int ROCK_COUNT = 15;
    private final Snake snake;
    private Food food;
    private final List<Rock> rocks;
    private final BufferedImage bufferImage;
    private ImageIcon rockIcon;
    private ImageIcon foodIcon;
    private ImageIcon desertImage;
    private int score = 0;

    public GUI() {
        setTitle("Snake");
        setSize(TILE_SIZE * GRID_SIZE, TILE_SIZE * GRID_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        this.snake = new Snake();
        this.rocks = new ArrayList<>();
        try {
            foodIcon = new ImageIcon("media/food.jpg");
            rockIcon  = new ImageIcon("media/rock.jpg");
            desertImage = new ImageIcon("media/desert.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        spawnFood();
        spawnRocks();
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
        } while (snake.contains(x, y) || rockExists(x, y));

        food = new Food(x, y);
    }

    private void spawnRocks() {
        Random random = new Random();
        for (int i = 0; i < ROCK_COUNT; i++) {
            int x, y;
            do {
                x = random.nextInt(GRID_SIZE);
                y = random.nextInt(GRID_SIZE);
            } while (snake.contains(x, y) || rockExists(x, y) || food.equals(new Point(x, y)));

            rocks.add(new Rock(x, y));
        }
    }

    private boolean rockExists(int x, int y) {
        for (Point rock : rocks) {
            if (rock.equals(new Point(x, y))) {
                return true;
            }
        }
        return false;
    }

    private void checkCollision() {
        if (snake.getHead().equals(food)) {
            snake.grow();
            spawnFood();
            score++;
        } else if (snake.collidesWithSelf() || snake.isOutOfBounds() || snake.collidesWithRock(rocks)) {
            handleGameOver();
        }
    }

    private void handleGameOver() {
        String playerName = JOptionPane.showInputDialog(this, "Game Over\nYour Score: " + score + "\nEnter your name:");
        if (playerName != null && !playerName.trim().isEmpty()) {
            try {
                Database database = Database.instance();
                database.insertScore(playerName, score);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        int option = JOptionPane.showOptionDialog(
                this,
                "Do you want to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );

        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        snake.reset();
        rocks.clear();
        spawnFood();
        spawnRocks();
        score = 0;
    }

    @Override
    public void paint(Graphics g) {
        Graphics bufferGraphics = bufferImage.getGraphics();
        bufferGraphics.clearRect(0, 0, getWidth(), getHeight());

        // Draw the desert background image
        bufferGraphics.drawImage(desertImage.getImage(), 0, 0, getWidth(), getHeight(), this);
        snake.draw(bufferGraphics);

        // Draw the food image
        bufferGraphics.drawImage(foodIcon.getImage(), food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);

        // Draw the rocks
        for (Point rock : rocks) {
            bufferGraphics.drawImage(rockIcon.getImage(), rock.x * TILE_SIZE, rock.y * TILE_SIZE, TILE_SIZE, TILE_SIZE, this);
        }

        g.drawImage(bufferImage, 0, 0, this);
    }
}
