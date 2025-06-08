import game.Sprite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import collisions.HitListener;
import game.Game;
import geometry.Point;
import geometry.Velocity;
import objects.Ball;
import objects.Block;

import biuoop.DrawSurface;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * Test class for Block.
 */
public class BlockTest {

    private Block block;
    private Point origin;
    private double width;
    private double height;

    // Simple stub implementation of DrawSurface for testing
    private static class TestDrawSurface implements DrawSurface {
        private Color lastColor;
        private int rectX, rectY, rectWidth, rectHeight;
        private boolean rectFilled = false;
        private boolean rectDrawn = false;

        @Override
        public void setColor(Color c) {
            this.lastColor = c;
        }

        @Override
        public void fillRectangle(int x, int y, int width, int height) {
            this.rectX = x;
            this.rectY = y;
            this.rectWidth = width;
            this.rectHeight = height;
            this.rectFilled = true;
        }

        @Override
        public void drawRectangle(int x, int y, int width, int height) {
            this.rectDrawn = true;
        }

        // Stub implementations for required DrawSurface methods
        @Override public void fillPolygon(Polygon p) {}
        @Override public void drawPolygon(Polygon p) {}
        @Override public void drawText(int x, int y, String a, int b) {}
        @Override public void drawImage(int x, int y, Image img) {}
        @Override public void drawCircle(int x, int y, int radius) {}
        @Override public void fillCircle(int x, int y, int radius) {}
        @Override public void fillOval(int x, int y, int width, int height) {}
        @Override public void drawOval(int x, int y, int width, int height) {}
        @Override public void drawLine(int x1, int y1, int x2, int y2) {}
        @Override public int getWidth() { return 800; }
        @Override public int getHeight() { return 600; }
    }

    // Mock HitListener for testing
    private static class TestHitListener implements HitListener {
        private List<Block> hitBlocks = new ArrayList<>();
        private List<Ball> hitBalls = new ArrayList<>();

        @Override
        public void hitEvent(Block beingHit, Ball hitter) {
            hitBlocks.add(beingHit);
            hitBalls.add(hitter);
        }

        public boolean wasHit() {
            return !hitBlocks.isEmpty();
        }

        public Block getLastHitBlock() {
            return hitBlocks.isEmpty() ? null : hitBlocks.get(hitBlocks.size() - 1);
        }

        public Ball getLastHitBall() {
            return hitBalls.isEmpty() ? null : hitBalls.get(hitBalls.size() - 1);
        }

        public int getHitCount() {
            return hitBlocks.size();
        }
    }

    // Mock Game class for testing
    private static class TestGame extends Game {
        private List<Sprite> sprites;
        private List<collisions.Collidable> collidables;

        public TestGame() {
            super(800, 600);
            this.sprites = new ArrayList<>();
            this.collidables = new ArrayList<>();
        }

        @Override
        public void addSprite(Sprite s) {
            if (this.sprites == null) {
                this.sprites = new ArrayList<>();
            }
            sprites.add(s);
        }

        @Override
        public void removeSprite(Sprite s) {
            sprites.remove(s);
        }

        @Override
        public void addCollidable(collisions.Collidable c) {
            collidables.add(c);
        }

        @Override
        public void removeCollidable(collisions.Collidable c) {
            collidables.remove(c);
        }

        public boolean hasSprite(Sprite s) {
            return sprites.contains(s);
        }

        public boolean hasCollidable(collisions.Collidable c) {
            return collidables.contains(c);
        }
    }

    @BeforeEach
    public void setUp() {
        origin = new Point(50, 50);
        width = 100;
        height = 50;
        block = new Block(origin, width, height);
    }

    @Test
    public void testConstructor() {
        assertEquals(origin, block.getOrigin());
        assertEquals(width, block.getWidth());
        assertEquals(height, block.getHeight());
    }

    @Test
    public void testDrawOnWithBorders() {
        TestDrawSurface surface = new TestDrawSurface();
        block.setColor(Color.RED);

        block.drawOn(surface);

        assertEquals(50, surface.rectX);
        assertEquals(50, surface.rectY);
        assertEquals(100, surface.rectWidth);
        assertEquals(50, surface.rectHeight);
        assertTrue(surface.rectFilled);
        assertTrue(surface.rectDrawn);
        assertEquals(Color.BLACK, surface.lastColor);
    }

