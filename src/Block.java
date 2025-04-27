import biuoop.DrawSurface;

/**
 * Represents a block in the game.
 */
public class Block extends Rectangle implements Sprite {

    /**
     * Constructor for block.
     * @param origin top left corner of rectangle
     * @param width  rectangle width
     * @param height rectangle height
     */
    public Block(Point origin, double width, double height) {
        super(origin, width, height);
    }

    @Override
    public void drawOn(DrawSurface surface) {
        Point origin = this.getOrigin();
        surface.fillRectangle(
                (int) origin.getX(), (int) origin.getY(), (int) this.getWidth(), (int) this.getHeight());
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