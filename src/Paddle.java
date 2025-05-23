import biuoop.KeyboardSensor;

/**
 * Represents a moveable paddle object.
 */
public class Paddle extends Block {
    private final KeyboardSensor keyboard;
    public static final int MOVEMENT_STEPS = 5;
    private int maxWidth;
    private int minWidth;

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

    @Override
    public void timePassed() {
        if (this.keyboard.isPressed(KeyboardSensor.LEFT_KEY)) {
            moveLeft();
        } else if (this.keyboard.isPressed(KeyboardSensor.RIGHT_KEY)) {
            moveRight();
        }
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
    public Velocity hit(Point cp, Velocity velocity) {
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
                return super.hit(cp, velocity);
        }
        return velocity.accelerate(angle, 0);
    }
}
