package objects;

import collisions.CollisionEdge;
import collisions.CollisionInfo;
import game.Game;
import game.GameEnvironment;
import game.Sprite;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;

import biuoop.DrawSurface;

import java.awt.Color;

/**
 * Represents a moving circle.
 */
public class Ball implements Sprite {
    private Point center;
    private int radius;
    private Color color;
    private Velocity velocity;
    private GameEnvironment environment;
    // Saving paddle in order to validate no unexpected collision happens
    private Rectangle paddle;

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
        this.paddle = null;
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
     * Sets the game paddle.
     * @param paddle game paddle
     */
    public void setPaddle(Rectangle paddle) {
        this.paddle = paddle;
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

    @Override
    public void setColor(Color color) {
        if (color != null) {
            this.color = color;
        }
    }

    @Override
    public void drawOn(DrawSurface surface) {
        surface.setColor(this.color);
        surface.fillCircle(this.getX(), this.getY(), this.radius);
        surface.setColor(Color.BLACK);
        surface.drawCircle(this.getX(), this.getY(), this.radius);
    }

    @Override
    public void timePassed() {
        this.moveOneStep();
    }

    @Override
    public void addToGame(Game game) {
        game.addSprite(this);
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
     * Checks whether paddle is colliding ball, and handles it if colliding.
     * @return whether movement was handled by this method
     * @implNote Through the process there are some safety guards to validate scenario:
     * 1. Ball must be inside paddle borders
     * 2. There is an achievable collision point
     * 3. Collision edge on paddle is horizontal
     * If one of those guards is false, ball is not colliding paddle.
     */
    private boolean collidingPaddle() {
        // Validate ball is inside paddle borders
        if (!this.paddle.isBallInside(this.center, this.radius)) {
            return false;
        }

        // Get collision point on paddle borders by ball diameter on x-axis
        Line diameter = new Line(this.center.getX() - radius, this.center.getY(),
                this.center.getX() + radius, this.center.getY());
        Point cp = diameter.closestIntersectionToStartOfLine(this.paddle);
        // Validate there is a collision
        if (cp == null) {
            return false;
        }

        CollisionEdge edge = this.paddle.getCollisionEdge(cp);
        double escapeAngle = 15;
        double escapeSpeed = 10;
        if (edge == CollisionEdge.LEFT) {
            escapeAngle = 360 - escapeAngle;
        } else if (edge != CollisionEdge.RIGHT) {
            // Collision edge is not horizontal
            this.center = this.velocity.applyToPoint(this.center);
            return false;
        }

        // Escape collision by accelerating out of paddle
        this.velocity = this.velocity.accelerate(0, escapeSpeed);
        this.center = this.velocity.applyToPoint(this.center);
        this.velocity = this.velocity.accelerate(escapeAngle, -escapeSpeed);
        return true;
    }

    /**
     * Calculates next ball movement collision info.
     * @param dest ball movement destination
     * @return collision info or null if there is no collision
     */
    private CollisionInfo getClosestCollision(Point dest) {
        Line closestMovement = new Line(this.center, dest);
        return this.environment.getClosestCollision(closestMovement);
    }

    /**
     * Calculates a reasonable close point to a collision point.
     * @param info information about collision point and edge
     * @return closest point to collision
     */
    private Point getCollisionClosePoint(CollisionInfo info) {
        double newX = info.getPoint().getX(), newY = info.getPoint().getY();
        double threshold = GameEnvironment.COLLISION_THRESHOLD;
        if (CollisionEdge.isHorizontal(info.getEdge())) {
            newX += this.velocity.isRight() ? -threshold : threshold;
        } else {
            newY += this.velocity.isBottom() ? -threshold : threshold;
        }
        return new Point(newX, newY);
    }

    /**
     * Moves ball one step with its velocity.
     */
    public void moveOneStep() {
        // Handle paddle movement that causes collision with this ball
        if (this.paddle != null && this.collidingPaddle()) {
            return;
        }

        Line movement = new Line(this.center, this.velocity.applyToPoint(this.center));
        CollisionInfo info = this.environment.getClosestCollision(movement);

        // If no collision, simply move
        if (info == null) {
            this.center = this.velocity.applyToPoint(this.center);
            return;
        }

        // Move ball close to collided object and make sure current dest doesn't collide again
        Point dest = getCollisionClosePoint(info);
        CollisionInfo newInfo = this.getClosestCollision(dest);
        // Assuming max of 2 close point
        if (newInfo != null) {
            dest = getCollisionClosePoint(newInfo);
            info = newInfo;
        }

        // Set ball's new center and velocity to match collision info
        this.center = dest;
        // Perform the object hit and change velocity accordingly
        this.velocity = info.getObject().hit(info.getPoint(), this.velocity);
    }
}
