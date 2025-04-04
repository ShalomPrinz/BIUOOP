/**
 * Represents a velocity in space.
 */
public class Velocity {
    private double dx;
    private double dy;

    /**
     * Constructor with deltas.
     * @param dx x delta
     * @param dy y delta
     */
    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
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
     * @param p point to apply velocity on
     * @return new point with velocity deltas applied
     */
    public Point applyToPoint(Point p) {
        return new Point(p.getX() + this.dx, p.getY() + this.dy);
    }

    /**
     * Adjusts velocity within given dimensions by inverting direction on collision.
     * @param p ball center
     * @param radius ball radius
     * @param dimensions ball movement limit (max values)
     */
    public void matchDimensions(Point p, int radius, Point dimensions) {
        double newX = p.getX() + this.dx;
        double newY = p.getY() + this.dy;
        if (newX + radius > dimensions.getX() || (this.dx < 0 && newX < radius)) {
            this.dx = -this.dx;
        }
        if (newY + radius > dimensions.getY() || (this.dy < 0 && newY < radius)) {
            this.dy = -this.dy;
        }
    }

    /**
     * Adjusts velocity within given dimensions by inverting direction on collision.
     * @param p ball center
     * @param radius ball radius
     * @param dimensions ball movement limit (max values)
     * @param indexZero ball movement limit (min values)
     */
    public void matchDimensions(Point p, int radius, Point dimensions, Point indexZero) {
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
