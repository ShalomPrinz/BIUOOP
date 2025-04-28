import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import biuoop.KeyboardSensor;

/**
 * Test class for Paddle.
 */
public class PaddleTest {

    @Test
    public void testConstructor() {
        TestKeyboardSensor keyboard = new TestKeyboardSensor();
        Point origin = new Point(50, 500);
        double width = 100;
        double height = 10;

        Paddle paddle = new Paddle(keyboard, origin, width, height);
        assertEquals(origin, paddle.getOrigin());
        assertEquals(width, paddle.getWidth());
        assertEquals(height, paddle.getHeight());
    }

    @Test
    public void testMoveLeft() {
        TestKeyboardSensor keyboard = new TestKeyboardSensor();
        Point origin = new Point(50, 500);
        Paddle paddle = new Paddle(keyboard, origin, 100, 10);

        // Move left
        paddle.moveLeft();
        assertEquals(new Point(45, 500), paddle.getOrigin());

        // Move left several times
        for (int i = 0; i < 5; i++) {
            paddle.moveLeft();
        }
        assertEquals(new Point(20, 500), paddle.getOrigin());
    }

    @Test
    public void testMoveRight() {
        TestKeyboardSensor keyboard = new TestKeyboardSensor();
        Point origin = new Point(50, 500);
        Paddle paddle = new Paddle(keyboard, origin, 100, 10);

        // Move right
        paddle.moveRight();
        assertEquals(new Point(55, 500), paddle.getOrigin());

        // Move right several times
        for (int i = 0; i < 5; i++) {
            paddle.moveRight();
        }
        assertEquals(new Point(80, 500), paddle.getOrigin());
    }

    @Test
    public void testSetXBounds() {
        TestKeyboardSensor keyboard = new TestKeyboardSensor();
        Paddle paddle = new Paddle(keyboard, new Point(50, 500), 100, 10);

        // Set X bounds
        int minBound = 20;
        int maxBound = 200;
        paddle.setXBounds(minBound, maxBound);

        // Move right past the max bound
        for (int i = 0; i < 30; i++) {
            paddle.moveRight();
            assertTrue(paddle.getOrigin().getX() < maxBound);  // Should not cross the max bound
        }
        assertEquals(500, paddle.getOrigin().getY());

        // Move left past the min bound
        paddle.setOrigin(new Point(30, 500));
        for (int i = 0; i < 30; i++) {
            paddle.moveLeft();
            assertTrue(paddle.getOrigin().getX() >= minBound);  // Should not cross the min bound
        }
        assertEquals(500, paddle.getOrigin().getY());
    }

    @Test
    public void testTimePassed() {
        TestKeyboardSensor keyboard = new TestKeyboardSensor();
        Paddle paddle = new Paddle(keyboard, new Point(50, 500), 100, 10);

        // Test left key press
        keyboard.pressKey(KeyboardSensor.LEFT_KEY);
        paddle.timePassed();
        assertEquals(new Point(45, 500), paddle.getOrigin());

        // Test right key press
        keyboard.releaseKey(KeyboardSensor.LEFT_KEY);
        keyboard.pressKey(KeyboardSensor.RIGHT_KEY);
        paddle.timePassed();
        assertEquals(new Point(50, 500), paddle.getOrigin());

        // Test no key press
        keyboard.releaseKey(KeyboardSensor.RIGHT_KEY);
        Point before = paddle.getOrigin();
        paddle.timePassed();
        assertEquals(before, paddle.getOrigin()); // Should not move
    }

    @Test
    public void testHit() {
        TestKeyboardSensor keyboard = new TestKeyboardSensor();
        Paddle paddle = new Paddle(keyboard, new Point(100, 500), 100, 10);

        // Test region 1 (leftmost)
        Point region1 = new Point(105, 499);
        Velocity v1 = paddle.hit(region1, new Velocity(0, 5));
        // Expect angle of 300 degrees with same speed
        Point after1 = v1.applyToPoint(new Point(0, 0));
        assertEquals(Math.cos(Math.toRadians(300 - 90)) * 5, after1.getX(), 0.01);
        assertEquals(Math.sin(Math.toRadians(300 - 90)) * 5, after1.getY(), 0.01);

        // Test region 2
        Point region2 = new Point(125, 499);
        Velocity v2 = paddle.hit(region2, new Velocity(0, 5));
        // Expect angle of 330 degrees with same speed
        Point after2 = v2.applyToPoint(new Point(0, 0));
        assertEquals(Math.cos(Math.toRadians(330 - 90)) * 5, after2.getX(), 0.01);
        assertEquals(Math.sin(Math.toRadians(330 - 90)) * 5, after2.getY(), 0.01);

        // Test region 3 (middle) - should behave like normal block
        // Note: must use real values, because implementation forces a collision edge
        Point region3 = new Point(150, 500);
        Velocity v3 = paddle.hit(region3, new Velocity(0, 5));
        // For normal block, velocity should just flip dy
        assertEquals(150, v3.applyToPoint(region3).getX());
        assertEquals(495, v3.applyToPoint(region3).getY());

        // Test region 4
        Point region4 = new Point(175, 499);
        Velocity v4 = paddle.hit(region4, new Velocity(0, 5));
        // Expect angle of 30 degrees with same speed
        Point after4 = v4.applyToPoint(new Point(0, 0));
        assertEquals(Math.cos(Math.toRadians(30 - 90)) * 5, after4.getX(), 0.01);
        assertEquals(Math.sin(Math.toRadians(30 - 90)) * 5, after4.getY(), 0.01);

        // Test region 5 (rightmost)
        Point region5 = new Point(195, 499);
        Velocity v5 = paddle.hit(region5, new Velocity(0, 5));
        // Expect angle of 60 degrees with same speed
        Point after5 = v5.applyToPoint(new Point(0, 0));
        assertEquals(Math.cos(Math.toRadians(60 - 90)) * 5, after5.getX(), 0.01);
        assertEquals(Math.sin(Math.toRadians(60 - 90)) * 5, after5.getY(), 0.01);

        // Test hit outside of paddle (y coordinate too large)
        Point outside = new Point(150, 510);
        Velocity v6 = paddle.hit(outside, new Velocity(0, 5));
        // For normal block collision, velocity should flip
        assertEquals(0, v6.applyToPoint(new Point(0, 0)).getX());
        assertEquals(-5, v6.applyToPoint(new Point(0, 0)).getY());
    }

    /**
     * A mock KeyboardSensor implementation for testing.
     */
    private static class TestKeyboardSensor implements KeyboardSensor {
        private java.util.Set<String> pressedKeys = new java.util.HashSet<>();

        @Override
        public boolean isPressed(String key) {
            return pressedKeys.contains(key);
        }

        public void pressKey(String key) {
            pressedKeys.add(key);
        }

        public void releaseKey(String key) {
            pressedKeys.remove(key);
        }
    }
}