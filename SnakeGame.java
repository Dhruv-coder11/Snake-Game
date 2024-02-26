import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JFrame implements ActionListener, KeyListener {
    private static final int GRID_SIZE = 20;
    private static final int CELL_SIZE = 20;

    private ArrayList<Point> snake;
    private Point food;
    private char direction;
    private Timer timer;
    private boolean isGameOver;
    private int score;

    public SnakeGame() {
        setTitle("Snake Game");
        setSize(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        snake = new ArrayList<>();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        direction = 'R'; // Initial direction: Right

        spawnFood();

        timer = new Timer(200, this);
        timer.start();

        isGameOver = false;
        score = 0;

        addKeyListener(this);
        setFocusable(true);
    }

    public void spawnFood() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(GRID_SIZE);
            y = rand.nextInt(GRID_SIZE);
        } while (snake.contains(new Point(x, y)));
        food = new Point(x, y);
    }

    public void move() {
        Point head = snake.get(0);
        Point newHead;

        switch (direction) {
            case 'U':
                newHead = new Point(head.x, (head.y - 1 + GRID_SIZE) % GRID_SIZE);
                break;
            case 'D':
                newHead = new Point(head.x, (head.y + 1) % GRID_SIZE);
                break;
            case 'L':
                newHead = new Point((head.x - 1 + GRID_SIZE) % GRID_SIZE, head.y);
                break;
            case 'R':
                newHead = new Point((head.x + 1) % GRID_SIZE, head.y);
                break;
            default:
                newHead = head;
        }

        if (snake.contains(newHead) || isOutsideBounds(newHead)) {
            isGameOver = true;
            timer.stop();
        } else {
            snake.add(0, newHead);

            if (newHead.equals(food)) {
                spawnFood();
                score++;
            } else {
                snake.remove(snake.size() - 1);
            }
        }

        repaint();
    }

    public boolean isOutsideBounds(Point p) {
        return p.x < 0 || p.y < 0 || p.x >= GRID_SIZE || p.y >= GRID_SIZE;
    }

    public void restartGame() {
        snake.clear();
        snake.add(new Point(GRID_SIZE / 2, GRID_SIZE / 2));
        direction = 'R';
        spawnFood();
        timer.start();
        isGameOver = false;
        score = 0;
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            move();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_UP:
                if (direction != 'D') {
                    direction = 'U';
                }
                break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') {
                    direction = 'D';
                }
                break;
            case KeyEvent.VK_LEFT:
                if (direction != 'R') {
                    direction = 'L';
                }
                break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') {
                    direction = 'R';
                }
                break;
            case KeyEvent.VK_ENTER:
                if (isGameOver) {
                    restartGame();
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw score
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);

        if (isGameOver) {
            g.drawString("Game Over! Press Enter to restart.", getWidth() / 2 - 120, getHeight() / 2);
            return;
        }

        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * CELL_SIZE, food.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);

        // Draw snake
        g.setColor(Color.GREEN);
        for (Point point : snake) {
            g.fillRect(point.x * CELL_SIZE, point.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakeGame snakeGame = new SnakeGame();
            snakeGame.setVisible(true);
        });
    }
}