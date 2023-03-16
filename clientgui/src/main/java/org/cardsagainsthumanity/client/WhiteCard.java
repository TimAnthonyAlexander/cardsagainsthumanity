package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class WhiteCard extends JPanel {

    private String text;
    private int index;
    public WhiteCard(String content, int index){
        this.text = content;
        this.index = index;
        this.setBackground(Color.WHITE);
    }

    public String getPassedText() {
        return text;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int width = ((CardArea)getParent()).getDrawWidthSingleComponent();
        int drawabletextwidth = (int)(width*0.9);

        int height = 200;
        int x = 0;
        int y = 0;

        g.setColor(Color.WHITE);
        g.fillRect(x,y,width,height);
        g.setColor(Color.BLACK);
        fillString(text, g, drawabletextwidth,x+5,y+5);
    }

    public void fillString(String s, Graphics g, int drawablewidth, int x, int y){
        if (g.getFontMetrics().stringWidth(s) <= drawablewidth) {
            // If it fits, simply draw the string as-is
            g.drawString(s, x, y);
        } else {
            // Otherwise, we need to break up the string and add the portion that goes out of bounds below the visible string
            String visibleString = "";
            String hiddenString = s;
            int ellipsisWidth = g.getFontMetrics().stringWidth("..."); // Get the width of the "..." ellipsis

            // Keep removing characters from the end of the string until it fits within the maxWidth
            while (g.getFontMetrics().stringWidth(visibleString + "...") < drawablewidth && hiddenString.length() > 0) {
                visibleString = hiddenString.substring(0, hiddenString.length() - 1);
                hiddenString = hiddenString.substring(0, hiddenString.length() - 1);
            }

            // Draw the visible string with an ellipsis at the end
            g.drawString(visibleString + "...", x, y);

            // If there is any hidden string left, draw it below the visible string with an ellipsis at the beginning
            if (hiddenString.length() > 0) {
                g.drawString("..." + hiddenString, x, y + g.getFontMetrics().getHeight());
            }
        }
    }

    public int getIndex(){
        return this.index;
    }

    public void setPassedText(String text) {
        this.text = text;
    }
}
