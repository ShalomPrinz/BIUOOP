import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Velocity.
 */
public class VelocityTest {

    @Test
    public void testConstructor() {
        Velocity v = new Velocity(3, 4);
        Point p = new Point(5, 5);
        Point newP = v.applyToPoint(p);

        assertEquals(8, newP.getX());
        assertEquals(9, newP.getY());
    }

    @Test
    public void testFromAngleAndSpeed() {
        // Test angle 0 (up direction)
        Velocity v1 = Velocity.fromAngleAndSpeed(0, 5);
        Point p1 = new Point(10, 10);
        Point newP1 = v1.applyToPoint(p1);

        assertEquals(10, Math.round(newP1.getX()));
        assertEquals(5, Math.round(newP1.getY()));

        // Test angle 90 (right direction)
        Velocity v2 = Velocity.fromAngleAndSpeed(90, 5);
        Point p2 = new Point(10, 10);
        Point newP2 = v2.applyToPoint(p2);

        assertEquals(15, Math.round(newP2.getX()));
        assertEquals(10, Math.round(newP2.getY()));

        // Test angle 180 (down direction)
        Velocity v3 = Velocity.fromAngleAndSpeed(180, 5);
        Point p3 = new Point(10, 10);
        Point newP3 = v3.applyToPoint(p3);

        assertEquals(10, Math.round(newP3.getX()));
        assertEquals(15, Math.round(newP3.getY()));

        // Test angle 270 (left direction)
        Velocity v4 = Velocity.fromAngleAndSpeed(270, 5);
        Point p4 = new Point(10, 10);
        Point newP4 = v4.applyToPoint(p4);

        assertEquals(5, Math.round(newP4.getX()));
        assertEquals(10, Math.round(newP4.getY()));
    }

    @Test
    public void testApplyToPoint() {
        Velocity v = new Velocity(3, -2);
        Point p = new Point(5, 8);
        Point newP = v.applyToPoint(p);

        assertEquals(8, newP.getX());
        assertEquals(6, newP.getY());
    }

    @Test
    public void testIsRightAndIsBottom() {
        // Positive dx (right direction)
        Velocity v1 = new Velocity(5, 0);
        assertTrue(v1.isRight());
        assertFalse(v1.isBottom());

        // Negative dx (left direction)
        Velocity v2 = new Velocity(-5, 0);
        assertFalse(v2.isRight());
        assertFalse(v2.isBottom());

        // Positive dy (bottom direction)
        Velocity v3 = new Velocity(0, 5);
        assertFalse(v3.isRight());
        assertTrue(v3.isBottom());

        // Negative dy (top direction)
        Velocity v4 = new Velocity(0, -5);
        assertFalse(v4.isRight());
        assertFalse(v4.isBottom());

        // Combined directions
        Velocity v5 = new Velocity(5, 5);
        assertTrue(v5.isRight());
        assertTrue(v5.isBottom());
    }

    @Test
    public void testCollide() {
        // Create test collidables with different collision edges
        TestCollidable horizontal = new TestCollidable(CollisionEdge.LEFT);
        TestCollidable vertical = new TestCollidable(CollisionEdge.TOP);
        TestCollidable corner = new TestCollidable(CollisionEdge.CORNER);
        TestCollidable noCollision = new TestCollidable(null);

        Point cp = new Point(5, 5);
        Point testPoint = new Point(10, 10);

        // Test horizontal edge collision (should flip dx)
        Velocity v1 = new Velocity(3, 4);
        v1.collide(cp, horizontal);
        Point p1 = v1.applyToPoint(testPoint);
        assertEquals(7, p1.getX()); // 10 - 3 (flipped dx)
        assertEquals(14, p1.getY()); // 10 + 4 (unchanged dy)

        // Test vertical edge collision (should flip dy)
        Velocity v2 = new Velocity(3, 4);
        v2.collide(cp, vertical);
        Point p2 = v2.applyToPoint(testPoint);
        assertEquals(13, p2.getX()); // 10 + 3 (unchanged dx)
        assertEquals(6, p2.getY()); // 10 - 4 (flipped dy)

        // Test corner collision (should flip both dx and dy)
        Velocity v3 = new Velocity(3, 4);
        v3.collide(cp, corner);
        Point p3 = v3.applyToPoint(testPoint);
        assertEquals(7, p3.getX()); // 10 - 3 (flipped dx)
        assertEquals(6, p3.getY()); // 10 - 4 (flipped dy)

        // Test no collision (no changes)
        Velocity v4 = new Velocity(3, 4);
        v4.collide(cp, noCollision);
        Point p4 = v4.applyToPoint(testPoint);
        assertEquals(13, p4.getX()); // 10 + 3 (unchanged dx)
        assertEquals(14, p4.getY()); // 10 + 4 (unchanged dy)

        // Test zero velocity (no changes should happen)
        Velocity v5 = new Velocity(0, 0);
        v5.collide(cp, horizontal);
        Point p5 = v5.applyToPoint(testPoint);
        assertEquals(10, p5.getX()); // 10 + 0 (unchanged dx)
        assertEquals(10, p5.getY()); // 10 + 0 (unchanged dy)
    }

    // Test helper class implementing Collidable with proper implementation
    private static class TestCollidable implements Collidable {
        private CollisionEdge edgeToReturn;

        public TestCollidable(CollisionEdge edgeToReturn) {
            this.edgeToReturn = edgeToReturn;
        }

        @Override
        public Rectangle getCollisionRectangle() {
            return new TestRectangle(edgeToReturn); // A dummy rectangle
        }

        @Override
        public Velocity hit(Point collisionPoint, Velocity currentVelocity) {
            // Not relevant for this test
            return currentVelocity;
        }

        @Override
        public CollisionEdge getCollisionEdge(Point cp) {
            // Return predefined edge for testing
            return edgeToReturn;
        }
    }

    // Simple Rectangle implementation for testing
    private static class TestRectangle extends Rectangle {
        private CollisionEdge edgeToReturn;

        public TestRectangle(CollisionEdge edgeToReturn) {
            super(new Point(0, 0), 10, 10);
            this.edgeToReturn = edgeToReturn;
        }

        @Override
        public CollisionEdge getCollisionEdge(Point p) {
            return edgeToReturn;
        }
    }
}
