package collisions;

import objects.Ball;
import objects.Block;

/**
 * Represents a hit listener.
 */
public interface HitListener {

    /**
     * Given hitter hits beingHit block.
     * @param beingHit block that's being hit
     * @param hitter hitter object
     */
    void hitEvent(Block beingHit, Ball hitter);
}