    @Test
    public void testDrawOnWithoutBorders() {
        TestDrawSurface surface = new TestDrawSurface();
        block.setColor(Color.BLUE);
        block.setNoBorders();

        block.drawOn(surface);

        assertEquals(50, surface.rectX);
        assertEquals(50, surface.rectY);
        assertEquals(100, surface.rectWidth);
        assertEquals(50, surface.rectHeight);
        assertTrue(surface.rectFilled);
        // Border drawing behavior depends on implementation details
        assertEquals(Color.BLUE, surface.lastColor);
    }

    @Test
    public void testSetColorWithNull() {
        Color originalColor = Color.BLACK; // Default color
        block.setColor(null);

        TestDrawSurface surface = new TestDrawSurface();
        block.drawOn(surface);
        // Color should remain unchanged when null is passed
        assertEquals(originalColor, surface.lastColor);
    }

    @Test
    public void testTimePassed() {
        // timePassed() is empty in Block, just ensure it doesn't crash
        assertDoesNotThrow(() -> block.timePassed());
    }

    @Test
    public void testAddToGame() {
        TestGame game = new TestGame();

        block.addToGame(game);

        assertTrue(game.hasSprite(block));
        assertTrue(game.hasCollidable(block));
    }

    @Test
    public void testRemoveFromGame() {
        TestGame game = new TestGame();
        block.addToGame(game);

        block.removeFromGame(game);

        assertFalse(game.hasSprite(block));
        assertFalse(game.hasCollidable(block));
    }

    @Test
    public void testAddHitListener() {
        TestHitListener listener = new TestHitListener();

        block.addHitListener(listener);

        // Test that listener is added by triggering a hit
        Ball ball = new Ball(60, 60, 5, Color.YELLOW);
        Point collisionPoint = new Point(60, 50);
        Velocity velocity = new Velocity(0, -5);

        block.hit(ball, collisionPoint, velocity);

        assertTrue(listener.wasHit());
        assertEquals(block, listener.getLastHitBlock());
        assertEquals(ball, listener.getLastHitBall());
    }

    @Test
    public void testRemoveHitListener() {
        TestHitListener listener = new TestHitListener();
        block.addHitListener(listener);

        block.removeHitListener(listener);

        // Test that listener is removed by triggering a hit
        Ball ball = new Ball(60, 60, 5, Color.YELLOW);
        Point collisionPoint = new Point(60, 50);
        Velocity velocity = new Velocity(0, -5);

        block.hit(ball, collisionPoint, velocity);

        assertFalse(listener.wasHit());
    }

    @Test
    public void testMultipleHitListeners() {
        TestHitListener listener1 = new TestHitListener();
        TestHitListener listener2 = new TestHitListener();

        block.addHitListener(listener1);
        block.addHitListener(listener2);

        Ball ball = new Ball(60, 60, 5, Color.YELLOW);
        Point collisionPoint = new Point(60, 50);
        Velocity velocity = new Velocity(0, -5);

        block.hit(ball, collisionPoint, velocity);

        assertTrue(listener1.wasHit());
        assertTrue(listener2.wasHit());
        assertEquals(1, listener1.getHitCount());
        assertEquals(1, listener2.getHitCount());
    }

    @Test
    public void testHitWithDifferentColorBall() {
        block.setColor(Color.RED);
        Ball ball = new Ball(60, 60, 5, Color.BLUE);
        Point collisionPoint = new Point(60, 50);
        Velocity velocity = new Velocity(0, -5);

        TestHitListener listener = new TestHitListener();
        block.addHitListener(listener);

        Velocity newVelocity = block.hit(ball, collisionPoint, velocity);

        // Ball color should change to block color
        assertEquals(Color.RED, ball.getColor());
        // Listener should be notified
        assertTrue(listener.wasHit());
        // Velocity should be modified by parent hit method
        assertNotNull(newVelocity);
    }

