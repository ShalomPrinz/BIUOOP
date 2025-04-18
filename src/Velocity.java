/**
 * Represents a velocity of an object with radius in space.
 */
public class Velocity {
    private double dx;
    private double dy;
    private boolean hasMoved = false;
    private static final int ROTATION_DEGREES = 10; // Calculated by max degree change for max speed
    // Movement scope
    private Point dimensions;
    private Point indexZero;
    // Excluded movement within movement scope
    private Point[] excludedIndexZero;
    private Point[] excludedDimensions;

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
        // Defaults to no scope exclusion
        this.excludedIndexZero = new Point[]{};
        this.excludedDimensions = new Point[]{};
    }

    /**
     * Constructs a velocity with angle and speed by converting them to deltas.
     * @param angle velocity angle
     * @param speed velocity speed
     * @return velocity defined with given angle and speed
     */
    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        // angle - 90: Make sure 0 is up, 90 is right, etc.
        double angleRad = Math.toRadians(angle - 90);
        double dx = speed * Math.cos(angleRad);
        double dy = speed * Math.sin(angleRad);
        return new Velocity(dx, dy);
    }

    /**
     * Rotate velocity direction at given degrees by a rotation formula.
     * @param degrees to rotate
     */
    private void rotate(double degrees) {
        double radians = Math.toRadians(degrees);
        double newDx = this.dx * Math.cos(radians) - this.dy * Math.sin(radians);
        double newDy = this.dx * Math.sin(radians) + this.dy * Math.cos(radians);
        this.dx = newDx;
        this.dy = newDy;
    }

    /**
     * Resets velocity to zero movement.
     */
    private void reset() {
        this.dx = 0;
        this.dy = 0;
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
     * Sets regions which velocity will be responsible for preventing object to reach inside them.
     * Note: indexZero[i] along with dimensions[i] mark together excluded rectangle #i.
     * @param indexZero start points of excluded rectangles
     * @param dimensions end points of excluded rectangles
     */
    public void setExclusions(Point[] indexZero, Point[] dimensions) {
        this.excludedIndexZero = indexZero;
        this.excludedDimensions = dimensions;
    }

    /**
     *
     * @param p object position
     * @param radius object radius
     * @return whether object next move will reach excluded regions
     */
    private boolean isInExclusion(Point p, int radius) {
        for (int i = 0; i < this.excludedIndexZero.length; i++) {
            double startX = this.excludedIndexZero[i].getX();
            double startY = this.excludedIndexZero[i].getY();
            double endX = this.excludedDimensions[i].getX();
            double endY = this.excludedDimensions[i].getY();

            // Checks whether movement is inside exclusion rectangle
            if (p.getX() + this.dx + radius > startX && p.getX() + this.dx - radius < endX
                    && p.getY() + this.dy + radius > startY && p.getY() + this.dy - radius < endY) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether movement is within dimensions and outside exclusion region.
     * @param p object position
     * @param radius object radius
     * @return whether current velocity settings over given axis moves object into disallowed region
     */
    private boolean isMovementDisallowed(Point p, int radius) {
         // Validate object moves inside indexZero and dimension regions
        if (p.getX() + this.dx + radius > this.dimensions.getX()
            || p.getX() + this.dx - radius < this.indexZero.getX()
            || p.getY() + this.dy + radius > this.dimensions.getY()
            || p.getY() + this.dy - radius < this.indexZero.getY()) {
            return true;
        }

        // Validate object moves outside any exclusion region
        return isInExclusion(p, radius);
    }

    /**
     * Adjusts next velocity movement within dimensions and outside exclusion regions.
     * If movement is not allowed, inverts it, and if it's still disallowed, resets velocity (no movement).
     * @param p object center
     * @param radius object radius
     */
    public void matchDimensions(Point p, int radius) {
        // Don't try matching dimensions if velocity set to no movement
        if (this.dx == 0 && this.dy == 0) {
            return;
        }

        // Check movement availability over all possible combinations of dx and dy
        // Check movement availability: X, Y
        if (isMovementDisallowed(p, radius)) {
            this.dx = -this.dx;
            // Check movement availability: -X, Y
            if (isMovementDisallowed(p, radius)) {
                this.dx = -this.dx;
                this.dy = -this.dy;
                // Check movement availability: X, -Y
                if (isMovementDisallowed(p, radius)) {
                    this.dx = -this.dx;
                    // Check movement availability: -X, -Y
                    if (isMovementDisallowed(p, radius)) {
                        // Try rotating velocity only if object didn't move yet
                        if (!this.hasMoved) {
                            this.hasMoved = true;
                            // No possible movement, rotate velocity until found a possible angle
                            int degrees = ROTATION_DEGREES;
                            // Usually it takes 1-5 rotations, theoretically limited with max of 10 rotations
                            while (degrees < 360) {
                                rotate(degrees);
                                if (!isMovementDisallowed(p, radius)) {
                                    return;
                                }
                                degrees += ROTATION_DEGREES;
                            }
                        }
                        // No possible angle for velocity speed. Shouldn't happen at all
                        this.reset();
                    }
                }
            }
        }
    }
}
