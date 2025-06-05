import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import collisions.CollisionEdge;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;

import java.util.List;

/**
 * Tests for the Rectangle class and new Line methods.
 */
public class RectangleTest {

    @Test
    public void testRectangleConstructor() {
        Point origin = new Point(10, 20);
        Rectangle rect = new Rectangle(origin, 50, 30);

        assertEquals(10, rect.getOrigin().getX());
        assertEquals(20, rect.getOrigin().getY());
        assertEquals(50, rect.getWidth());
        assertEquals(30, rect.getHeight());
    }

    @Test
    public void testRectangleInvalidConstructor() {
        // Test with null origin
        Rectangle rect1 = new Rectangle(null, 50, 30);
        assertEquals(0, rect1.getOrigin().getX());
        assertEquals(0, rect1.getOrigin().getY());
        assertEquals(0, rect1.getWidth());
        assertEquals(0, rect1.getHeight());

        // Test with invalid dimensions
        Rectangle rect2 = new Rectangle(new Point(10, 20), -5, 30);
        assertEquals(0, rect2.getWidth());
        assertEquals(0, rect2.getHeight());

        Rectangle rect3 = new Rectangle(new Point(10, 20), 50, -10);
        assertEquals(0, rect3.getWidth());
        assertEquals(0, rect3.getHeight());
    }

    @Test
    public void testIntersectionPoints() {
        Rectangle rect = new Rectangle(new Point(100, 100), 100, 100);

        // Line that crosses rectangle
        Line line1 = new Line(50, 150, 250, 150);
        List<Point> intersections1 = rect.intersectionPoints(line1);
        assertEquals(2, intersections1.size());
        assertTrue(intersections1.contains(new Point(100, 150)));
        assertTrue(intersections1.contains(new Point(200, 150)));

        // Line that passes through corner
        Line line2 = new Line(100, 100, 300, 300);
        List<Point> intersections2 = rect.intersectionPoints(line2);
        assertEquals(2, intersections2.size());
        assertTrue(intersections2.contains(new Point(100, 100)));
        assertTrue(intersections2.contains(new Point(200, 200)));

        // Line completely outside rectangle
        Line line3 = new Line(50, 50, 80, 80);
        List<Point> intersections3 = rect.intersectionPoints(line3);
        assertEquals(0, intersections3.size());

        // Line that touches one side
        Line line4 = new Line(150, 50, 150, 250);
        List<Point> intersections4 = rect.intersectionPoints(line4);
        assertEquals(2, intersections4.size());
        assertTrue(intersections4.contains(new Point(150, 100)));
        assertTrue(intersections4.contains(new Point(150, 200)));
    }

    @Test
    public void testGetCollisionEdge() {
        Point origin = new Point(100, 100);
        Rectangle rect = new Rectangle(origin, 50, 30);

        // Test top edge collision
        Point topPoint = new Point(125, 100);
        assertEquals(CollisionEdge.TOP, rect.getCollisionEdge(topPoint));

        // Test right edge collision
        Point rightPoint = new Point(150, 115);
        assertEquals(CollisionEdge.RIGHT, rect.getCollisionEdge(rightPoint));

        // Test bottom edge collision
        Point bottomPoint = new Point(125, 130);
        assertEquals(CollisionEdge.BOTTOM, rect.getCollisionEdge(bottomPoint));

        // Test left edge collision
        Point leftPoint = new Point(100, 115);
        assertEquals(CollisionEdge.LEFT, rect.getCollisionEdge(leftPoint));

        // Test corner collision (top-left)
        Point cornerPoint = new Point(100, 100);
        assertEquals(CollisionEdge.CORNER, rect.getCollisionEdge(cornerPoint));

        // Test point not on any edge
        Point outsidePoint = new Point(125, 115);
        assertNull(rect.getCollisionEdge(outsidePoint));
    }

    @Test
    public void testIsBallInside() {
        Rectangle rect = new Rectangle(new Point(10, 10), 20, 20);

        // Test ball completely inside rectangle
        assertTrue(rect.isBallInside(new Point(20, 20), 5));

        // Test ball center inside but part of it extends outside
        assertTrue(rect.isBallInside(new Point(12, 12), 5));

        // Test ball center on edge with half inside, half outside
        assertTrue(rect.isBallInside(new Point(10, 20), 5));
        assertTrue(rect.isBallInside(new Point(20, 10), 5));
        assertTrue(rect.isBallInside(new Point(30, 20), 5));
        assertTrue(rect.isBallInside(new Point(20, 30), 5));

        // Test ball center outside but part of it extends inside
        assertTrue(rect.isBallInside(new Point(5, 15), 6));  // Left side
        assertTrue(rect.isBallInside(new Point(15, 5), 6));  // Top side
        assertTrue(rect.isBallInside(new Point(35, 15), 6)); // Right side
        assertTrue(rect.isBallInside(new Point(15, 35), 6)); // Bottom side

        // Test ball completely outside rectangle
        assertFalse(rect.isBallInside(new Point(5, 5), 4));     // Top-left
        assertFalse(rect.isBallInside(new Point(35, 5), 4));    // Top-right
        assertFalse(rect.isBallInside(new Point(5, 35), 4));    // Bottom-left
        assertFalse(rect.isBallInside(new Point(35, 35), 4));   // Bottom-right
        assertFalse(rect.isBallInside(new Point(0, 20), 5));    // Far left
        assertFalse(rect.isBallInside(new Point(20, 0), 5));    // Far top
        assertFalse(rect.isBallInside(new Point(40, 20), 5));   // Far right
        assertFalse(rect.isBallInside(new Point(20, 40), 5));   // Far bottom

        // Test ball with zero radius (just the center point)
        assertTrue(rect.isBallInside(new Point(15, 15), 0));
        assertFalse(rect.isBallInside(new Point(5, 5), 0));
    }
}
