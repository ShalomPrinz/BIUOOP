package objects;

import game.Game;
import game.Sprite;
import geometry.Point;
import geometry.Rectangle;

import biuoop.DrawSurface;

import java.awt.Color;

/**
 * Represents a block in the game.
 */
public class Block extends Rectangle implements Sprite {
    private Color color;

    /**
     * Constructor for block.
     * @param origin top left corner of rectangle
     * @param width  rectangle width
     * @param height rectangle height
     */
    public Block(Point origin, double width, double height) {
        super(origin, width, height);
        this.color = Color.BLACK;
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
    public void setColor(Color color) {
        if (color != null) {
            this.color = color;
        }
    }
}