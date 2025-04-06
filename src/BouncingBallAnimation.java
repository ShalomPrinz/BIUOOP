import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.awt.Color;

/**
 * Draws a single ball and animates its movement.
 */
public class BouncingBallAnimation {
    private static final int WIDTH = 200;
    private static final int HEIGHT = 200;
    private static final int RADIUS = 30;

    /**
     * Draws a single ball and moves it according to given velocity until program exits.
     * @param start ball center point
     * @param dx velocity x delta
     * @param dy velocity y delta
     */
    private static void drawAnimation(Point start, double dx, double dy) {
        GUI gui = new GUI("Single bouncing ball", WIDTH, HEIGHT);
        Sleeper sleeper = new Sleeper();
        Ball ball = new Ball(start, RADIUS, Color.BLACK);
        ball.setVelocity(dx, dy);
        ball.getVelocity().setDimensions(WIDTH, HEIGHT);
        while (true) {
            ball.moveOneStep();
            DrawSurface d = gui.getDrawSurface();
            ball.drawOn(d);
            gui.show(d);
            sleeper.sleepFor(50);
        }
    }

    /**
     *
     * @param args cmd input args
     */
    public static void main(String[] args) {
        // Read variables from cmd input args
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double dx = Double.parseDouble(args[2]);
        double dy = Double.parseDouble(args[3]);

        // Get x and y into available regions
        if (x < RADIUS) {
            x = RADIUS;
        }
        if (y < RADIUS) {
            y = RADIUS;
        }
        if (x > WIDTH - RADIUS) {
            x = WIDTH - RADIUS;
        }
        if (y > HEIGHT - RADIUS) {
            y = HEIGHT - RADIUS;
        }

        // Draw the bouncing ball
        drawAnimation(new Point(x, y), dx, dy);
    }
}
