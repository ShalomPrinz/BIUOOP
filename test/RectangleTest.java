import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
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

        // Test deprecated method
        assertEquals(origin.getX(), rect.getUpperLeft().getX());
        assertEquals(origin.getY(), rect.getUpperLeft().getY());
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
}
