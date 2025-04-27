/**
 * Represents a shape's collision edge.
 */
public enum CollisionEdge {
    TOP, RIGHT, BOTTOM, LEFT, CORNER;

    /**
     *
     * @param index enum value's index
     * @return enum value at given index
     */
    public static CollisionEdge get(int index) {
        CollisionEdge[] values = values();
        if (index < 0 || index >= values.length) {
            return null;
        }
        return values[index];
    }

    /**
     *
     * @param edge collidable collision edge
     * @return whether edge is horizontal (left or right)
     */
    public static boolean isHorizontal(CollisionEdge edge) {
        return edge == LEFT || edge == RIGHT;
    }
}
