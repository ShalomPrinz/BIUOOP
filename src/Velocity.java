/**
 * Represents a velocity of an object with radius in space.
 */
public class Velocity {
    private double dx;
    private double dy;
    private Point dimensions; // Max position values allowed
    private Point indexZero; // Min position values allowed

    /**
     * Constructor with deltas.
     * @param dx x delta
     * @param dy y delta
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
        // Initialize movement scope with default values
        this.indexZero = new Point(0, 0);
        this.dimensions = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    /**
     * Constructs a velocity with angle and speed by converting them to deltas.
     * @param angle velocity angle
     * @param speed velocity speed
     * @return velocity defined with given angle and speed
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double angleRad = Math.toRadians(angle);
        double dx = speed * Math.cos(angleRad);
        double dy = speed * Math.sin(angleRad);
        return new Velocity(dx, dy);
    }

    /**
     * Applies velocity deltas to a given point.
     * Safety note: should call only after matching velocity with object dimensions using matchDimensions.
     * @param p point to apply velocity on
     * @return new point with velocity deltas applied
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + this.dx, p.getY() + this.dy);
    }

    /**
     * Sets dimensions for movement using coordinates, and assures positive dimensions.
     * @param x max value of x for movement
     * @param y max value of y for movement
     */
    public void setDimensions(double x, double y) {
        setDimensions(new Point(x, y));
    }

    /**
     * Sets dimensions for movement using point, and assures positive dimensions.
     * @param dimensions point that represents dimensions (max values)
     */
    public void setDimensions(Point dimensions) {
        // Setting to given dimensions only if both coordinates of given point are positive
        if (dimensions.getX() >= 0 && dimensions.getY() >= 0) {
            this.dimensions = dimensions;
        }
    }

    /**
     * Sets minimum position values for movement, and assures positive dimensions.
     * @param indexZero point that represents dimensions
     */
    public void setIndexZero(Point indexZero) {
        // Setting to given indexZero only if both coordinates of given point are positive
        if (indexZero.getX() >= 0 && indexZero.getY() >= 0) {
            this.indexZero = indexZero;
        }
    }

    /**
     * Adjusts velocity within given dimensions by inverting direction on collision.
     * @param p object center
     * @param radius object radius
     */
    public void matchDimensions(Point p, int radius) {
        double newX = p.getX() + this.dx;
        double newY = p.getY() + this.dy;
        if (newX + radius > dimensions.getX() || (this.dx < 0 && newX < radius + indexZero.getX())) {
            this.dx = -this.dx;
        }
        if (newY + radius > dimensions.getY() || (this.dy < 0 && newY < radius + indexZero.getY())) {
            this.dy = -this.dy;
        }
    }
}
