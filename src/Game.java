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
    private final Block borders;
    private Ball ball;

    /**
     * Constructor for game.
     */
    public Game(int width, int height) {
        this.width = width;
        this.height = height;

        this.environment = new GameEnvironment();
        this.sprites = new SpriteCollection();
        this.borders = new Block(new Point(0, 0), width, height);
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
        this.environment.addCollidable(this.borders);

        this.ball = new Ball(new Point(20, 20), 10, Color.BLACK);
        ball.setVelocity(5, 8);
        ball.setEnvironment(this.environment);

        Block block1 = new Block(new Point(100, 100), 100, 200);
        Block block2 = new Block(new Point(300, 300), 300, 100);
        Block block3 = new Block(new Point(700, 500), 80, 80);
        Block[] blocks = new Block[]{block1, block2, block3};
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].addToGame(this);
        }
    }

    /**
     * Run the game.
     */
    public void run() {
        // GUI setup
        GUI gui = new GUI("Game", 800, 600);
        Sleeper sleeper = new Sleeper();
        int framesPerSecond = 60;
        int millisecondsPerFrame = 1000 / framesPerSecond;

        // Paddle setup
        Paddle paddle = new Paddle(
                gui.getKeyboardSensor(),
                new Point(this.width - 100, this.height - 40), 100, 40);
        paddle.setXBound(this.width);
        paddle.addToGame(this);

        // Add ball after paddle, to force paddle move before ball
        ball.addToGame(this);
        ball.setPaddle(paddle);

        // Animation loop
        while (true) {
            long startTime = System.currentTimeMillis();

            DrawSurface d = gui.getDrawSurface();
            d.setColor(Color.WHITE);
            this.borders.drawOn(d);

            d.setColor(Color.BLACK);
            this.sprites.drawAllOn(d);
            gui.show(d);
            this.sprites.notifyAllTimePassed();

            long usedTime = System.currentTimeMillis() - startTime;
            long milliSecondLeftToSleep = millisecondsPerFrame - usedTime;
            if (milliSecondLeftToSleep > 0) {
                sleeper.sleepFor(milliSecondLeftToSleep);
            }
        }
    }
}
