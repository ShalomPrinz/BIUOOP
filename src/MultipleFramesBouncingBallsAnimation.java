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
    private static final int GRAY_END = GRAY_START + GRAY_WIDTH;
    private static final int YELLOW_START = 450;
    private static final int YELLOW_WIDTH = 150;
    private static final int YELLOW_END = YELLOW_START + YELLOW_WIDTH;

    private static final Random RAND = new Random();
    private static final double THRESHOLD = 0.05;

    /**
     * Randomizes a double and prevents the risk of calling random with min bigger than max.
     * Uses a quite big threshold to compare max and min before calling random.
     * @param min min double value
     * @param max max double value
     * @return randomized double between given min and max, or min if max >= min
     */
    private static double randDouble(double min, double max) {
        if (max - THRESHOLD <= min + THRESHOLD) {
            System.out.println("Danger - asked for: max (" + max + "), min (" + min + ").");
            return min;
        }

        return RAND.nextDouble(min + THRESHOLD, max - THRESHOLD);
    }

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
     * Generates ball within given dimensions.
     * @param radius ball radius
     * @param indexZero min values for ball position
     * @param dimensions max values for ball position
     * @return the generated ball
     */
    private static Ball genBallWithDimensions(int radius, Point indexZero, Point dimensions) {
        // Randomize ball center point
        double x = randDouble(indexZero.getX() + radius, dimensions.getX() - radius);
        double y = randDouble(indexZero.getY() + radius, dimensions.getY() - radius);

        // Generate ball
        Ball ball = MultipleBouncingBallsAnimation.genVelocityBall(x, y, radius);
        ball.getVelocity().setDimensions(dimensions);
        ball.getVelocity().setIndexZero(indexZero);
        return ball;
    }

    /**
     * Generates point outside gray and yellow rectangles.
     * Assumes not given a radius bigger than MAX_RADIUS.
     * @param radius ball radius
     * @return valid start point for ball
     */
    private static Point randomizeWithExclusions(int radius) {
        int diameter = 2 * radius;
        double x, y;

        // Ball is too big to be up or left to gray rectangle
        if (GRAY_START <= diameter || RAND.nextBoolean()) {
            // Ball is too big to be below gray rectangle
            if (GRAY_END >= HEIGHT - diameter || RAND.nextBoolean()) {
                // Ball is too big to be right to yellow rectangle
                if (YELLOW_END >= WIDTH - diameter || RAND.nextBoolean()) {
                    // Right to gray and above yellow top
                    x = randDouble(GRAY_END + radius, WIDTH - radius);
                    y = randDouble(radius, YELLOW_START - radius);
                    return new Point(x, y);
                }

                // Right to yellow
                x = randDouble(YELLOW_END + radius, WIDTH - radius);
                y = randDouble(radius, HEIGHT - radius);
                return new Point(x, y);
            }

            // Below gray
            x = randDouble(radius, YELLOW_START - radius);
            y = randDouble(GRAY_END + radius, HEIGHT - radius);
            return new Point(x, y);
        }

        // Left or above gray
        if (RAND.nextBoolean()) {
            // Left to gray
            x = randDouble(radius, GRAY_START - radius);
            y = randDouble(radius, GRAY_END - radius);
        } else {
            // Above gray
            x = randDouble(GRAY_START + radius, GRAY_END - radius);
            y = randDouble(radius, GRAY_START - radius);
        }
        return new Point(x, y);
    }

    /**
     * Generates ball with regions exclusions - excluding gray and yellow rectangle.
     * @param radius ball radius
     * @return the generated ball
     */
    private static Ball genBallWithExclusions(int radius) {
        // Randomize ball center point
        Point start = randomizeWithExclusions(radius);

        // Generate ball
        Ball ball = MultipleBouncingBallsAnimation.genVelocityBall(start.getX(), start.getY(), radius);
        ball.getVelocity().setDimensions(new Point(WIDTH, HEIGHT));

        // Set ball velocity exclusions
        Point[] indexZero = new Point[]{new Point(GRAY_START, GRAY_START), new Point(YELLOW_START, YELLOW_START)};
        Point[] dimensions = new Point[]{new Point(GRAY_END, GRAY_END), new Point(YELLOW_END, YELLOW_END)};
        ball.getVelocity().setExclusions(indexZero, dimensions);
        return ball;
    }

    /**
     *
     * @param args cmd input args
     */
    public static void main(String[] args) {
        // Points for gray rectangle scope
        Point indexZero = new Point(GRAY_START, GRAY_START);
        Point dimensions = new Point(GRAY_END, GRAY_END);

        Ball[] balls = new Ball[args.length];
        for (int i = 0; i < args.length; i++) {
            int radius = MultipleBouncingBallsAnimation.parseRadius(args[i]);

            if (i % 2 == 0) { // Inside gray rectangle
                balls[i] = genBallWithDimensions(radius, indexZero, dimensions);
            } else { // Outside both rectangles
                balls[i] = genBallWithExclusions(radius);
            }
        }

        animate(balls);
    }
}
