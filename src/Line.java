/**
 * Represents a line segment defined by two points.
 */
public class Line {
    static final double COMPARISON_THRESHOLD = 0.00001;
    private Point start;
    private Point end;

    /**
     * Constructor with points.
     * @param start of line
     * @param end of line
     */
    public Line(Point start, Point end) {
        this.start = new Point(start.getX(), start.getY());
        this.end = new Point(end.getX(), end.getY());
    }

    /**
     * Constructor with coordinates.
     * @param x1 x val of first point
     * @param y1 y val of first point
     * @param x2 x val of second point
     * @param y2 y val of second point
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.start = new Point(x1, y1);
        this.end = new Point(x2, y2);
    }

    /**
     *
     * @return Line's length
     */
    public double length() {
        return this.start.distance(end);
    }

    /**
     *
     * @return middle point of line
     */
    public Point middle() {
        return new Point((this.start.getX() + this.end.getX()) / 2,
                (this.start.getY() + this.end.getY()) / 2);
    }

    /**
     *
     * @return start point of the line
     */
    public Point start() {
        return this.start;
    }

    /**
     *
     * @return end point of the line
     */
    public Point end() {
        return this.end;
    }

    /**
     * Determines if this line intersects with given line.
     * @param other line to check intersection
     * @return whether this line and other line intersects
     */
    public boolean isIntersecting(Line other) {
        // Shortcut coordinates
        Point a = this.start(), b = this.end(), c = other.start(), d = other.end();

        // First - check if given lines are sharing endpoints
        if (a.equals(c) || a.equals(d) || b.equals(c) || b.equals(d)) {
            return true;
        }

        // Calculate orientations (for intersection formula)
        int o1 = orientation(a, c, d);
        int o2 = orientation(b, c, d);
        int o3 = orientation(a, b, c);
        int o4 = orientation(a, b, d);

        // General case
        if (o1 != o2 && o3 != o4) {
            return true;
        }

        // Special Cases (Collinear)
        return (o1 == 0 && onSegment(a, c, b))
                || (o4 == 0 && onSegment(a, d, b))
                || (o3 == 0 && onSegment(c, a, d))
                || (o2 == 0 && onSegment(c, b, d));
    }

    /**
     * Calculates orientation of three points.
     * @param p first point
     * @param q second point
     * @param r third point
     * @return whether going from p to q to r is a clockwise turn (1) or counterclockwise (2), or all in same line (0)
     */
    private int orientation(Point p, Point q, Point r) {
        // Orientation formula
        double val = (q.getY() - p.getY()) * (r.getX() - q.getX())
                   - (q.getX() - p.getX()) * (r.getY() - q.getY());
        if (val == 0) {
            return 0; // Collinear
        }
        return (val > 0) ? 1 : 2; // Clockwise or counterclockwise
    }

    /**
     * Determines if this line intersects with both given lines.
     * @param other1 first line
     * @param other2 second line
     * @return whether given lines and this line intersect
     */
    public boolean isIntersecting(Line other1, Line other2) {
        return this.isIntersecting(other1) && this.isIntersecting(other2);
    }

    /**
     * Checks if a point q is on between points p and r.
     * Assumes that points p, q, and r are collinear.
     * @param p first point
     * @param q the point being checked
     * @param r second point
     * @return whether point q is on the segment between points p and r
     */
    private boolean onSegment(Point p, Point q, Point r) {
        // Shortcut for all points
        double qx = q.getX();
        double qy = q.getY();
        double px = p.getX();
        double py = p.getY();
        double rx = r.getX();
        double ry = r.getY();

        // Calculate with a pre-determined threshold
        return qx <= Math.max(px, rx) + COMPARISON_THRESHOLD && qx >= Math.min(px, rx) - COMPARISON_THRESHOLD
                && qy <= Math.max(py, ry) + COMPARISON_THRESHOLD && qy >= Math.min(py, ry) - COMPARISON_THRESHOLD;
    }

    /**
     * Calculates point of intersection point between this line and another line, if it exists.
     *
     * @param other line to calculate the intersection with
     * @return intersection point if exists, else null
     */
    public Point intersectionWith(Line other) {
        // Validate there is an intersection
        if (!isIntersecting(other)) {
            return null;
        }

        // Shortcut for all relevant points
        Point a = other.start(), b = other.end(), c = this.start(), d = this.end();

        // Collinear check
        if (orientation(a, b, c) == 0 && orientation(a, b, d) == 0) {
            // Check if lines overlap
            if (onSegment(a, c, b) || onSegment(a, d, b)
                    || onSegment(c, a, d) || onSegment(c, b, d)) {
                // Special case: check if they only share exactly one endpoint
                if ((a.equals(c) && !onSegment(a, d, b) && !onSegment(c, b, d))
                        || (a.equals(d) && !onSegment(a, c, b) && !onSegment(d, b, c))
                        || (b.equals(c) && !onSegment(b, d, a) && !onSegment(c, a, d))
                        || (b.equals(d) && !onSegment(b, c, a) && !onSegment(d, a, c))) {
                    if (a.equals(c) || a.equals(d)) {
                        return a;
                    } else if (b.equals(c) || b.equals(d)) {
                        return b;
                    }
                }

                // Lines overlap in a segment, not just a point
                return null;
            }
        }

        // Calculate first line data (AB: other)
        double dx1 = b.getX() - a.getX(), dy1 = b.getY() - a.getY();
        double firstOffset = dy1 * a.getX() + (-dx1) * a.getY();

        // Calculate second line data (CD: this)
        double dx2 = d.getX() - c.getX(), dy2 = d.getY() - c.getY();
        double secondOffset = dy2 * c.getX() + (-dx2) * c.getY();

        // Calculate determinant and confirm intersection found
        double det = dy1 * dx2 - dy2 * dx1;
        if (Math.abs(det) < COMPARISON_THRESHOLD) {
            return null;
        }

        // Calculate intersection point
        double x = (dx2 * firstOffset - dx1 * secondOffset) / det;
        double y = (dy2 * firstOffset - dy1 * secondOffset) / det;

        return new Point(x, y);
    }

    /**
     * Compares this line to given line.
     * @param other line to compare
     * @return whether this and other lines are equal
     */
    @Override
    public boolean equals(Object other) {
        // Validate given object is a Line object
        if (!(other instanceof Line)) {
            return false;
        }

        // Cast given object to Line to check lines equality
        Line otherLine = (Line) other;
        if (this.start.equals(otherLine.start)) {
            return this.end.equals(otherLine.end);
        } else if (this.start.equals(otherLine.end)) {
            return this.end.equals(otherLine.start);
        }
        return false;
    }

    /**
     * @return a string representation of this Line instance
     */
    @Override
    public String toString() {
        return this.start + " -> " + this.end;
    }
}
