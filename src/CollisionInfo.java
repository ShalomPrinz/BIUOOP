/**
 * Represents a collision information.
 */
public class CollisionInfo {
    private final Point point;
    private final Rectangle rectangle;
    private final CollisionEdge edge;

    /**
     * Constructor of collision info.
     *
     * @param cr collision object
     * @param cp collision point
     * @param ce collision edge
     */
    public CollisionInfo(Rectangle cr, Point cp, CollisionEdge ce) {
        this.point = cp;
        this.rectangle = cr;
        this.edge = ce;
    }

    /**
     *
     * @return collision point
     */
    public Point getPoint() {
        return this.point;
    }

    /**
     *
     * @return collision object
     */
    public Collidable getObject() {
        return this.rectangle;
    }

    /**
     *
     * @return collision edge
     */
    public CollisionEdge getEdge() {
        return this.edge;
    }
}