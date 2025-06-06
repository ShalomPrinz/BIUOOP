package game;

import collisions.HitListener;
import objects.Ball;
import objects.Block;

/**
 * Removes balls from the game and keeps count of remaining balls.
 */
public class BallRemover implements HitListener {
    private Game game;
    private Counter remainingBalls;

    /**
     * Constructs a ball remover object.
     * @param game ball remover game
     * @param remainingBalls ball remover counter
     */
    public BallRemover(Game game, Counter remainingBalls) {
        this.game = game;
        this.remainingBalls = remainingBalls;
    }

    @Override
    public void hitEvent(Block beingHit, Ball hitter) {
        hitter.removeFromGame(this.game);
        this.remainingBalls.decrease(1);
    }
}
