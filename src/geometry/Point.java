package geometry;

/**
 * Represents a point defined by x and y values.
 */
public class Point {
    static final double COMPARISON_THRESHOLD = 0.00001;
    private double x;
    private double y;

    /**
     * Constructor of Point.
     * @param x x value of Point
     * @param y y value of Point
     */
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Calculates distance from given point.
     * @param other point to calculate distance from
     * @return distance of this point to the other point
     */
    public double distance(Point other) {
        return Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y));
    }

    /**
     * Compares points.
     * @param other point to compare
     * @return whether this point equals to other point
     */
    @Override
    public boolean equals(Object other) {
        // Validate given object is a Point object
        if (!(other instanceof Point)) {
            return false;
        }

        // Cast given object to Point to check points equality
        Point otherPoint = (Point) other;
        return Math.abs(this.x - otherPoint.x) < COMPARISON_THRESHOLD
                && Math.abs(this.y - otherPoint.y) < COMPARISON_THRESHOLD;
    }

    /**
     * @return x value of point
     */
    public double getX() {
        return this.x;
    }

    /**
     * @return y value of point
     */
    public double getY() {
        return this.y;
    }

    /**
     * @return a string representation of this Point instance
     */
    @Override
    public String toString() {
        return String.format("(%.2f, %.2f)", this.x, this.y);
    }
}
