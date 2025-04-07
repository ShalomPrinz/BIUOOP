import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.awt.Color;
import java.util.Random;

/**
 * Draws multiple balls and animates their movement.
 */
public class MultipleBouncingBallsAnimation {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int MAX_RADIUS = 100;
    private static final int MIN_RADIUS = 1;
    private static final int MAX_SPEED_BALL_SIZE = 50;
    private static final double MIN_SPEED = 2;

    /**
     *
     * @param balls to animate
     */
    public static void animate(Ball[] balls) {
        GUI gui = new GUI("Multiple bouncing balls", WIDTH, HEIGHT);
        Sleeper sleeper = new Sleeper();
        while (true) {
            DrawSurface d = gui.getDrawSurface();
            for (int i = 0; i < balls.length; i++) {
                balls[i].moveOneStep();
                balls[i].drawOn(d);
            }
            gui.show(d);
            sleeper.sleepFor(50);
        }
    }

    /**
     * Generates a ball with a calculated velocity based on given values.
     * @param centerX ball center x value
     * @param centerY ball center y value
     * @param radius ball radius
     * @return the generated ball
     */
    public static Ball genVelocityBall(double centerX, double centerY, int radius) {
        // Calculate ball speed and randomize angle
        double speed = MIN_SPEED;
        if (radius < MAX_SPEED_BALL_SIZE) {
            speed = MIN_SPEED * MAX_SPEED_BALL_SIZE / radius;
        }
        double angle = new Random().nextDouble(0, 360);

        // Create ball and insert into animation array
        Ball ball = new Ball(centerX, centerY, radius, Color.BLACK);
        ball.setVelocity(Velocity.fromAngleAndSpeed(angle, speed));
        return ball;
    }

    /**
     * Parses a cmd input arg to int and limits it within a pre-defined range.
     * @param arg cmd input arg
     * @return ball radius
     */
    public static int parseRadius(String arg) {
        int radius = Integer.parseInt(arg);
        if (radius > MAX_RADIUS) {
            radius = MAX_RADIUS;
        }
        if (radius < MIN_RADIUS) {
            radius = MIN_RADIUS;
        }
        return radius;
    }

    /**
     * Generates balls array of given cmd args.
     * @param args cmd input args
     * @return the generated balls array
     */
    private static Ball[] genBalls(String[] args) {
        Random rand = new Random();
        Ball[] balls = new Ball[args.length];

        for (int i = 0; i < args.length; i++) {
            int radius = parseRadius(args[i]);

            // Randomize ball center point
            double x = rand.nextDouble(radius, WIDTH - radius);
            double y = rand.nextDouble(radius, HEIGHT - radius);
            balls[i] = genVelocityBall(x, y, radius);
            balls[i].getVelocity().setDimensions(WIDTH, HEIGHT);
        }

        return balls;
    }

    /**
     *
     * @param args cmd input args
     */
    public static void main(String[] args) {
        // Don't animate 0 balls
        if (args.length < 1) {
            return;
        }

        animate(genBalls(args));
    }
}
