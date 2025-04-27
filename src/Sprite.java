import biuoop.DrawSurface;

/**
 * Represents a game object.
 */
public interface Sprite {
    /**
     * Draw the sprite to the screen.
     * @param d draw surface
     */
    void drawOn(DrawSurface d);

    /**
     * Notify the sprite that time has passed.
     */
    void timePassed();

    /**
     * Add object to game.
     * @param game game object
     */
    void addToGame(Game game);
}