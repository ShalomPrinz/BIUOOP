import java.util.ArrayList;
import java.util.List;

/**
 * Represents a rectangle defined by an origin point, width and height.
 */
public class Rectangle implements Collidable {
    private Point origin;
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

        this.width = width;
        this.height = height;
        this.setOrigin(origin);
    }

    /**
     * Sets origin and optimizes rectangle edges calculations.
     */
    public void setOrigin(Point origin) {
        this.origin = origin;

        // Shortcut rectangle corners
        Point tr = new Point(origin.getX() + this.width, origin.getY());
        Point br = new Point(origin.getX() + this.width, origin.getY() + this.height);
        Point bl = new Point(origin.getX(), origin.getY() + this.height);

        // Define rectangle edges
        this.edges = new Line[]{new Line(origin, tr), new Line(tr, br), new Line(br, bl), new Line(bl, origin)};
    }

    /**
     * Checks if given ball is inside this rectangle.
     * @param p ball position
     * @param radius ball radius
     * @return whether ball is inside this rectangle borders
     */
    public boolean isBallInside(Point p, int radius) {
        double startX = this.origin.getX();
        double startY = this.origin.getY();
        double endX = startX + this.width;
        double endY = startY + this.height;

        return p.getX() + radius > startX && p.getX() - radius < endX
                && p.getY() + radius > startY && p.getY() - radius < endY;
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
        if (cp == null) {
            return null;
        }

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