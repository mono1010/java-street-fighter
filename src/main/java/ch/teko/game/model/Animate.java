package ch.teko.game.model;

import java.awt.image.BufferedImage;

import ch.teko.game.model.*;

public class Animate {
    private int animationIndex;

    private int spriteHeight;
    private int spriteWidth;
    private int spriteColumn;

    private Asset currentAsset;

    public Animate() {
    }

    private long lastTime, timer;
    final int rate = 60;

    public void onTick() {
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        if (timer > rate) {
            this.animationIndex++;
            if (this.animationIndex == this.spriteColumn)
                this.animationIndex = 0;
            
            timer = 0;
        }
    }

    public int getAnimationIndex() {
        return this.animationIndex;
    }

    public int getAnimationMaxIndex() {
        return this.spriteColumn;
    }

    public Asset getCurrentAsset() {
        return this.currentAsset;
    }

    public boolean isAnimationRunning(Asset asset) {
        if (this.currentAsset == null)
            return false;
        
        return this.currentAsset.metadata.name.equals(asset.metadata.name);
    }

    public void animate(Asset asset) {
        if (isAnimationRunning(asset))
            return;
        
        this.currentAsset = asset;
        this.animationIndex = 1;
        this.spriteColumn = asset.metadata.column;
        this.spriteHeight = this.currentAsset.image.getHeight() / 1;
        this.spriteWidth = this.currentAsset.image.getWidth() / this.spriteColumn;
    }

    public BufferedImage get() {
        return this.currentAsset.image.getSubimage(this.animationIndex * this.spriteWidth, 0, this.spriteWidth, this.spriteHeight);
    }
}
