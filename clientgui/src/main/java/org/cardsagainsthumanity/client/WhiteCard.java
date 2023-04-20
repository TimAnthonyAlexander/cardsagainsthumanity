package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class WhiteCard extends JPanel {
    private final String text;
    private String drawableText;
    private final int index;

    private Dimension referenceSize;
    private Color color;

    public WhiteCard(String content, int index){
        this.text = content;
        this.drawableText = this.text;
        this.index = index;
        this.color = Color.WHITE;
    }

    public void setColor(Color c){
        color = c;
        //this.setBackground(color);
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

        Dimension size = this.getSize();

        int width = (int) size.getWidth();
        int height = width * 8 / 5;

        size.height = height;

        if(referenceSize == null){
            referenceSize = size;
        }

        int drawableTextWidth = (int)(width*0.9);

        double scalingFactor = Math.min(size.getHeight() / referenceSize.getHeight(), size.getWidth() /referenceSize.getWidth());
        int fontSize = (int) (24*scalingFactor);

        int x = 0;
        int y = 0;

        Font font = g.getFont();
        g.setFont(font.deriveFont((float) fontSize));
        g.setColor(color);
        g.fillRect(x,y,width,height);
        g.setColor(Color.BLACK);
        fillString(drawableText, g, drawableTextWidth,x+((int)(width*0.1)/2),y+fontSize);
    }

    public void fillString(String s, Graphics g, int drawableWidth, int x, int y){
        FontMetrics metrics = g.getFontMetrics();
        if (metrics.stringWidth(s) <= drawableWidth) {
            g.drawString(s, x, y);
        } else {
            String[] split = s.split(" ");
            String line = "";
            int lineWidth = 0;
            for (String word : split){
                int wordWidth = metrics.stringWidth(word);
                if(lineWidth + wordWidth > drawableWidth){
                    g.drawString(line, x, y);
                    y += metrics.getHeight();
                    line = "";
                }
                line += word + " ";
                lineWidth = metrics.stringWidth(line);
            }
            g.drawString(line, x, y);
        }
    }

    public int getIndex(){
        return this.index;
    }
}
