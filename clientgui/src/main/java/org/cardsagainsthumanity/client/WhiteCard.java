package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class WhiteCard extends JPanel {
    private final String text;
    private String drawableText;
    private final int index;

    private Color color;

    public WhiteCard(String content, int index){
        this.text = content;
        this.drawableText = this.text;
        this.index = index;
        this.color = Color.WHITE;
        this.setBackground(color);
    }

    public void setColor(Color c){
        color = c;
        this.setBackground(color);
        this.repaint();
    }

    public void setVisibility(boolean visible){
        if(visible){
            drawableText = text;
        }else{
            drawableText = "";
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = ((CardArea)getParent()).getDrawWidthSingleComponent("WhiteCard");
        int drawableTextWidth = (int)(width*0.9);
        int height = 200;
        int x = 0;
        int y = 0;

        g.setColor(color);
        g.fillRect(x,y,width,height);
        g.setColor(Color.BLACK);
        fillString(drawableText, g, drawableTextWidth,x+((int)(width*0.1)/2),y+15);
    }

    public void fillString(String s, Graphics g, int drawableWidth, int x, int y){
        if (g.getFontMetrics().stringWidth(s) <= drawableWidth) {
            g.drawString(s, x, y);
        } else {
            String[] split = s.split(" ");
            String line = "";
            int lineWidth = 0;
            for (String word : split){
                int wordWidth = g.getFontMetrics().stringWidth(word);
                if(lineWidth + wordWidth > drawableWidth){
                    g.drawString(line, x, y);
                    y += g.getFontMetrics().getHeight();
                    line = "";
                }
                line += word + " ";
                lineWidth = g.getFontMetrics().stringWidth(line);
            }
            g.drawString(line, x, y);
        }
    }

    public int getIndex(){
        return this.index;
    }
}
