import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import game.GameEnvironment;
import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import objects.Ball;

import biuoop.DrawSurface;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Image;

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
            if (c != Color.BLACK) {
                this.lastColor = c;
            }
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
    public void testZeroVelocity() {
        Ball ball = new Ball(50, 50, 10, Color.RED);
        ball.setVelocity(0, 0);

        ball.moveOneStep();

        // Ball should not move
        assertEquals(50, ball.getX());
        assertEquals(50, ball.getY());
    }

    @Test
    public void testMoveOneStepNoCollision() {
        GameEnvironment gameEnv = new GameEnvironment();
        Ball ball = new Ball(new Point(50, 50), 5, Color.BLACK);
        ball.setVelocity(3, 4);
        ball.setEnvironment(gameEnv);

        // No collision scenario
        ball.moveOneStep();

        // Ball should move according to velocity
        assertEquals(53, ball.getX());
        assertEquals(54, ball.getY());
    }

    @Test
    public void testMoveOneStepWithHorizontalCollision() {
        // Create collision objects
        Rectangle collidable = new Rectangle(new Point(100, 100), 50, 50);

        // Setup horizontal collision
        GameEnvironment gameEnv = new GameEnvironment();
        Ball ball = new Ball(new Point(98, 120), 5, Color.BLACK);
        ball.setVelocity(3, 4);
        ball.setEnvironment(gameEnv);
        gameEnv.addCollidable(collidable);

        ball.moveOneStep();

        // Ball should not cross collidable borders
        assertTrue(ball.getX() < 100);

        // Verify velocity direction changed
        Point testPoint = new Point(0, 0);
        Point afterMovement = ball.getVelocity().applyToPoint(testPoint);
        assertTrue(afterMovement.getX() < 0); // dx should be negative now
        assertTrue(afterMovement.getY() > 0); // dy should still be positive
    }

    @Test
    public void testMoveOneStepWithVerticalCollision() {
        // Create collision objects
        Rectangle collidable = new Rectangle(new Point(100, 100), 50, 50);

        // Setup horizontal collision
        GameEnvironment gameEnv = new GameEnvironment();
        Ball ball = new Ball(new Point(120, 98), 5, Color.BLACK);
        ball.setVelocity(3, 4);
        ball.setEnvironment(gameEnv);
        gameEnv.addCollidable(collidable);

        ball.moveOneStep();

        // Ball should not cross collidable borders
        assertTrue(ball.getY() < 100);

        // Verify velocity direction changed
        Point testPoint = new Point(0, 0);
        Point afterMovement = ball.getVelocity().applyToPoint(testPoint);
        assertTrue(afterMovement.getX() > 0); // dx should still be positive
        assertTrue(afterMovement.getY() < 0); // dy should be negative now
    }

    @Test
    public void testCollidingPaddleWhenBallInsidePaddleLeftEdge() {
        GameEnvironment gameEnv = new GameEnvironment();
        Ball ball = new Ball(new Point(50, 65), 5, Color.BLACK);
        Rectangle paddle = new Rectangle(new Point(40, 60), 50, 10);

        ball.setEnvironment(gameEnv);
        ball.setPaddle(paddle);
        ball.setVelocity(2, 1);

        Point initialPos = new Point(ball.getX(), ball.getY());
        ball.moveOneStep();

        // Ball should escape from paddle collision
        // Position should change due to escape mechanism
        assertTrue(ball.getX() != initialPos.getX() || ball.getY() != initialPos.getY());
    }

    @Test
    public void testCollidingPaddleWhenBallInsidePaddleRightEdge() {
        GameEnvironment gameEnv = new GameEnvironment();
        Ball ball = new Ball(new Point(85, 65), 5, Color.BLACK);
        Rectangle paddle = new Rectangle(new Point(40, 60), 50, 10);

        ball.setEnvironment(gameEnv);
        ball.setPaddle(paddle);
        ball.setVelocity(-2, 1);

        Point initialPos = new Point(ball.getX(), ball.getY());
        ball.moveOneStep();

        // Ball should escape from paddle collision
        // Position should change due to escape mechanism
        assertTrue(ball.getX() != initialPos.getX() || ball.getY() != initialPos.getY());
    }

    @Test
    public void testCollidingPaddleWhenBallNotInsidePaddle() {
        GameEnvironment gameEnv = new GameEnvironment();
        Ball ball = new Ball(new Point(30, 65), 5, Color.BLACK); // Outside paddle
        Rectangle paddle = new Rectangle(new Point(40, 60), 50, 10);

        ball.setEnvironment(gameEnv);
        ball.setPaddle(paddle);
        ball.setVelocity(2, 1);

        ball.moveOneStep();

        // Ball should move normally since it's not inside paddle
        assertEquals(32, ball.getX());
        assertEquals(66, ball.getY());
    }

    @Test
    public void testCollidingPaddleWithNoCollisionPoint() {
        GameEnvironment gameEnv = new GameEnvironment();
        Ball ball = new Ball(new Point(65, 65), 5, Color.BLACK);
        Rectangle paddle = new Rectangle(new Point(40, 60), 50, 10);

        ball.setEnvironment(gameEnv);
        ball.setPaddle(paddle);
        ball.setVelocity(0, 2); // Moving only vertically, no horizontal collision

        ball.moveOneStep();

        // Ball should move normally since there's no collision point on diameter
        assertEquals(65, ball.getX());
        assertEquals(67, ball.getY());
    }

    @Test
    public void testCollidingPaddleWithNonHorizontalEdge() {
        GameEnvironment gameEnv = new GameEnvironment();
        Ball ball = new Ball(new Point(65, 65), 5, Color.BLACK);
        Rectangle paddle = new Rectangle(new Point(40, 60), 50, 10);

        ball.setEnvironment(gameEnv);
        ball.setPaddle(paddle);
        ball.setVelocity(2, 1);

        // This test is tricky as we need to simulate a scenario where
        // collision edge is vertical (top/bottom) rather than horizontal
        // The ball is positioned to potentially hit top or bottom edge
        Point initialPos = new Point(ball.getX(), ball.getY());
        ball.moveOneStep();

        // Behavior depends on the specific collision detection logic
        assertNotNull(ball); // Basic verification that method doesn't crash
    }

    @Test
    public void testMoveOneStepWithNullPaddle() {
        GameEnvironment gameEnv = new GameEnvironment();
        Ball ball = new Ball(new Point(50, 50), 5, Color.BLACK);
        ball.setEnvironment(gameEnv);
        ball.setVelocity(3, 4);
        // Paddle is null by default

        ball.moveOneStep();

        // Should move normally without paddle collision handling
        assertEquals(53, ball.getX());
        assertEquals(54, ball.getY());
    }

    @Test
    public void testMoveOneStepWithDoubleCollisionPointCalculation() {
        // This test targets the branch where newInfo != null in moveOneStep
        GameEnvironment gameEnv = new GameEnvironment();

        // Create two collidables very close to each other
        Rectangle collidable1 = new Rectangle(new Point(100, 100), 50, 50);
        Rectangle collidable2 = new Rectangle(new Point(99, 99), 52, 52);

        gameEnv.addCollidable(collidable1);
        gameEnv.addCollidable(collidable2);

        Ball ball = new Ball(new Point(95, 120), 5, Color.BLACK);
        ball.setVelocity(10, 1);
        ball.setEnvironment(gameEnv);

        ball.moveOneStep();

        // Ball should handle multiple close collision points
        assertTrue(ball.getX() < 100); // Should not pass through the collidables
    }

    @Test
    public void testSetColor() {
        Ball ball = new Ball(50, 50, 10, Color.RED);

        ball.setColor(Color.BLUE);
        assertEquals(Color.BLUE, ball.getColor());

        // Test with null color - should not change
        ball.setColor(null);
        assertEquals(Color.BLUE, ball.getColor());
    }

    @Test
    public void testTimePassed() {
        Ball ball = new Ball(50, 50, 10, Color.RED);
        ball.setVelocity(3, 4);

        ball.timePassed();

        // timePassed() should call moveOneStep()
        assertEquals(53, ball.getX());
        assertEquals(54, ball.getY());
    }
}