package game;

import biuoop.DrawSurface;
import objects.Block;

import java.awt.Color;

/**
 * Displays the game score.
 */
public class ScoreIndicator extends Block implements Sprite {
    private Counter scoreCounter;

    /**
     * Constructs a score indicator.
     * @param block where indicator stands
     * @param scoreCounter tracks game score
     */
    public ScoreIndicator(Block block, Counter scoreCounter) {
        super(block.getOrigin(), block.getWidth(), block.getHeight());
        this.scoreCounter = scoreCounter;
        this.setPassiveColor();
        this.setNoBorders();
    }

    @Override
    public void drawOn(DrawSurface d) {
        this.setColor(Color.WHITE);
        super.drawOn(d);

        String text = "Score: " + this.scoreCounter.getValue();
        int startX = ((int) this.getWidth() / 2) - text.length() * 3;
        d.setColor(Color.BLACK);
        d.drawText(startX, 15, text, 15);
    }

    @Override
    public void addToGame(Game game) {
        game.addSprite(this);
    }

    @Override
    public void removeFromGame(Game game) {
        game.removeSprite(this);
    }
}
