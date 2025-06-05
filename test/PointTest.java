import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import geometry.Point;

/**
 * Test class for Point.
 */
public class PointTest {

    @Test
    public void testConstructor() {
        Point p = new Point(5.0, 7.0);
        assertEquals(5.0, p.getX());
        assertEquals(7.0, p.getY());
    }

    @Test
    public void testDistance() {
        Point p1 = new Point(0, 0);
        Point p2 = new Point(3, 4);
        assertEquals(5.0, p1.distance(p2));

        // Distance to self should be 0
        assertEquals(0.0, p1.distance(p1));

        // Test with negative coordinates
        Point p3 = new Point(-3, -4);
        assertEquals(5.0, p1.distance(p3));
    }

    @Test
    public void testEquals() {
        Point p1 = new Point(2.0, 3.0);
        Point p2 = new Point(2.0, 3.0);
        Point p3 = new Point(2.0, 3.1);
        Point p4 = new Point(2.1, 3.0);
        Point p5 = new Point(2.000005, 3.0);

        // Test for equality
        assertEquals(p1, p2);
        assertNotEquals(p1, p3);
        assertNotEquals(p1, p4);

        // Test with very small difference (less than COMPARISON_THRESHOLD)
        assertEquals(p1, p5);

        // Test with non-Point object
        assertNotEquals("Not a point", p1);
        assertNotEquals(null, p1);
    }

    @Test
    public void testGetX() {
        Point p = new Point(-5.25, 10.75);
        assertEquals(-5.25, p.getX());
    }

    @Test
    public void testGetY() {
        Point p = new Point(-5.25, 10.75);
        assertEquals(10.75, p.getY());
    }

    @Test
    public void testToString() {
        Point p = new Point(5.0, 7.0);
        assertEquals("(5.00, 7.00)", p.toString());

        Point p2 = new Point(5.125, 7.875);
        assertEquals("(5.13, 7.88)", p2.toString()); // Should round to 2 decimal places
    }
}
