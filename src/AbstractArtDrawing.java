import biuoop.GUI;
import biuoop.DrawSurface;

import java.util.Random;
import java.awt.Color;

public class AbstractArtDrawing {
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int LINES = 10;
    private final int CIRCLE_RADIUS = 3;
    private final Random rand = new Random();

    public void drawLines() {
        GUI gui = new GUI("Main Task GUI - Ass1", WIDTH, HEIGHT);
        DrawSurface d = gui.getDrawSurface();

        Line[] lines = new Line[LINES];
        // intersections[i][j] represents intersection point of lines i and j (or null if there is none)
        // NOTE: it's a triangular matrix, which means: col > row => intersection[row][col] is null
        Point[][] intersections = new Point[lines.length][lines.length];
        // Optimization for drawing triangles time complexity: O(n^3) -> O(n^2)
        int[] count = new int[lines.length];

        // Create all lines and draw them with intersections
        for (int i = 0; i < lines.length; ++i) {
            lines[i] = generateRandomLine();

            // Draw line
            d.setColor(Color.BLACK);
            drawLine(lines[i], d);

            // Draw middle line circle
            d.setColor(Color.BLUE);
            d.fillCircle((int) lines[i].middle().getX(), (int) lines[i].middle().getY(), CIRCLE_RADIUS);

            // Draw intersections with previously generated lines
            for (int j = 0; j < i; j++) {
                Line other = lines[j];

                // Calculate the intersection
                Point intersection = other.intersectionWith(lines[i]);
                // Validate intersection is not null before painting
                if (intersection == null) continue;

                // Draw intersection
                d.setColor(Color.RED);
                d.fillCircle((int) intersection.getX(), (int) intersection.getY(), CIRCLE_RADIUS);

                // Save intersection for triangles painting
                // NOTE: j < i
                intersections[j][i] = intersection;
                count[i]++;
                count[j]++;
            }
        }

        // Draw triangle lines
        for (int i = 0; i < lines.length; i++) {
            if (count[i] < 2) continue;
            for (int j = i + 1; j < lines.length; j++) {
                // Validate there is an intersection between i and j
                if (count[j] < 2 || intersections[i][j] == null) continue;
                for (int k = j + 1; k < lines.length; k++) {
                    // Validate k intersects with both i and j
                    if (count[k] < 2 || intersections[j][k] == null || intersections[i][k] == null) continue;

                    // Draw the triangle
                    d.setColor(Color.GREEN);
                    drawTriangle(intersections[j][k], intersections[i][k], intersections[i][j], d);
                }
            }
        }

        System.out.println("Calculations done");
        gui.show(d);
    }

    private void drawTriangle(Point a, Point b, Point c, DrawSurface d) {
        d.setColor(Color.GREEN);
        drawLine(new Line(a, b), d);
        drawLine(new Line(b, c), d);
        drawLine(new Line(c, a), d);
    }

    private void drawLine(Line l, DrawSurface d) {
        d.drawLine((int) l.start().getX(), (int) l.start().getY(), (int) l.end().getX(), (int) l.end().getY());
    }

    private Line generateRandomLine() {
        return new Line(genWidth(), genHeight(), genWidth(), genHeight());
    }

    private int genWidth() {
        return rand.nextInt(WIDTH) + 1;
    }

    private int genHeight() {
        return rand.nextInt(HEIGHT) + 1;
    }

    public static void main(String[] args) {
        AbstractArtDrawing drawing = new AbstractArtDrawing();
        drawing.drawLines();
    }
}
