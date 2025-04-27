import biuoop.DrawSurface;

/**
 * Represents a block in the game.
 */
public class Block implements Collidable, Sprite {
    private final Rectangle rect;

    /**
     * Constructor for block.
     * @param rect block rectangle
     */
    public Block(Rectangle rect) {
        this.rect = rect;
    }

    @Override
    public Rectangle getCollisionRectangle() {
        return this.rect;
    }

    @Override
    public Velocity hit(Point cp, Velocity velocity) {
        velocity.collide(cp, this);
        return velocity;
    }

    @Override
    public CollisionEdge getCollisionEdge(Point cp) {
        return this.rect.getCollisionEdge(cp);
    }

    @Override
    public void drawOn(DrawSurface surface) {
        Point origin = this.rect.getOrigin();
        surface.fillRectangle(
                (int) origin.getX(), (int) origin.getY(), (int) this.rect.getWidth(), (int) this.rect.getHeight());
    }

    @Override
    public void timePassed() {

    }

    @Override
    public void addToGame(Game game) {
        game.addSprite(this);
        game.addCollidable(this);
    }
}