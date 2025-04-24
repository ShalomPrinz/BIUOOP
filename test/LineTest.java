import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for Line.
 */
public class LineTest {

    @Test
    public void testConstructorWithPoints() {
        Point start = new Point(1, 2);
        Point end = new Point(3, 4);
        Line line = new Line(start, end);

        assertEquals(1, line.start().getX());
        assertEquals(2, line.start().getY());
        assertEquals(3, line.end().getX());
        assertEquals(4, line.end().getY());
    }

    @Test
    public void testConstructorWithCoordinates() {
        Line line = new Line(1, 2, 3, 4);

        assertEquals(1, line.start().getX());
        assertEquals(2, line.start().getY());
        assertEquals(3, line.end().getX());
        assertEquals(4, line.end().getY());
    }

    @Test
    public void testLength() {
        Line line1 = new Line(0, 0, 3, 4);
        assertEquals(5, line1.length());

        // Test zero length
        Line line2 = new Line(1, 1, 1, 1);
        assertEquals(0, line2.length());

        // Test with negative coordinates
        Line line3 = new Line(-3, -4, 0, 0);
        assertEquals(5, line3.length());
    }

    @Test
    public void testMiddle() {
        Line line = new Line(0, 0, 4, 6);
        Point middle = line.middle();

        assertEquals(2, middle.getX());
        assertEquals(3, middle.getY());

        // Test with negative coordinates
        Line line2 = new Line(-2, -4, 2, 0);
        Point middle2 = line2.middle();

        assertEquals(0, middle2.getX());
        assertEquals(-2, middle2.getY());
    }

    @Test
    public void testStartAndEnd() {
        Point start = new Point(1, 2);
        Point end = new Point(3, 4);
        Line line = new Line(start, end);

        Point returnedStart = line.start();
        Point returnedEnd = line.end();

        // Check equality but ensure they're not the same object instances
        assertTrue(returnedStart.equals(start));
        assertTrue(returnedEnd.equals(end));
        assertFalse(returnedStart == start); // Should be a different object instance
        assertFalse(returnedEnd == end);     // Should be a different object instance
    }

    @Test
    public void testIsIntersectingSimple() {
        // Lines that definitely intersect
        Line line1 = new Line(0, 0, 2, 2);
        Line line2 = new Line(0, 2, 2, 0);
        assertTrue(line1.isIntersecting(line2));

        // Lines that don't intersect
        Line line3 = new Line(0, 0, 1, 1);
        Line line4 = new Line(2, 2, 3, 3);
        assertFalse(line3.isIntersecting(line4));
    }

    @Test
    public void testIsIntersectingSharedEndpoint() {
        // Lines that share an endpoint
        Line line1 = new Line(0, 0, 2, 2);
        Line line2 = new Line(2, 2, 4, 0);
        assertTrue(line1.isIntersecting(line2));
    }

    @Test
    public void testIsIntersectingCollinear() {
        // Collinear lines that overlap
        Line line1 = new Line(0, 0, 4, 4);
        Line line2 = new Line(2, 2, 6, 6);
        assertTrue(line1.isIntersecting(line2));

        // Collinear lines that don't overlap
        Line line3 = new Line(0, 0, 2, 2);
        Line line4 = new Line(3, 3, 5, 5);
        assertFalse(line3.isIntersecting(line4));
    }

    @Test
    public void testIsIntersectingWithTwoLines() {
        Line mainLine = new Line(0, 0, 4, 4);
        Line intersectingLine1 = new Line(0, 4, 4, 0);
        Line intersectingLine2 = new Line(2, 0, 2, 4);
        Line nonIntersectingLine = new Line(5, 5, 8, 8);

        assertTrue(mainLine.isIntersecting(intersectingLine1, intersectingLine2));
        assertFalse(mainLine.isIntersecting(intersectingLine1, nonIntersectingLine));
    }

    @Test
    public void testIntersectionWithSimple() {
        // Lines that intersect at a clear point
        Line line1 = new Line(0, 0, 4, 4);
        Line line2 = new Line(0, 4, 4, 0);
        Point intersection = line1.intersectionWith(line2);

        assertNotNull(intersection);
        assertEquals(2, intersection.getX());
        assertEquals(2, intersection.getY());
    }

