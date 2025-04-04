import biuoop.DrawSurface;

import java.awt.Color;

public class Ball {
    private Point center;
    private int radius;
    private Color color;
    private Velocity velocity;
    private Point dimensions;

    public Ball(Point center, int r, Color color) {
        this.center = center;
        this.radius = r;
        this.color = color;
        this.velocity = new Velocity(0, 0);
    }

    public int getX() {
        return (int) this.center.getX();
    }

    public int getY() {
        return (int) this.center.getY();
    }

    public int getSize() {
        return (int) Math.PI * this.radius * this.radius;
    }

    public Color getColor() {
        return this.color;
    }

    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle(this.getX(), this.getY(), this.radius);
    }

    public void setVelocity(Velocity v) {
        this.velocity = v;
    }

    public void setVelocity(double dx, double dy) {
        this.velocity = new Velocity(dx, dy);
    }

    public Velocity getVelocity() {
        return this.velocity;
    }

    public void setDimensions(int width, int height) {
        if (width > 0 && height > 0) {
            this.dimensions = new Point(width, height);
        }
    }

    public void moveOneStep() {
        if (this.dimensions != null) {
            this.velocity.matchDimensions(this.center, this.dimensions);
        }
        this.center = this.velocity.applyToPoint(this.center);
    }
}
