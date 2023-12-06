import Sprites.*;
import db.Database;
import db.Highscore;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Game extends JFrame {
    private final int TILE_SIZE = 30;
    private final int GRID_SIZE = 30;
    private final int ROCK_COUNT = 15;
    private final Snake snake = new Snake(GRID_SIZE, TILE_SIZE);
    private Food food;
    private final List<Rock> rocks = new ArrayList<>();
    private ImageIcon desertImage;
    private int score = 0;
    private int elapsedSeconds = 0;
    private final GamePanel gamePanel;
    private final Timer timer;
    private final Timer gameTimer;

    public Game() {
        setTitle("Snake");
        setSize(TILE_SIZE * GRID_SIZE + 50, TILE_SIZE * GRID_SIZE + 50);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        setContentPane(gamePanel);

        try {
            desertImage = new ImageIcon("media/desert.png");
        } catch (Exception e) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, e);
        }

        spawnFood();
        spawnRocks();

        gameTimer = new Timer(1000, e -> elapsedSeconds++);
        timer = new Timer(200, e -> {
            snake.move();
            checkCollision();
            gamePanel.repaint();
        });
        gameTimer.start();
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

    private void displayHighscores() {
        timer.stop();
        gameTimer.stop();
        try {
            Database database = Database.instance();
            List<Highscore> highscores = database.getTopScores();

            JFrame highScoresFrame = new JFrame("Highscores");

            HighScoreMenu highScoresPanel = new HighScoreMenu(highscores, this);
            JScrollPane scrollPane = new JScrollPane(highScoresPanel);

            highScoresFrame.add(scrollPane);
            highScoresFrame.setSize(700, 700);
            highScoresFrame.setLocationRelativeTo(this);
            highScoresFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            highScoresFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    openRestartDialog();
                }
            });

            highScoresFrame.setVisible(true);

        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error fetching highscores.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void openRestartDialog() {
        int option = JOptionPane.showOptionDialog(
                this,
                "Would you like to play again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                null,
                null
        );

        if (option == JOptionPane.YES_OPTION) restartGame();
        else System.exit(0);
    }

    private void spawnFood() {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(GRID_SIZE);
            y = random.nextInt(GRID_SIZE);
        } while (snake.contains(x, y) || rockExists(x, y));

        food = new Food(x, y, TILE_SIZE);
    }

    private void spawnRocks() {
        Random random = new Random();
        for (int i = 0; i < ROCK_COUNT; i++) {
            int x, y;
            do {
                x = random.nextInt(GRID_SIZE);
                y = random.nextInt(GRID_SIZE);
            } while (snake.contains(x, y) || rockExists(x, y) || food.getPosition().equals(new Point(x, y)));

            rocks.add(new Rock(x, y, TILE_SIZE));
        }
    }

    private boolean rockExists(int x, int y) {
        for (Rock rock : rocks) {
            if (rock.getPosition().equals(new Point(x, y))) {
                return true;
            }
        }
        return false;
    }

    private void checkCollision() {
        if (snake.getHead().equals(food.getPosition())) {
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
                database.insertOrUpdateScore(playerName, score);
            } catch (SQLException e) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, e);
            }
        }
        this.displayHighscores();
    }

    private void restartGame() {
        elapsedSeconds = 0;
        snake.reset();
        rocks.clear();
        spawnFood();
        spawnRocks();
        score = 0;
        timer.start();
        gameTimer.start();
    }

    private class GamePanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw the desert background image
            g.drawImage(desertImage.getImage(), 0, 0, getWidth(), getHeight(), this);

            snake.draw(g);
            food.draw(g);

            for (Rock rock : rocks) {
                rock.draw(g);
            }

            // Draw the elapsed time on the screen
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Time: " + elapsedSeconds + "s", 10, 20);
            g.drawString("Points: " + score, 10, 40);
        }
    }
}
