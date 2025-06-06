import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.awt.Color;

/**
 * Represents a game management object.
 */
public class Game {
    private final int width;
    private final int height;
    private final SpriteCollection sprites;
    private final GameEnvironment environment;
    private final Block[] borders;
    private final Ball[] balls;
    private final int borderSize = 30;

    /**
     * Constructor for game.
     * @param width screen width
     * @param height screen height
     */
    public Game(int width, int height) {
        this.width = width;
        this.height = height;
        this.environment = new GameEnvironment();
        this.sprites = new SpriteCollection();

        // Borders
        this.borders = new Block[]{
                new Block(new Point(0, 0), width, borderSize),
                new Block(new Point(0, borderSize), borderSize, height - borderSize),
                new Block(new Point(borderSize, height - borderSize), width - 2 * borderSize, borderSize),
                new Block(new Point(width - borderSize, borderSize), borderSize, height - borderSize),
        };

        // Balls
        this.balls = new Ball[] {
                new Ball(width - 200, height - 200, 8, Color.BLACK),
                new Ball(width - 300, height - 200, 8, Color.BLACK),
        };
        for (int i = 0; i < this.balls.length; i++) {
            this.balls[i].setVelocity(5, 8);
            this.balls[i].setEnvironment(this.environment);
            this.balls[i].addToGame(this);
        }
    }

    /**
     * Adds a collidable to the game.
     * @param c collidable
     */
    public void addCollidable(Collidable c) {
        this.environment.addCollidable(c);
    }

    /**
     * Adds a sprite to the game.
     * @param s sprite
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * Creates all game objects and adds them to the game.
     */
    public void initialize() {
        // Borders
        for (int i = 0; i < this.borders.length; i++) {
            this.environment.addCollidable(this.borders[i]);
            this.borders[i].setColor(Color.GRAY);
        }

        // Blocks
        Color[] colors = new Color[]{Color.GRAY, Color.RED.darker(),
                Color.YELLOW, Color.BLUE.brighter(), Color.PINK, Color.GREEN};
        int blockWidth = 50;
        int blockHeight = 20;
        int initialCols = 12;
        for (int i = 0; i < colors.length; i++) {
            int cols = initialCols - i;
            int y = this.borderSize + 100 + (i * blockHeight);
            for (int j = 0; j < cols; j++) {
                int x = this.width - this.borderSize - ((j + 1) * blockWidth);
                Block block = new Block(new Point(x, y), blockWidth, blockHeight);
                block.addToGame(this);
                block.setColor(colors[i]);
            }
        }
    }

    /**
     * Run the game.
     */
    public void run() {
        // GUI setup
        GUI gui = new GUI("Arkanoid", 800, 600);
        Sleeper sleeper = new Sleeper();
        int framesPerSecond = 60;
        int millisecondsPerFrame = 1000 / framesPerSecond;

        // Paddle setup
        Paddle paddle = new Paddle(
                gui.getKeyboardSensor(),
                new Point(this.width - 100 - borderSize, this.height - 30 - borderSize), 100, 30);
        paddle.setXBounds(borderSize, this.width - borderSize);
        paddle.addToGame(this);
        paddle.setColor(Color.ORANGE);

        // Add ball after paddle, to force paddle move before ball
        for (int i = 0; i < this.balls.length; i++) {
            this.balls[i].setPaddle(paddle);
            this.balls[i].setColor(Color.WHITE);
        }

        // Animation loop
        while (true) {
            long startTime = System.currentTimeMillis();
            DrawSurface d = gui.getDrawSurface();

            // Screen and borders
            d.setColor(Color.BLUE.darker().darker());
            d.fillRectangle(0, 0, this.width, this.height);
            for (int i = 0; i < this.borders.length; i++) {
                this.borders[i].drawOn(d);
            }

            // Sprites (changeable objects)
            this.sprites.drawAllOn(d);
            gui.show(d);
            this.sprites.notifyAllTimePassed();

            // Time management and sleep
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }
}
