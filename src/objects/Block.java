package objects;

import collisions.HitListener;
import collisions.HitNotifier;
import game.Game;
import game.Sprite;
import geometry.Point;
import geometry.Rectangle;

import biuoop.DrawSurface;
import geometry.Velocity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a block in the game.
 */
public class Block extends Rectangle implements Sprite, HitNotifier {
    private Color color;
    private boolean passiveColor;
    private List<HitListener> hitListeners;

    /**
     * Constructor for block.
     * @param origin top left corner of rectangle
     * @param width  rectangle width
     * @param height rectangle height
     */
    public Block(Point origin, double width, double height) {
        super(origin, width, height);
        this.color = Color.BLACK;
        this.hitListeners = new ArrayList<>();
        this.passiveColor = false;
    }

    @Override
    public void drawOn(DrawSurface surface) {
        int x = (int) this.getOrigin().getX();
        int y = (int) this.getOrigin().getY();
        int width = (int) this.getWidth();
        int height = (int) this.getHeight();
        surface.setColor(this.color);
        surface.fillRectangle(x, y, width, height);
        surface.setColor(Color.BLACK);
        surface.drawRectangle(x, y, width, height);
    }

    @Override
    public void timePassed() {
    }

    @Override
    public void addToGame(Game game) {
        game.addSprite(this);
        game.addCollidable(this);
    }

    @Override
    public void removeFromGame(Game game) {
        game.removeSprite(this);
        game.removeCollidable(this);
    }

    @Override
    public void setColor(Color color) {
        if (color != null) {
            this.color = color;
        }
    }

    @Override
    public void addHitListener(HitListener hl) {
        this.hitListeners.add(hl);
    }

    @Override
    public void removeHitListener(HitListener hl) {
        this.hitListeners.remove(hl);
    }

    @Override
    public Velocity hit(Ball hitter, Point cp, Velocity velocity) {
        Velocity v = super.hit(hitter, cp, velocity);
        if (!ballColorMatch(hitter)) {
            this.notifyHit(hitter);
            if (!this.passiveColor) {
                hitter.setColor(this.color);
            }
        }
        return v;
    }

    /**
     * Sets this block to be color passive, meaning it won't change its hitter color.
     */
    public void setPassiveColor() {
        this.passiveColor = true;
    }

    /**
     * Notifies hitter that it hits this block.
     * @param hitter hitter object
     */
    private void notifyHit(Ball hitter) {
        List<HitListener> listeners = new ArrayList<>(this.hitListeners);

        for (HitListener hl : listeners) {
            hl.hitEvent(this, hitter);
        }
    }

    /**
     *
     * @param ball ball to compare color with
     * @return whether this block's color is same as given ball's color
     */
    public boolean ballColorMatch(Ball ball) {
        return ball.getColor().equals(this.color);
    }
}