package game;

import collisions.HitListener;
import objects.Ball;
import objects.Block;

/**
 * Tracks game score.
 */
public class ScoreTrackingListener implements HitListener {
    private Counter scoreCounter;

    /**
     * Constructs a score tracker with given counter.
     * @param scoreCounter score counter
     */
    public ScoreTrackingListener(Counter scoreCounter) {
        this.scoreCounter = scoreCounter;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        this.scoreCounter.increase(5);
    }
}