    @Test
    public void testIntersectionWithNoIntersection() {
        // Parallel lines
        Line line1 = new Line(0, 0, 4, 4);
        Line line2 = new Line(1, 0, 5, 4);
        Point intersection = line1.intersectionWith(line2);

        assertNull(intersection);
    }

    @Test
    public void testIntersectionWithCollinearOverlap() {
        // Collinear lines that overlap
        Line line1 = new Line(0, 0, 4, 4);
        Line line2 = new Line(2, 2, 6, 6);
        Point intersection = line1.intersectionWith(line2);

        assertNull(intersection); // Should be null for overlapping lines
    }

    @Test
    public void testIntersectionWithSharedEndpoint() {
        // Lines that share an endpoint
        Line line1 = new Line(0, 0, 2, 2);
        Line line2 = new Line(2, 2, 4, 0);
        Point intersection = line1.intersectionWith(line2);

        assertNotNull(intersection);
        assertEquals(2, intersection.getX());
        assertEquals(2, intersection.getY());
    }

    @Test
    public void testClosestIntersectionToStartOfLine() {
        Rectangle rect = new Rectangle(new Point(100, 100), 100, 100);

        // Line from outside going through rectangle
        Line line1 = new Line(50, 50, 250, 250);
        Point closest1 = line1.closestIntersectionToStartOfLine(rect);
        assertEquals(100, closest1.getX());
        assertEquals(100, closest1.getY());

        // Line starting inside rectangle
        Line line2 = new Line(150, 150, 300, 300);
        Point closest2 = line2.closestIntersectionToStartOfLine(rect);
        assertEquals(200, closest2.getX());
        assertEquals(200, closest2.getY());

        // Line with no intersection
        Line line3 = new Line(0, 0, 50, 50);
        Point closest3 = line3.closestIntersectionToStartOfLine(rect);
        assertNull(closest3);

        // Line that intersects multiple sides, check closest is returned
        Line line4 = new Line(0, 150, 300, 150);
        Point closest4 = line4.closestIntersectionToStartOfLine(rect);
        assertEquals(100, closest4.getX());
        assertEquals(150, closest4.getY());

        // Line that starts at an intersection point
        Line line5 = new Line(100, 100, 300, 300);
        Point closest5 = line5.closestIntersectionToStartOfLine(rect);
        assertEquals(100, closest5.getX());
        assertEquals(100, closest5.getY());
    }

    @Test
    public void testClosestIntersectionWithEmptyRectangle() {
        Rectangle emptyRect = new Rectangle(new Point(0, 0), 0, 0);
        Line line = new Line(0, 0, 100, 100);

        Point closest = line.closestIntersectionToStartOfLine(emptyRect);
        assertNull(closest);
    }

    @Test
    public void testClosestIntersectionEdgeCases() {
        Rectangle rect = new Rectangle(new Point(100, 100), 100, 100);

        // Line tangent to rectangle (touching but not crossing)
        Line tangentLine = new Line(100, 50, 200, 50);
        Point tangentClosest = tangentLine.closestIntersectionToStartOfLine(rect);
        assertNull(tangentClosest);

        // Line that intersects at exactly the same distance from start
        // This is a contrived example where numerical precision might matter
        Line equidistantLine = new Line(50, 150, 250, 150);
        Point equidistantClosest = equidistantLine.closestIntersectionToStartOfLine(rect);
        assertEquals(100, equidistantClosest.getX());
        assertEquals(150, equidistantClosest.getY());
    }

    @Test
    public void testEquals() {
        Line line1 = new Line(0, 0, 2, 2);
        Line line2 = new Line(0, 0, 2, 2);
        Line line3 = new Line(2, 2, 0, 0); // Same line but different direction
        Line line4 = new Line(0, 0, 3, 3);

        assertTrue(line1.equals(line2));
        assertTrue(line1.equals(line3)); // Should be equal regardless of direction
        assertFalse(line1.equals(line4));
        assertFalse(line1.equals("Not a line"));
    }

    @Test
    public void testToString() {
        Line line = new Line(1.5, 2.5, 3.5, 4.5);
        assertEquals("(1.50, 2.50) -> (3.50, 4.50)", line.toString());
    }
}
