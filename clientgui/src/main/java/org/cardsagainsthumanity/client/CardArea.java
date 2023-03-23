package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardArea extends JPanel {

    private int padding_w = 5;
    private WhiteCard[] wcarr;

    private Runner runner;

    public CardArea(Runner r){
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.runner = r;
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }
    public void setWc(WhiteCard[] wc, String type){
        this.removeAll();
        wcarr = wc;
        addCards(wc, type);
    }

    public void setBC(BlackCard bc) {
        this.removeAll();
        addBlackCard(bc);
    }

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

    private void addBlackCard(BlackCard pbc){
        this.add(pbc);
    }

    private void addCards(WhiteCard[] wcArr, String type){
        for(WhiteCard wc : wcArr){
            wc.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (type.equals("putCards")) {
                        runner.putCard(wc.getIndex());
                    }else {
                        runner.playCard(wc.getIndex());
                    }
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    wc.setBackground(new Color(0x9A9794));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    wc.setBackground(Color.WHITE);
                }
            });
            this.add(wc);
            this.add(Box.createRigidArea(new Dimension(10, 0)));
        }
    }

    private void updateCards(){

    }
}
