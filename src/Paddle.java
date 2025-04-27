import biuoop.KeyboardSensor;

/**
 * Represents a moveable paddle object.
 */
public class Paddle extends Block {
    private final KeyboardSensor keyboard;
    public static final int MOVEMENT_STEPS = 5;
    private int maxWidth;

    /**
     * Constructor for paddle.
     * @param origin top left corner of rectangle
     * @param width  rectangle width
     * @param height rectangle height
     */
    public Paddle(KeyboardSensor keyboard, Point origin, double width, double height) {
        super(origin, width, height);
        this.keyboard = keyboard;
        this.maxWidth = -1;
    }

    /**
     * Sets given width to paddle max width.
     * @param width x-axis most right value of paddle
     */
    public void setXBound(int width) {
        this.maxWidth = width;
    }

    /**
     *
     * @return whether paddle has been given an X bound.
     */
    private boolean hasXBound() {
        return this.maxWidth != -1;
    }

    /**
     * Moves the paddle to the left.
     */
    public void moveLeft() {
        Point origin = this.getOrigin();
        if (hasXBound() && origin.getX() <= 0) {
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
        if (hasXBound() && origin.getX() + this.getWidth() >= this.maxWidth) {
            this.setOrigin(new Point(0, origin.getY()));
        } else {
            this.setOrigin(new Point(origin.getX() + MOVEMENT_STEPS, origin.getY()));
        }
    }

    @Override
    public void timePassed() {
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        } else if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
    }
}