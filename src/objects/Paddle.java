package objects;

import biuoop.DrawSurface;
import geometry.Point;
import geometry.Velocity;

import biuoop.KeyboardSensor;

import java.awt.Color;
import java.awt.Polygon;

/**
 * Represents a moveable paddle object.
 */
public class Paddle extends Block {
    private final KeyboardSensor keyboard;
    public static final int MOVEMENT_STEPS = 10;
    private int maxWidth;
    private int minWidth;
    private double rotationAngle = 0;
    private Point centerPoint;

    /**
     * Constructor for paddle.
     * @param keyboard keyboard sensor for moving paddle
     * @param origin top left corner of rectangle
     * @param width  rectangle width
     * @param height rectangle height
     */
    public Paddle(KeyboardSensor keyboard, Point origin, double width, double height) {
        super(origin, width, height);
        this.keyboard = keyboard;
        this.minWidth = -1;
        this.maxWidth = -1;
    }

    /**
     * Sets x-axis bounds for paddle.
     * @param min x-axis most left value of paddle
     * @param max x-axis most right value of paddle
     */
    public void setXBounds(int min, int max) {
        this.minWidth = min;
        this.maxWidth = max;
    }

    /**
     *
     * @return whether paddle has been given an X bound.
     */
    private boolean hasXBounds() {
        return this.maxWidth != -1 && this.minWidth != -1;
    }

    /**
     * Moves the paddle to the left.
     */
    public void moveLeft() {
        Point origin = this.getOrigin();
        if (hasXBounds() && origin.getX() - MOVEMENT_STEPS < this.minWidth) {
            this.setOrigin(new Point(this.maxWidth - this.getWidth(), origin.getY()));
        } else {
            this.setOrigin(new Point(origin.getX() - MOVEMENT_STEPS, origin.getY()));
        }
    }

    /**
     * Moves the paddle to the right.
     */
    public void moveRight() {
        Point origin = this.getOrigin();
        if (hasXBounds() && origin.getX() + this.getWidth() + MOVEMENT_STEPS >= this.maxWidth) {
            this.setOrigin(new Point(this.minWidth, origin.getY()));
        } else {
            this.setOrigin(new Point(origin.getX() + MOVEMENT_STEPS, origin.getY()));
        }
    }

    /**
     * Updates center point of paddle according to origin, width and height.
     */
    private void updateCenterPoint() {
        double centerX = this.getOrigin().getX() + this.getWidth() / 2;
        double centerY = this.getOrigin().getY() + this.getHeight() / 2;
        this.centerPoint = new Point(centerX, centerY);
    }

    /**
     * Rotates paddle and moves it up toward screen top border.
     */
    public void rotateUpward() {
        if (this.centerPoint == null) {
            updateCenterPoint();
        }

        this.rotationAngle += 6;
        if (this.rotationAngle >= 360) {
            this.rotationAngle -= 360;
        }

        int movement = this.centerPoint.getX() < 200 ? 0 : -2;
        double newOriginX = this.centerPoint.getX() - this.getWidth() / 2 + movement;
        double newOriginY = this.centerPoint.getY() - this.getHeight() / 2 - 2;

        this.setOrigin(new Point(newOriginX, newOriginY));
        updateCenterPoint();
    }

    @Override
    public void timePassed() {
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        } else if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }

    @Override
    public void drawOn(DrawSurface surface) {
        // Not rotating yet
        if (this.centerPoint == null) {
            super.drawOn(surface);
            return;
        }

        // Draw rotation by calculating new corners
        Point[] corners = getRotatedCorners();
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];
        for (int i = 0; i < 4; i++) {
            xPoints[i] = (int) corners[i].getX();
            yPoints[i] = (int) corners[i].getY();
        }

        // Paddle body
        surface.setColor(Color.ORANGE);
        surface.fillPolygon(new Polygon(xPoints, yPoints, 4));

        // Paddle borders
        surface.setColor(Color.BLACK);
        surface.drawPolygon(new Polygon(xPoints, yPoints, 4));
    }

    /**
     * Calculate the four corners of the rotated rectangle.
     * @return rotated corners
     */
    private Point[] getRotatedCorners() {
        if (this.centerPoint == null) {
            updateCenterPoint();
        }

        // Calculations
        double radians = Math.toRadians(this.rotationAngle);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double halfWidth = this.getWidth() / 2;
        double halfHeight = this.getHeight() / 2;
        double[] relativeX = {-halfWidth, halfWidth, halfWidth, -halfWidth};
        double[] relativeY = {-halfHeight, -halfHeight, halfHeight, halfHeight};

        // Rotate each corner around center
        Point[] corners = new Point[4];
        for (int i = 0; i < 4; i++) {
            double rotatedX = relativeX[i] * cos - relativeY[i] * sin;
            double rotatedY = relativeX[i] * sin + relativeY[i] * cos;

            corners[i] = new Point(
                    this.centerPoint.getX() + rotatedX,
                    this.centerPoint.getY() + rotatedY
            );
        }

        return corners;
    }

    /**
     * Calculates paddle region number of hit based on collision point.
     * @param cp collision point
     * @return region number between 1-5, or 0 if hit isn't within paddle regions
     */
    private int calculateHitRegion(Point cp) {
        double relativeHit = cp.getX() - this.getOrigin().getX();
        if (relativeHit < 0 || relativeHit > this.getWidth() || cp.getY() > this.getOrigin().getY()) {
            return 0; // Hit outside paddle regions
        }
        double regionWidth = this.getWidth() / 5;
        return Math.min(1 + (int) (relativeHit / regionWidth), 5);
    }

    @Override
    public Velocity hit(Ball hitter, Point cp, Velocity velocity) {
        double angle;
        switch (calculateHitRegion(cp)) {
            case 1:
                angle = 300;
                break;
            case 2:
                angle = 330;
                break;
            case 4:
                angle = 30;
                break;
            case 5:
                angle = 60;
                break;
            default: // Including valid region 3
                return super.hit(hitter, cp, velocity);
        }
        return velocity.accelerate(angle, 0);
    }
}
