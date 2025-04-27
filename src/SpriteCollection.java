import biuoop.DrawSurface;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a sprite collection.
 */
public class SpriteCollection {
    private final List<Sprite> sprites;

    /**
     * Constructs a sprite collection.
     */
    public SpriteCollection() {
        this.sprites = new ArrayList<>();
    }

    /**
     * Adds a sprite to the sprite collection.
     * @param s sprite object
     */
    public void addSprite(Sprite s) {
        this.sprites.add(s);
    }

    /**
     * Call timePassed on all sprites.
     */
    public void notifyAllTimePassed() {
        for (int i = 0; i < this.sprites.size(); i++) {
            this.sprites.get(i).timePassed();
        }
    }

    /**
     * Call drawOn on all sprites.
     * @param d draw surface
     */
    public void drawAllOn(DrawSurface d) {
        for (int i = 0; i < this.sprites.size(); i++) {
            this.sprites.get(i).drawOn(d);
        }
    }
}
