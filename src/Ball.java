import biuoop.DrawSurface;

import java.awt.Color;

/**
 * Represents a moving circle.
 */
public class Ball {
    private Point center;
    private int radius;
    private Color color;
    private Velocity velocity;
    private GameEnvironment environment;

    /**
     * Constructor with center point, radius and color.
     * @param center circle center
     * @param r circle radius
     * @param color circle color
     */
    public Ball(Point center, int r, Color color) {
        this.center = center;
        this.radius = r;
        this.color = color;
        this.velocity = new Velocity(0, 0);
        this.environment = new GameEnvironment();
    }

    /**
     * Constructor with center point values, radius and color.
     * @param x circle center x value
     * @param y circle center y value
     * @param r circle radius
     * @param color circle color
     */
    public Ball(double x, double y, int r, Color color) {
        this(new Point(x, y), r, color);
    }

    /**
     *
     * @param environment ball's game environment
     */
    public void setEnvironment(GameEnvironment environment) {
        this.environment = environment;
    }

    /**
     *
     * @return x value of ball's center
     */
    public int getX() {
        return (int) this.center.getX();
    }

    /**
     *
     * @return y value of ball's center
     */
    public int getY() {
        return (int) this.center.getY();
    }

    /**
     *
     * @return ball size (radius)
     */
    public int getSize() {
        return this.radius;
    }

    /**
     *
     * @return ball color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Draws ball on circle with defined attributes.
     * @param surface surface to draw on
     */
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle(this.getX(), this.getY(), this.radius);
    }

    /**
     * Sets ball velocity using a pre-defined velocity.
     * @param v new velocity
     */
    public void setVelocity(Velocity v) {
        this.velocity = v;
    }

    /**
     * Sets ball velocity using deltas.
     * @param dx x delta
     * @param dy y delta
     */
    public void setVelocity(double dx, double dy) {
        setVelocity(new Velocity(dx, dy));
    }

    /**
     *
     * @return ball velocity
     */
    public Velocity getVelocity() {
        return this.velocity;
    }

    /**
     * Moves ball one step with its velocity.
     */
    public void moveOneStep() {
        Line movement = new Line(this.center, this.velocity.applyToPoint(this.center));
        CollisionInfo info = this.environment.getClosestCollision(movement);

        // If no collision, simply move
        if (info == null) {
            this.center = this.velocity.applyToPoint(this.center);
            return;
        }

        // Collision algorithm
        // Move ball closer to collided object
        double newX = info.getPoint().getX(), newY = info.getPoint().getY();
        double threshold = GameEnvironment.COLLISION_THRESHOLD;
        if (CollisionEdge.isHorizontal(info.getEdge())) {
            newX += this.velocity.isRight() ? -threshold : threshold;
        } else {
            newY += this.velocity.isBottom() ? -threshold : threshold;
        }
        this.center = new Point(newX, newY);
        // Perform the object hit and change velocity accordingly
        this.velocity = info.getObject().hit(info.getPoint(), this.velocity);
    }
}
