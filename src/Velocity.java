/**
 * Represents a velocity of an object with radius in space.
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
        // angle - 90: Make sure 0 is up, 90 is right, etc.
        double angleRad = Math.toRadians(angle - 90);
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

    public boolean isRight() {
        return this.dx > 0;
    }

    public boolean isBottom() {
        return this.dy > 0;
    }

    /**
     * Flips direction of velocity, determined by rectangle's collision edge.
     * @param cp collision point
     * @param c object that had been collided with
     */
    public void collide(Point cp, Collidable c) {
        // No velocity means no collision (and no direction flip)
        if (this.dx == 0 && this.dy == 0) {
            return;
        }

        CollisionEdge edge = c.getCollisionRectangle().getCollisionEdge(cp);
        if (edge == null) {
            return;
        }
        if (edge == CollisionEdge.CORNER) {
            this.dx = -this.dx;
            this.dy = -this.dy;
            return;
        }
        if (CollisionEdge.isHorizontal(edge)) {
            this.dx = -this.dx;
        } else {
            this.dy = -this.dy;
        }
    }
}
