import biuoop.GUI;
import biuoop.DrawSurface;

import java.util.Random;
import java.awt.Color;

/**
 * Generates a visual representation of abstract art (Point and Line classes) by using a GUI framework (biuoop).
 */
public class AbstractArtDrawing {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int LINES = 10;
    private static final int CIRCLE_RADIUS = 3;
    private static final Random RAND = new Random();

    /**
     * Draws a set of random lines, highlights their midpoints and intersection points, and identifies triangles
     * formed by intersecting lines.
     */
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
                if (intersection == null) {
                    continue;
                }

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
            if (count[i] < 2) {
                continue;
            }
            for (int j = i + 1; j < lines.length; j++) {
                // Validate there is an intersection between i and j
                if (count[j] < 2 || intersections[i][j] == null) {
                    continue;
                }
                for (int k = j + 1; k < lines.length; k++) {
                    // Validate k intersects with both i and j
                    if (count[k] < 2 || intersections[j][k] == null || intersections[i][k] == null) {
                        continue;
                    }

                    // Draw the triangle
                    d.setColor(Color.GREEN);
                    drawTriangle(intersections[j][k], intersections[i][k], intersections[i][j], d);
                }
            }
        }

        System.out.println("Calculations done");
        gui.show(d);
    }

    /**
     * Draws a triangle (formed by given points) on the provided drawing surface.
     * @param a the first point of the triangle
     * @param b the second point of the triangle
     * @param c the third point of the triangle
     * @param d the drawing surface where the triangle will be drawn
     */
    private void drawTriangle(Point a, Point b, Point c, DrawSurface d) {
        d.setColor(Color.GREEN);
        drawLine(new Line(a, b), d);
        drawLine(new Line(b, c), d);
        drawLine(new Line(c, a), d);
    }

    /**
     * Draws given line on the provided drawing surface.
     * @param l the line to be drawn
     * @param d the drawing surface where the line will be drawn
     */
    private void drawLine(Line l, DrawSurface d) {
        d.drawLine((int) l.start().getX(), (int) l.start().getY(), (int) l.end().getX(), (int) l.end().getY());
    }

    /**
     * @return a Line object with randomly generated start and end points.
     */
    private Line generateRandomLine() {
        return new Line(genWidth(), genHeight(), genWidth(), genHeight());
    }

    /**
     * @return a randomly generated value between 1 and WIDTH.
     */
    private int genWidth() {
        return RAND.nextInt(WIDTH) + 1;
    }

    /**
     * @return a randomly generated value between 1 and HEIGHT.
     */
    private int genHeight() {
        return RAND.nextInt(HEIGHT) + 1;
    }

    /**
     * The entry point of the program.
     * @param args command-line arguments passed to the program.
     */
    public static void main(String[] args) {
        AbstractArtDrawing drawing = new AbstractArtDrawing();
        drawing.drawLines();
    }
}
