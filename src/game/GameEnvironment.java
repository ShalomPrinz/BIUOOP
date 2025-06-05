package game;

import collisions.Collidable;
import collisions.CollisionEdge;
import collisions.CollisionInfo;
import geometry.Line;
import geometry.Point;
import geometry.Rectangle;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game environment.
 */
public class GameEnvironment {
    private final List<Collidable> collidables;
    public static final double COLLISION_THRESHOLD = 0.1;

    /**
     * Constructor of game environment.
     */
    public GameEnvironment() {
        this.collidables = new ArrayList<>();
    }

    /**
     * Add given collidable to environment.
     * @param c collidable to be added
     */
    public void addCollidable(Collidable c) {
        collidables.add(c);
    }

    /**
     * Removes given collidable from environment.
     * @param c collidable to be removed
     */
    public void removeCollidable(Collidable c) {
        collidables.remove(c);
    }

    /**
     * Returns information about the closest collision.
     * Assumes object moves from movement start to movement end.
     * @param movement line which describes object movement
     * @return collision info if there is a collision, else null
     */
    public CollisionInfo getClosestCollision(Line movement) {
        for (int i = 0; i < collidables.size(); i++) {
            Rectangle rectangle = collidables.get(i).getCollisionRectangle();
            Point intersection = movement.closestIntersectionToStartOfLine(rectangle);
            if (intersection != null) {
                CollisionEdge edge = rectangle.getCollisionEdge(intersection);
                return new CollisionInfo(rectangle, intersection, edge);
            }
        }

        // No collisions found
        return null;
    }
}