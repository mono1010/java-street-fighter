package ch.teko.game.model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;

import ch.teko.game.model.*;

public class Animate {
    private int animationIndex;

    private int spriteHeight;
    private int spriteWidth;
    private int spriteColumn;

    private Asset currentAsset;

    private boolean flipAsset;

    public Animate() {
    }

    private long lastTime, timer;
    final int rate = 60;

    public boolean onTick() {
        boolean reachedEnd = false;
        timer += System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        if (timer > rate) {
            this.animationIndex++;
            if (this.animationIndex == this.spriteColumn) {
                reachedEnd = true;
                this.animationIndex = 0;
            }
            
            timer = 0;
        }

        return reachedEnd;
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
        this.animationIndex = 0;
        this.spriteColumn = asset.metadata.column;
        this.spriteHeight = this.currentAsset.image.getHeight() / 1;
        this.spriteWidth = this.currentAsset.image.getWidth() / this.spriteColumn;
        this.timer = 0;
    }

    public BufferedImage get() {
        return rotate(this.currentAsset.image.getSubimage(this.animationIndex * this.spriteWidth, 0, this.spriteWidth, this.spriteHeight));
    }

    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }
    
    public BufferedImage rotate(BufferedImage image) {
        if (!this.flipAsset)
            return image;

        BufferedImage copy = copyImage(image);
        flipAsset(copy);
        return copy;
    }

    void flipAsset() {
        this.flipAsset = !this.flipAsset;
    }

    boolean getFlipAsset() {
        return this.flipAsset;
    }

    public static void flipAsset(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
    
        for (int i = 0; i < width / 2; i++) {
            for (int j = 0; j < height; j++) {
                int tmp = image.getRGB(i, j);
                image.setRGB(i, j, image.getRGB(width - i - 1, j));
                image.setRGB(width - i - 1, j, tmp);
            }
        }
    }
    
}
