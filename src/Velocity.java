
public class Velocity {
    private double dx;
    private double dy;

    public Velocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public static Velocity fromAngleAndSpeed(double angle, double speed) {
        double angleRad = Math.toRadians(angle);
        double dx = speed * Math.cos(angleRad);
        double dy = speed * Math.sin(angleRad);
        return new Velocity(dx, dy);
    }

    public Point applyToPoint(Point p) {
        return new Point(p.getX() + this.dx, p.getY() + this.dy);
    }

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
