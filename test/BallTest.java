import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.Image;
import biuoop.DrawSurface;

/**
 * Test class for Ball.
 */
public class BallTest {

    // Simple stub implementation of DrawSurface for testing
    private static class TestDrawSurface implements DrawSurface {
        private Color lastColor;
        private int circleX, circleY, radius;
        private boolean circleFilled = false;

        @Override
        public void setColor(Color c) {
            this.lastColor = c;
        }

        @Override
        public void fillCircle(int x, int y, int radius) {
            this.circleX = x;
            this.circleY = y;
            this.radius = radius;
            this.circleFilled = true;
        }

        // Stub implementations for required DrawSurface methods
        @Override public void fillPolygon(Polygon p) {}
        @Override public void drawPolygon(Polygon p) {}
        @Override public void drawText(int x, int y, String a, int b) {}
        @Override public void drawImage(int x, int y, Image img) {}
        @Override public void drawCircle(int x, int y, int radius) {}
        @Override public void drawRectangle(int x, int y, int width, int height) {}
        @Override public void fillRectangle(int x, int y, int width, int height) {}
        @Override public void fillOval(int x, int y, int width, int height) {}
        @Override public void drawOval(int x, int y, int width, int height) {}
        @Override public void drawLine(int x1, int y1, int x2, int y2) {}
        @Override public int getWidth() { return 800; }
        @Override public int getHeight() { return 600; }
    }

    @Test
    public void testConstructorWithPointRadiusColor() {
        Point center = new Point(5, 10);
        int radius = 7;
        Color color = Color.RED;

        Ball ball = new Ball(center, radius, color);

        assertEquals(5, ball.getX());
        assertEquals(10, ball.getY());
        assertEquals(7, ball.getSize());
        assertEquals(Color.RED, ball.getColor());
    }

    @Test
    public void testConstructorWithCoordinatesRadiusColor() {
        Ball ball = new Ball(5.5, 10.7, 7, Color.BLUE);

        assertEquals(5, ball.getX());  // Should be rounded to int
        assertEquals(10, ball.getY()); // Should be rounded to int
        assertEquals(7, ball.getSize());
        assertEquals(Color.BLUE, ball.getColor());
    }

    @Test
    public void testGetters() {
        Ball ball = new Ball(5.7, 10.2, 7, Color.GREEN);

        assertEquals(5, ball.getX());
        assertEquals(10, ball.getY());
        assertEquals(7, ball.getSize());
        assertEquals(Color.GREEN, ball.getColor());
    }

    @Test
    public void testSetVelocityWithVelocityObject() {
        Ball ball = new Ball(50, 50, 10, Color.RED);
        Velocity v = new Velocity(3, 4);

        ball.setVelocity(v);
        assertEquals(v, ball.getVelocity());
    }

    @Test
    public void testSetVelocityWithCoordinates() {
        Ball ball = new Ball(50, 50, 10, Color.RED);

        ball.setVelocity(3, 4);
        Velocity v = ball.getVelocity();

        // Test by applying to a point and checking the result
        Point p = new Point(5, 5);
        Point newP = v.applyToPoint(p);

        assertEquals(8, newP.getX());
        assertEquals(9, newP.getY());
    }

    @Test
    public void testMoveOneStep() {
        Ball ball = new Ball(50, 50, 10, Color.RED);
        ball.setVelocity(5, 7);

        ball.moveOneStep();

        assertEquals(55, ball.getX());
        assertEquals(57, ball.getY());
    }

    @Test
    public void testMoveOneStepWithBoundaries() {
        Ball ball = new Ball(95, 50, 10, Color.RED);
        Velocity v = new Velocity(10, 0);
        v.setDimensions(100, 100);
        ball.setVelocity(v);

        ball.moveOneStep();

        // Ball should bounce off right wall (moved from x=95 with radius 10)
        assertEquals(85, ball.getX());
        assertEquals(50, ball.getY());
    }

    @Test
    public void testDrawOn() {
        Ball ball = new Ball(50, 50, 10, Color.RED);
        TestDrawSurface testSurface = new TestDrawSurface();

        ball.drawOn(testSurface);

        // Verify the correct methods were called with correct parameters
        assertEquals(Color.RED, testSurface.lastColor);
        assertEquals(50, testSurface.circleX);
        assertEquals(50, testSurface.circleY);
        assertEquals(10, testSurface.radius);
        assertTrue(testSurface.circleFilled);
    }

    @Test
    public void testMultipleStepsMovement() {
        Ball ball = new Ball(50, 50, 5, Color.RED);
        ball.setVelocity(3, 4);

        // Move multiple steps
        for (int i = 0; i < 5; i++) {
            ball.moveOneStep();
        }

        assertEquals(65, ball.getX());
        assertEquals(70, ball.getY());
    }

    @Test
    public void testCornerBounce() {
        Ball ball = new Ball(95, 95, 10, Color.RED);
        Velocity v = new Velocity(10, 10);
        v.setDimensions(100, 100);
        ball.setVelocity(v);

        ball.moveOneStep();

        // Ball should bounce off the corner
        assertEquals(85, ball.getX());
        assertEquals(85, ball.getY());
    }

    @Test
    public void testZeroVelocity() {
        Ball ball = new Ball(50, 50, 10, Color.RED);
        ball.setVelocity(0, 0);

        ball.moveOneStep();

        // Ball should not move
        assertEquals(50, ball.getX());
        assertEquals(50, ball.getY());
    }
}
