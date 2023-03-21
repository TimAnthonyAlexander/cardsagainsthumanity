package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class CardArea extends JPanel {

    private int padding_w = 5;
    private WhiteCard[] wcarr;
    private BlackCard bc;

    public CardArea(){
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }
    public void setWcArr(WhiteCard[] wc){
        wcarr = wc;
    }

    public void setBC(BlackCard pbc) { bc = pbc; }

    public int getDrawWidthSingleComponent(String type){
        int width = 0;
        switch(type){
            case "BlackCard":
                width = (int)(this.getSize().getWidth()-padding_w);
                return width;
            case "WhiteCard":
                int count = 0;
                for(WhiteCard wc : wcarr){
                    if(wc != null){
                        count++;
                    }
                }
                int entirepadding_w = padding_w*count;
                width = (int)(this.getSize().getWidth()-entirepadding_w)/count;
                return width;
            default:
                return 0;
        }

    }

    public void addBlackCard(){
        this.add(bc);
    }

    public void addCards(){
        for(WhiteCard wc : wcarr){
            this.add(wc);
            this.add(Box.createRigidArea(new Dimension(10, 0)));
        }
    }
}
