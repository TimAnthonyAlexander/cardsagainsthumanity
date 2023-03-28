package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel {
    private String[] players;
    public PlayerPanel(String [] players){
        this.players = players;
    }

    public void updatePlayers(String[] players){
        this.players = players;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = 200;
        int height = 200;
        int x = 0;
        int y = 0;

        g.setColor(Color.WHITE);
        g.fillRect(x,y,width,height);
        int fmh = g.getFontMetrics().getHeight();
        for(String p : players){
            g.setColor(Color.GREEN);
            g.fillRect(x, y, fmh, fmh);
            g.setColor(Color.BLACK);
            y += fmh;
            g.drawString(p, x + fmh + 5, y);
        }
    }
}
