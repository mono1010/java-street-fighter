package ch.teko.game.model;

import java.awt.Color;
import java.awt.Graphics;

public class Floor {
    private int height;

    private static Floor instance;

    public Floor() {
        
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void onRender(Graphics g) {
        g.setColor(Color.RED);
        g.drawLine(0, this.height, 100000, this.height);
    }

    public static Floor getInstance() {
        if (instance == null) {
            instance = new Floor();
        }
        return instance;
    }
}
