/**
 * Represents a velocity of an object with radius in space.
 */
public class Velocity {
    private double dx;
    private double dy;
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
        double angleRad = Math.toRadians(angle);
        double dx = speed * Math.cos(angleRad);
        double dy = speed * Math.sin(angleRad);
        return new Velocity(dx, dy);
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
     * Checks whether movement is within dimensions and outside exclusion region.
     * @param position object current position
     * @param radius object radius
     * @param x true means x axis movement, false means y axis
     * @return whether current velocity settings over given axis moves object into disallowed region
     */
    private boolean isMovementDisallowed(double position, int radius, boolean x) {
        double movement = x ? this.dx : this.dy;

        // Validate object moves inside indexZero and dimensions regions
        double indexZero = x ? this.indexZero.getX() : this.indexZero.getY();
        double dimension = x ? this.dimensions.getX() : this.dimensions.getY();
        if (position + movement + radius > dimension || position + movement - radius < indexZero) {
            return true;
        }

        // Validate object moves outside any exclusion region
        for (int i = 0; i < this.excludedIndexZero.length; i++) {
            indexZero = x ? this.excludedIndexZero[i].getX() : this.excludedIndexZero[i].getY();
            dimension = x ? this.excludedDimensions[i].getX() : this.excludedDimensions[i].getY();
            // Checks whether movement is inside current exclusion region
            if (position + movement + radius > indexZero && position + movement - radius < dimension) {
                return true;
            }
        }

        // Movement has passed all validations and is allowed
        return false;
    }

    /**
     * Adjusts next velocity movement within dimensions and outside exclusion regions.
     * If movement is not allowed, inverts it, and if it's still disallowed, resets velocity (no movement).
     * @param p object center
     * @param radius object radius
     */
    public void matchDimensions(Point p, int radius) {
        // Validate x movement
        if (isMovementDisallowed(p.getX(), radius, true)) {
            this.dx = -this.dx;
            if (isMovementDisallowed(p.getX(), radius, true)) {
                this.reset();
                return;
            }
        }

        // Validate y movement
        if (isMovementDisallowed(p.getY(), radius, false)) {
            this.dy = -this.dy;
            if (isMovementDisallowed(p.getY(), radius, false)) {
                this.reset();
                return;
            }
        }
    }
}
