import biuoop.DrawSurface;

import java.awt.Color;

public class Ball {
    private Point center;
    private int radius;
    private Color color;


    public Ball(Point center, int r, Color color) {
        this.center = center;
        this.radius = r;
        this.color = color;
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
}
