import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.awt.Color;

/**
 * Draws a single ball and animates its movement.
 */
public class BouncingBallAnimation {
    /**
     * Draws a single ball and moves it according to given velocity until program exits.
     * @param start ball center point
     * @param dx velocity x delta
     * @param dy velocity y delta
     */
    private static void drawAnimation(Point start, double dx, double dy) {
        GUI gui = new GUI("Single bouncing ball", 200, 200);
        Sleeper sleeper = new Sleeper();
        Ball ball = new Ball(start, 30, Color.BLACK);
        ball.setVelocity(dx, dy);
        ball.setDimensions(200, 200);
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
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double dx = Double.parseDouble(args[2]);
        double dy = Double.parseDouble(args[3]);
        drawAnimation(new Point(x, y), dx, dy);
    }
}
