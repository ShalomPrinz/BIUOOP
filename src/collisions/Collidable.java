package collisions;

import geometry.Point;
import geometry.Rectangle;
import geometry.Velocity;
import objects.Ball;

/**
 * Represents a collidable object.
 */
public interface Collidable {
    /**
     *
     * @return collision rectangle
     */
    Rectangle getCollisionRectangle();

    /**
     * Notify a collidable that a collision occurred.
     * @param hitter hitter object
     * @param cp collision point
     * @param velocity colliding object velocity
     * @return colliding object's new velocity
     */
    Velocity hit(Ball hitter, Point cp, Velocity velocity);

    /**
     * Determine collision edge.
     * @param cp collision point
     * @return collision edge
     */
    CollisionEdge getCollisionEdge(Point cp);
}
