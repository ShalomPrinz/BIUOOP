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
    private Point dimensions;

    /**
     * Constructor with center, radius and color.
     * @param center circle center
     * @param r circle radius
     * @param color circle color
     */
    public Ball(Point center, int r, Color color) {
        this.center = center;
        this.radius = r;
        this.color = color;
        this.velocity = new Velocity(0, 0);
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
     * @return ball size (pi * radius ^ 2)
     */
    public int getSize() {
        return (int) Math.PI * this.radius * this.radius;
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
        this.velocity = new Velocity(dx, dy);
    }

    /**
     *
     * @return ball velocity
     */
    public Velocity getVelocity() {
        return this.velocity;
    }

    /**
     * Sets dimensions for ball movement, and assures positive dimensions.
     * @param width max value of x for ball movement
     * @param height max value of y for ball movement
     */
    public void setDimensions(int width, int height) {
        // Setting width and height only if both are positive
        if (width > 0 && height > 0) {
            this.dimensions = new Point(width, height);
        }
    }

    /**
     * Moves ball one step with its velocity.
     * If ball movement is scoped within dimensions, changes velocity accordingly (if necessary).
     */
    public void moveOneStep() {
        // Matches ball dimensions only if those exist
        if (this.dimensions != null) {
            this.velocity.matchDimensions(this.center, this.dimensions);
        }
        this.center = this.velocity.applyToPoint(this.center);
    }
}
