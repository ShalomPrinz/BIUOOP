import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rectangle defined by an origin point, width and height.
 */
public class Rectangle implements Collidable {
    private final Point origin;
    private final double width;
    private final double height;
    // Save rectangle edges - optimization to prevent redundant calculations
    // Saved in this order: Top, Right, Bottom, Left
    private Line[] edges;

    /**
     * Constructor for rectangle.
     * @param origin top left corner of rectangle
     * @param width rectangle width
     * @param height rectangle height
     */
    public Rectangle(Point origin, double width, double height) {
        // Validate correctness of arguments
        if (origin == null || width <= 0 || height <= 0) {
            // Create a default rectangle for malformed input
            this.origin = new Point(0, 0);
            this.width = 0;
            this.height = 0;
            this.edges = new Line[0];
            return;
        }

        this.origin = origin;
        this.width = width;
        this.height = height;
        calculateRectangleEdges();
    }

    /**
     * Optimizes rectangle edges calculations.
     */
    private void calculateRectangleEdges() {
        // Shortcut rectangle corners
        Point tl = this.origin;
        Point tr = new Point(this.origin.getX() + this.width, this.origin.getY());
        Point br = new Point(this.origin.getX() + this.width, this.origin.getY() + this.height);
        Point bl = new Point(this.origin.getX(), this.origin.getY() + this.height);

        // Define rectangle edges
        this.edges = new Line[]{new Line(tl, tr), new Line(tr, br), new Line(br, bl), new Line(bl, tl)};
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return this;
    }

    @Override
    public Velocity hit(Point cp, Velocity velocity) {
        velocity.collide(cp, this);
        return velocity;
    }

    @Override
    public CollisionEdge getCollisionEdge(Point cp) {
        CollisionEdge edge = null;
        for (int i = 0; i < edges.length; i++) {
            if (edges[i].isPointOnLine(cp)) {
                // If collides with two edges, that's a corner collision
                if (edge != null) {
                    return CollisionEdge.CORNER;
                }
                edge = CollisionEdge.get(i);
            }
        }
        return edge;
    }

    /**
     * Calculates intersection points with the given line.
     * @param line line to calculate intersections with
     * @return a list of intersection points, or null if there are no intersection
     */
    public List<Point> intersectionPoints(Line line) {
        List<Point> intersections = new ArrayList<>();

        // Search for intersections with each of rectangle edges
        for (int i = 0; i < this.edges.length; i++) {
            Point intersection = line.intersectionWith(this.edges[i]);
            if (intersection == null) {
                continue;
            }

            // Validate no duplicate intersection points
            boolean duplicate = false;
            for (int j = 0; j < intersections.size(); j++) {
                if (intersection.equals(intersections.get(j))) {
                    duplicate = true;
                    break;
                }
            }

            // Save intersection if not a duplicate
            if (!duplicate) {
                intersections.add(intersection);
            }
        }

        return new ArrayList<>(intersections);
    }

    /**
     *
     * @return rectangle width
     */
    public double getWidth() {
        return this.width;
    }

    /**
     *
     * @return rectangle height
     */
    public double getHeight() {
        return this.height;
    }

    /**
     *
     * @return rectangle origin point
     */
    @Deprecated
    public Point getUpperLeft() {
        return this.origin;
    }

    /**
     *
     * @return rectangle origin point
     */
    public Point getOrigin() {
        return this.origin;
    }
}