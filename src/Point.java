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
    public boolean equals(Point other) {
        return Math.abs(this.x - other.x) < COMPARISON_THRESHOLD
                && Math.abs(this.y - other.y) < COMPARISON_THRESHOLD;
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

    public String toString() {
        return "(" + (int) this.x + ", " + (int) this.y + ")";
    }
}
