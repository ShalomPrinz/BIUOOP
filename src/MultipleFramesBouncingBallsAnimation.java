import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.awt.Color;
import java.util.Random;

/**
 * Draws two rectangles in a constant position with a certain given rules.
 * Animates multiple balls according to given cmd args.
 */
public class MultipleFramesBouncingBallsAnimation {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    private static final int GRAY_START = 50;
    private static final int GRAY_WIDTH = 450;
    private static final int YELLOW_START = 450;
    private static final int YELLOW_WIDTH = 150;

    /**
     *
     * @param balls to animate
     */
    private static void animate(Ball[] balls) {
        GUI gui = new GUI("Multiple frames with bouncing balls", WIDTH, HEIGHT);
        Sleeper sleeper = new Sleeper();
        while (true) {
            // Draw gray rectangle
            DrawSurface d = gui.getDrawSurface();
            d.setColor(Color.GRAY);
            d.fillRectangle(GRAY_START, GRAY_START, GRAY_WIDTH, GRAY_WIDTH);

            // Bounce balls
            for (int i = 0; i < balls.length; i++) {
                balls[i].moveOneStep();
                balls[i].drawOn(d);
            }

            // Draw yellow rectangle and show all drawings
            d.setColor(Color.YELLOW);
            d.fillRectangle(YELLOW_START, YELLOW_START, YELLOW_WIDTH, YELLOW_WIDTH);
            gui.show(d);
            sleeper.sleepFor(50);
        }
    }

    /**
     * Generate ball within given dimensions.
     * @param sizeArg cmd arg that represents a ball size
     * @param indexZero min values for ball position
     * @param dimensions max values for ball position
     * @return the generated ball
     */
    private static Ball genBall(String sizeArg, Point indexZero, Point dimensions) {
        Random rand = new Random();
        int ballSize = Integer.parseInt(sizeArg);

        // Randomize ball center point
        double x = rand.nextDouble(indexZero.getX(), dimensions.getX());
        double y = rand.nextDouble(indexZero.getY(), dimensions.getY());

        // Generate ball
        Ball ball = MultipleBouncingBallsAnimation.genVelocityBall(x, y, ballSize);
        ball.setDimensions(dimensions);
        ball.setIndexZero(indexZero);
        return ball;
    }

    /**
     *
     * @param args cmd input args
     */
    public static void main(String[] args) {
        // Points for gray rectangle scope
        Point indexZero = new Point(GRAY_START, GRAY_START);
        Point dimensions = new Point(GRAY_START + GRAY_WIDTH, GRAY_START + GRAY_WIDTH);

        Ball[] balls = new Ball[args.length];
        for (int i = 0; i < args.length; i++) {
            if (i % 2 == 0) { // Inside gray rectangle
                balls[i] = genBall(args[i], indexZero, dimensions);
            } else { // Outside both rectangles
                balls[i] = genBall(args[i], indexZero, dimensions);
            }
        }

        animate(balls);
    }
}
