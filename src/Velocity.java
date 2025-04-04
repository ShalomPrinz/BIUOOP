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
     * @param p current point position
     * @param dimensions point movement limit (max values)
     */
    public void matchDimensions(Point p, Point dimensions) {
        double newX = p.getX() + this.dx;
        double newY = p.getY() + this.dy;
        if (newX > dimensions.getX() || newX < 0) {
            this.dx = -this.dx;
        }
        if (newY > dimensions.getY() || newY < 0) {
            this.dy = -this.dy;
        }
    }
}
