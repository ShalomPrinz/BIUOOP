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
    private Point dimensions; // Max position values allowed
    private Point indexZero; // Min position values allowed

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
     * Sets dimensions for ball movement using coordinates, and assures positive dimensions.
     * @param x max value of x for ball movement
     * @param y max value of y for ball movement
     */
    public void setDimensions(int x, int y) {
        // Setting x and y only if both are positive
        if (x >= 0 && y >= 0) {
            this.dimensions = new Point(x, y);
        }
    }

    /**
     * Sets dimensions for ball movement using point, and assures positive dimensions.
     * @param dimensions point that represents dimensions
     */
    public void setDimensions(Point dimensions) {
        // Setting to given dimensions only if both coordinates of given point are positive
        if (dimensions.getX() >= 0 && dimensions.getY() >= 0) {
            this.dimensions = dimensions;
        }
    }

    /**
     * Sets minimum position values for ball movement, and assures positive dimensions.
     * @param indexZero point that represents dimensions
     */
    public void setIndexZero(Point indexZero) {
        // Setting to given indexZero only if both coordinates of given point are positive
        if (indexZero.getX() >= 0 && indexZero.getY() >= 0) {
            this.indexZero = indexZero;
        }
    }

    /**
     * Moves ball one step with its velocity.
     * If ball movement is scoped within dimensions, changes velocity accordingly (if necessary).
     */
    public void moveOneStep() {
        // Matches ball dimensions only if those exist
        if (this.dimensions != null) {
            if (this.indexZero == null) {
                this.velocity.matchDimensions(this.center, this.radius, this.dimensions);
            } else {
                this.velocity.matchDimensions(this.center, this.radius, this.dimensions, this.indexZero);
            }
        }
        this.center = this.velocity.applyToPoint(this.center);
    }
}
