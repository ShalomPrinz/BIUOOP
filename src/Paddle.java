import biuoop.KeyboardSensor;

/**
 * Represents a moveable paddle object.
 */
public class Paddle extends Block {
    private final KeyboardSensor keyboard;
    public static final int MOVEMENT_STEPS = 5;

    /**
     * Constructor for paddle.
     * @param origin top left corner of rectangle
     * @param width  rectangle width
     * @param height rectangle height
     */
    public Paddle(KeyboardSensor keyboard, Point origin, double width, double height) {
        super(origin, width, height);
        this.keyboard = keyboard;
    }

    /**
     * Moves the paddle to the left.
     */
    public void moveLeft() {
        Point origin = this.getOrigin();
        this.setOrigin(new Point(origin.getX() - MOVEMENT_STEPS, origin.getY()));
    }

    /**
     * Moves the paddle to the right.
     */
    public void moveRight() {
        Point origin = this.getOrigin();
        this.setOrigin(new Point(origin.getX() + MOVEMENT_STEPS, origin.getY()));
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