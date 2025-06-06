package game;

import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;
import collisions.Collidable;
import geometry.Point;
import objects.Ball;
import objects.Block;
import objects.Paddle;

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
    private final Block deathBlock;
    private final int deathBlockThreshold = 5;
    private final Ball[] balls;
    private final int borderSize = 30;
    private final Counter remainingBlocks;
    private final BlockRemover blockRemover;
    private final Counter remainingBalls;
    private final BallRemover ballRemover;

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

        // Keeping track of blocks and balls
        this.remainingBlocks = new Counter();
        this.blockRemover = new BlockRemover(this, remainingBlocks);
        this.remainingBalls = new Counter();
        this.ballRemover = new BallRemover(this, remainingBalls);

        // Borders
        this.borders = new Block[]{
                new Block(new Point(0, 0), width, borderSize),
                new Block(new Point(0, borderSize), borderSize, height - borderSize),
                new Block(new Point(borderSize, height - borderSize), width - 2 * borderSize, borderSize),
                new Block(new Point(width - borderSize, borderSize), borderSize, height - borderSize),
        };
        this.deathBlock = new Block(new Point(borderSize, height - borderSize + deathBlockThreshold),
                width - 2 * borderSize, borderSize - deathBlockThreshold);

        // Balls
        this.balls = new Ball[] {
                new Ball(width - 200, height - 200, 5, Color.WHITE),
                new Ball(width - 250, height - 200, 5, Color.WHITE),
                new Ball(width - 300, height - 200, 5, Color.WHITE),
        };
        for (int i = 0; i < this.balls.length; i++) {
            this.balls[i].setVelocity(4, 6);
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
     * Removes given collidable from game.
     * @param c collidable to remove
     */
    public void removeCollidable(Collidable c) {
        this.environment.removeCollidable(c);
    }

    /**
     * Adds a sprite to the game.
     * @param s sprite
     */
    public void addSprite(Sprite s) {
        this.sprites.addSprite(s);
    }

    /**
     * Removes a sprite from the game.
     * @param s sprite
     */
    public void removeSprite(Sprite s) {
        this.sprites.removeSprite(s);
    }

    /**
     * Creates all game objects and adds them to the game.
     */
    public void initialize() {
        // Borders
        for (int i = 0; i < this.borders.length; i++) {
            if (i != 2) {
                this.environment.addCollidable(this.borders[i]);
            }
            this.borders[i].setColor(Color.GRAY);
            this.borders[i].setPassiveColor();
        }

        // Death block (bottom border)
        this.deathBlock.addHitListener(this.ballRemover);
        this.environment.addCollidable(this.deathBlock);
        this.deathBlock.setColor(Color.GRAY);
        this.deathBlock.setPassiveColor();
        this.deathBlock.setNoBorders();

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
                block.addHitListener(this.blockRemover);
            }
            this.remainingBlocks.increase(cols);
        }
    }

    /**
     * Run the game.
     */
    public void run() {
        // GUI setup
        GUI gui = new GUI("Arkanoid", this.width, this.height);
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
        paddle.setPassiveColor();

        // Add ball after paddle, to force paddle move before ball
        for (int i = 0; i < this.balls.length; i++) {
            this.balls[i].setPaddle(paddle);
        }
        this.remainingBalls.increase(this.balls.length);

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
            this.deathBlock.drawOn(d);

            // Sprites (changeable objects)
            this.sprites.drawAllOn(d);
            gui.show(d);
            this.sprites.notifyAllTimePassed();

            // Game finish check
            if (this.remainingBlocks.getValue() == 0 || this.remainingBalls.getValue() == 0) {
                gui.close();
                return;
            }

            // Time management and sleep
            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }
}
