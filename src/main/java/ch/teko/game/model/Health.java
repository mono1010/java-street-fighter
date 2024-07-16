package ch.teko.game.model;

import java.awt.Color;
import java.awt.Graphics;

public class Health {

    private static Health instance;
    
    private float player1HealthPercent = 1;
    private float player2HealthPercent = 1;

    public Health() {

    }

    public void setHealth(float p1Health, float p1MaxHealth, float p2Health, float p2MaxHealth) {
        this.player1HealthPercent = (p1Health / p1MaxHealth);
        this.player2HealthPercent = (p2Health / p2MaxHealth);
    }

    public void onRender(Graphics g) {
        final int middle = Map.getInstance().getWidth() / 2;
        final int offset = 150;
        final int separationOffset = 1;

        final int y = 20;
        final int height = 18;

        // rectangle around the entire health bar
        g.setColor(Color.BLACK);
        g.drawRect((middle - separationOffset) - offset - 1, y - 1, ((offset + separationOffset) * 2) + 2, height + 2);

        int player1Offset =  Math.max(0, offset - (int)(offset * this.player1HealthPercent));
        
        // left player 1 health
        g.setColor(Color.YELLOW);
        g.fillRect((middle - separationOffset) - offset + player1Offset, y, offset - player1Offset, height);

        // lost player 1 health
        g.setColor(Color.RED);
        g.fillRect(middle - separationOffset - offset, y, player1Offset, height);

        int player2Offset =  Math.max(0, offset - (int)(offset * this.player2HealthPercent));

        // left player 2 health
        g.setColor(Color.YELLOW);
        g.fillRect(middle + separationOffset, y, offset - player2Offset, height);

        // lost player 2 health
        g.setColor(Color.RED);
        g.fillRect(middle + separationOffset + offset - player2Offset, y, player2Offset, height);

        // border between the two health bars
        g.setColor(Color.BLACK);
        g.fillRect(middle - separationOffset, y - 1, separationOffset * 2, height + 2);
    }

    public static Health getInstance() {
        if (instance == null) {
            instance = new Health();
        }
        return instance;
    }
}
