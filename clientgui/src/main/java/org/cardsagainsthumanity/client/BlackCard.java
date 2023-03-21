package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class BlackCard extends JPanel{

    private String text;
    public BlackCard(String content){
        text = content;
    }

    public String getText(){
        return text;
    }

    public void setText(String s){
        text = s;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = ((CardArea)getParent()).getDrawWidthSingleComponent("BlackCard");
        int drawableTextWidth = (int)(width*0.9);

        int height = 200;
        int x = 0;
        int y = 0;

        g.setColor(Color.BLACK);
        g.fillRect(x,y,width,height);
        g.setColor(Color.WHITE);
        fillString(text, g, drawableTextWidth,x+((int)(width*0.1)/2),y+15);
    }

    public void fillString(String s, Graphics g, int drawableWidth, int x, int y){
        if (g.getFontMetrics().stringWidth(s) <= drawableWidth) {
            g.drawString(s, x, y);
        } else {
            String[] splitted = s.split(" ");
            String line = "";
            int lineWidth = 0;
            for (String word : splitted){
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

}