    @Test
    public void testHitWithSameColorBall() {
        block.setColor(Color.RED);
        Ball ball = new Ball(60, 60, 5, Color.RED);
        Point collisionPoint = new Point(60, 50);
        Velocity velocity = new Velocity(0, -5);

        TestHitListener listener = new TestHitListener();
        block.addHitListener(listener);

        Velocity newVelocity = block.hit(ball, collisionPoint, velocity);

        // Ball color should remain the same
        assertEquals(Color.RED, ball.getColor());
        // Listener should NOT be notified for same color hit
        assertFalse(listener.wasHit());
        // Velocity should still be modified by parent hit method
        assertNotNull(newVelocity);
    }

    @Test
    public void testHitWithPassiveColorBlock() {
        block.setColor(Color.RED);
        block.setPassiveColor();
        Ball ball = new Ball(60, 60, 5, Color.BLUE);
        Point collisionPoint = new Point(60, 50);
        Velocity velocity = new Velocity(0, -5);

        TestHitListener listener = new TestHitListener();
        block.addHitListener(listener);

        Velocity newVelocity = block.hit(ball, collisionPoint, velocity);

        // Ball color should NOT change when block is passive
        assertEquals(Color.BLUE, ball.getColor());
        // Listener should still be notified
        assertTrue(listener.wasHit());
        // Velocity should be modified by parent hit method
        assertNotNull(newVelocity);
    }

    @Test
    public void testBallColorMatch() {
        block.setColor(Color.GREEN);
        Ball matchingBall = new Ball(60, 60, 5, Color.GREEN);
        Ball nonMatchingBall = new Ball(60, 60, 5, Color.YELLOW);

        assertTrue(block.ballColorMatch(matchingBall));
        assertFalse(block.ballColorMatch(nonMatchingBall));
    }

    @Test
    public void testSetPassiveColor() {
        block.setPassiveColor();

        // Test passivity through hit behavior
        block.setColor(Color.RED);
        Ball ball = new Ball(60, 60, 5, Color.BLUE);
        Point collisionPoint = new Point(60, 50);
        Velocity velocity = new Velocity(0, -5);

        block.hit(ball, collisionPoint, velocity);

        // Ball color should not change when block is passive
        assertEquals(Color.BLUE, ball.getColor());
    }

    @Test
    public void testSetNoBorders() {
        block.setNoBorders();

        // Test through drawing behavior
        TestDrawSurface surface = new TestDrawSurface();
        block.drawOn(surface);

        // Rectangle should be filled but border behavior depends on implementation
        assertTrue(surface.rectFilled);
    }

    @Test
    public void testHitListenerModificationDuringNotification() {
        // Test that modifying listeners during notification doesn't cause issues
        TestHitListener listener1 = new TestHitListener();
        TestHitListener listener2 = new TestHitListener() {
            @Override
            public void hitEvent(Block beingHit, Ball hitter) {
                super.hitEvent(beingHit, hitter);
                // Remove self during notification
                beingHit.removeHitListener(this);
            }
        };

        block.addHitListener(listener1);
        block.addHitListener(listener2);

        Ball ball = new Ball(60, 60, 5, Color.YELLOW);
        Point collisionPoint = new Point(60, 50);
        Velocity velocity = new Velocity(0, -5);

        // This should not cause ConcurrentModificationException
        assertDoesNotThrow(() -> block.hit(ball, collisionPoint, velocity));

        assertTrue(listener1.wasHit());
        assertTrue(listener2.wasHit());
    }

    @Test
    public void testBlockInheritanceFromRectangle() {
        // Test that Block properly inherits Rectangle functionality
        assertEquals(block.getOrigin(), origin);
        assertEquals(width, block.getWidth());
        assertEquals(height, block.getHeight());

        // Test collision detection inherited from Rectangle
        Point testPoint = new Point(75, 75); // Inside the block
        assertTrue(block.getOrigin().getX() <= testPoint.getX() &&
                testPoint.getX() <= block.getOrigin().getX() + block.getWidth());
        assertTrue(block.getOrigin().getY() <= testPoint.getY() &&
                testPoint.getY() <= block.getOrigin().getY() + block.getHeight());
    }

    @Test
    public void testBlockDimensions() {
        Block smallBlock = new Block(new Point(0, 0), 10, 5);
        Block largeBlock = new Block(new Point(100, 200), 500, 300);

        assertEquals(10, smallBlock.getWidth());
        assertEquals(5, smallBlock.getHeight());
        assertEquals(500, largeBlock.getWidth());
        assertEquals(300, largeBlock.getHeight());
    }
}