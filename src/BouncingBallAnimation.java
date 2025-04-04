import biuoop.DrawSurface;
import biuoop.GUI;
import biuoop.Sleeper;

import java.awt.Color;

public class BouncingBallAnimation {
    static private void drawAnimation(Point start, double dx, double dy) {
        GUI gui = new GUI("title",400,400);
        Sleeper sleeper = new Sleeper();
        Ball ball = new Ball(start, 30, Color.BLACK);
        ball.setVelocity(dx, dy);
        ball.setDimensions(400, 400);
        while (true) {
            ball.moveOneStep();
            DrawSurface d = gui.getDrawSurface();
            ball.drawOn(d);
            gui.show(d);
            sleeper.sleepFor(50);
        }
    }

    public static void main(String[] args) {
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        double dx = Double.parseDouble(args[2]);
        double dy = Double.parseDouble(args[3]);
        drawAnimation(new Point(x, y), dx, dy);
    }
}
