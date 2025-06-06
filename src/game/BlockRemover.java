package game;

import collisions.HitListener;
import objects.Ball;
import objects.Block;

/**
 * Removes blocks from the game and keeps count of remaining blocks.
 */
public class BlockRemover implements HitListener {
    private Game game;
    private Counter remainingBlocks;

    /**
     * Constructs a block remover object.
     * @param game block remover game
     * @param remainingBlocks block remover counter
     */
    public BlockRemover(Game game, Counter remainingBlocks) {
        this.game = game;
        this.remainingBlocks = remainingBlocks;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        beingHit.removeFromGame(this.game);
        beingHit.removeHitListener(this);
        this.remainingBlocks.decrease(1);
    }
}
