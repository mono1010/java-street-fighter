package ch.teko.game.model;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Entity {
    protected int x, y, velocityX, velocityY;

    public Entity(int x, int y, int velocityX, int velocityY) {
        this.x = x;
        this.y = y;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public abstract void onTick();
    public abstract void onRender(Graphics g);

    public abstract Rectangle getAABB();
}
