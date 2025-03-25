import biuoop.GUI;
import biuoop.DrawSurface;

import java.util.Random;
import java.awt.Color;

public class AbstractArtDrawing {
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    static final int LINES = 8;
    static final int CIRCLE_RADIUS = 3;

    public void drawRandomCircles() {
        GUI gui = new GUI("Main Task GUI - Ass1", WIDTH, HEIGHT);
        DrawSurface d = gui.getDrawSurface();

        Line[] lines = new Line[LINES];
        for (int i = 0; i < lines.length; ++i) {
            lines[i] = generateRandomLine();

            // Draw line
            d.setColor(Color.BLACK);
            drawLine(lines[i], d);

            // Draw middle line circle
            d.setColor(Color.BLUE);
            d.fillCircle((int) lines[i].middle().getX(), (int) lines[i].middle().getY(), CIRCLE_RADIUS);

            // Draw intersections with previously generated lines
            for (Line other : lines) {
                // No intersection between a line to itself
                if (other == null || other.equals(lines[i])) continue;

                // Calculate intersection
                Point intersection = other.intersectionWith(lines[i]);
                if (intersection == null) continue;

                // Draw intersection
                d.setColor(Color.RED);
                d.fillCircle((int) intersection.getX(), (int) intersection.getY(), CIRCLE_RADIUS);
            }
        }

        gui.show(d);
    }

    private void drawLine(Line l, DrawSurface d) {
        d.drawLine((int) l.start().getX(), (int) l.start().getY(), (int) l.end().getX(), (int) l.end().getY());
    }

    private Line generateRandomLine() {
        return new Line(genWidth(), genHeight(), genWidth(), genHeight());
    }

    private int genWidth() {
        Random rand = new Random();
        return rand.nextInt(WIDTH) + 1;
    }

    private int genHeight() {
        Random rand = new Random();
        return rand.nextInt(HEIGHT) + 1;
    }

    public static void main(String[] args) {
        AbstractArtDrawing drawing = new AbstractArtDrawing();
        drawing.drawRandomCircles();
    }
}
