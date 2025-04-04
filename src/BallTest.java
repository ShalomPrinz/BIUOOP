import biuoop.GUI;
import biuoop.DrawSurface;

import java.awt.Color;

/**
 * Tests balls drawing.
 */
public class BallTest {
    /**
     *
     * @param args cmd input args
     */
    public static void main(String[] args) {
        GUI gui = new GUI("Balls Test 1", 400, 400);
        DrawSurface d = gui.getDrawSurface();

        Ball b1 = new Ball(100, 100, 30, Color.RED);
        Ball b2 = new Ball(100, 150, 10, Color.BLUE);
        Ball b3 = new Ball(90, 249, 50, Color.GREEN);

        b1.drawOn(d);
        b2.drawOn(d);
        b3.drawOn(d);

        gui.show(d);
    }
}