package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class CardArea extends JPanel {

    private int padding_w = 5;
    private WhiteCard[] wcarr;
    public CardArea(){
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
    }

    public void setWcArr(WhiteCard[] wc){
        wcarr = wc;
    }

    public int getDrawWidthSingleComponent(){
        int width = 0;
        int count = 0;
        for(WhiteCard wc : wcarr){
            if(wc != null){
                count++;
            }
        }
        int entirepadding_w = padding_w*count;
        width = (this.getWidth()-entirepadding_w)/count;
        return width;
    }

    public void addCards(){
        for(WhiteCard wc : wcarr){
            this.add(wc);
        }
    }
}
