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
    public void testSetDimensions() {
        Velocity v = new Velocity(5, 5);
        v.setDimensions(100, 100);

        // Test movement in bounds
        Point p = new Point(50, 50);
        v.matchDimensions(p, 10);
        Point newP = v.applyToPoint(p);

        assertEquals(55, newP.getX());
        assertEquals(55, newP.getY());

        // Test with negative dimensions (should ignore)
        v.setDimensions(-100, 100);
        v.matchDimensions(p, 10);
        newP = v.applyToPoint(p);

        assertEquals(55, newP.getX());
        assertEquals(55, newP.getY());
    }

    @Test
    public void testSetIndexZero() {
        Velocity v = new Velocity(-5, -5);
        v.setDimensions(100, 100);
        v.setIndexZero(new Point(10, 10));

        // Test movement with boundaries
        Point p = new Point(15, 15);
        v.matchDimensions(p, 5);
        Point newP = v.applyToPoint(p);

        // Verify that velocity was inverted as we'd hit the indexZero boundary
        assertEquals(20, newP.getX());
        assertEquals(20, newP.getY());

        // Test with negative indexZero (should ignore)
        v = new Velocity(-5, -5);
        v.setDimensions(100, 100);
        v.setIndexZero(new Point(-10, 10));
        p = new Point(15, 15);
        v.matchDimensions(p, 5);
        newP = v.applyToPoint(p);

        assertEquals(10, newP.getX());
        assertEquals(10, newP.getY());
    }

    @Test
    public void testSetExclusions() {
        Velocity v = new Velocity(4, 4);
        v.setDimensions(100, 100);

        // Create an exclusion region in the middle
        Point[] excludedStart = new Point[]{new Point(40, 40)};
        Point[] excludedEnd = new Point[]{new Point(60, 60)};
        v.setExclusions(excludedStart, excludedEnd);

        // Test approaching exclusion zone
        Point p = new Point(35, 35);
        v.matchDimensions(p, 5);
        Point newP = v.applyToPoint(p);

        // We should see a velocity change to avoid the exclusion zone
        assertNotEquals(40, newP.getX());
        assertNotEquals(40, newP.getY());
    }

    @Test
    public void testMatchDimensionsBoundaries() {
        // Test hitting the right boundary
        Velocity v1 = new Velocity(5, 0);
        v1.setDimensions(100, 100);
        Point p1 = new Point(98, 50);
        v1.matchDimensions(p1, 5);
        Point newP1 = v1.applyToPoint(p1);

        // Should bounce off right wall
        assertEquals(93, newP1.getX());
        assertEquals(50, newP1.getY());

        // Test hitting the left boundary
        Velocity v2 = new Velocity(-5, 0);
        v2.setDimensions(100, 100);
        v2.setIndexZero(new Point(10, 10));
        Point p2 = new Point(15, 50);
        v2.matchDimensions(p2, 5);
        Point newP2 = v2.applyToPoint(p2);

        // Should bounce off left wall
        assertEquals(20, newP2.getX());
        assertEquals(50, newP2.getY());

        // Test hitting the bottom boundary
        Velocity v3 = new Velocity(0, 5);
        v3.setDimensions(100, 100);
        Point p3 = new Point(50, 98);
        v3.matchDimensions(p3, 5);
        Point newP3 = v3.applyToPoint(p3);

        // Should bounce off bottom wall
        assertEquals(50, newP3.getX());
        assertEquals(93, newP3.getY());

        // Test hitting the top boundary
        Velocity v4 = new Velocity(0, -5);
        v4.setDimensions(100, 100);
        v4.setIndexZero(new Point(10, 10));
        Point p4 = new Point(50, 15);
        v4.matchDimensions(p4, 5);
        Point newP4 = v4.applyToPoint(p4);

        // Should bounce off top wall
        assertEquals(50, newP4.getX());
        assertEquals(20, newP4.getY());
    }

    @Test
    public void testNoMovement() {
        Velocity v = new Velocity(0, 0);
        Point p = new Point(50, 50);
        v.matchDimensions(p, 10);
        Point newP = v.applyToPoint(p);

        // Should not move
        assertEquals(50, newP.getX());
        assertEquals(50, newP.getY());
    }

    @Test
    public void testAllDirectionsBlocked() {
        // Create a velocity
        Velocity v = new Velocity(5, 5);
        v.setDimensions(100, 100);

        // Create exclusions that block all directions
        Point[] excludedStart = new Point[]{
                new Point(55, 45), // blocks right
                new Point(45, 55), // blocks down
                new Point(35, 45), // blocks left
                new Point(45, 35)  // blocks up
        };
        Point[] excludedEnd = new Point[]{
                new Point(65, 55),
                new Point(55, 65),
                new Point(45, 55),
                new Point(55, 45)
        };
        v.setExclusions(excludedStart, excludedEnd);

        // Position the point so it's surrounded
        Point p = new Point(50, 50);
        v.matchDimensions(p, 5);
        Point newP = v.applyToPoint(p);

        // The velocity should have to either rotate or reset to 0
        // Since we can't test internal state directly, we'll just verify
        // that it's not trying to move into the exclusion zones
        for (int i = 0; i < excludedStart.length; i++) {
            assertFalse(
                    newP.getX() + 5 > excludedStart[i].getX() &&
                            newP.getX() - 5 < excludedEnd[i].getX() &&
                            newP.getY() + 5 > excludedStart[i].getY() &&
                            newP.getY() - 5 < excludedEnd[i].getY()
            );
        }
    }
}
