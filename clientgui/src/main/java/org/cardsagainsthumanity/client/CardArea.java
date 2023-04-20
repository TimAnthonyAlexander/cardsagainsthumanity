package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CardArea extends JPanel {

    private WhiteCard[] wcArr;

    private final Runner runner;

    private int playerCount;

    private boolean isCzar;

    private boolean visible;

    public CardArea(Runner r){
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.runner = r;
        this.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        this.visible = true;
    }
    public void setWc(WhiteCard[] wc, String type){
        this.removeAll();
        wcArr = wc;
        addCards(wc, type);
    }

    public void setBC(BlackCard bc) {
        this.removeAll();
        addBlackCard(bc);
    }

    public void setVisibility(boolean visible){
        this.visible = visible;
    }

    public boolean getVisibility(){
        return this.visible;
    }

    public void setPlayerCount(int playerCount){
        this.playerCount = playerCount;
    }

    private void addBlackCard(BlackCard pbc){
        this.add(pbc);
    }

    private void addCards(WhiteCard[] wcArr, String type){
        for(WhiteCard wc : wcArr){
            this.wcArr = wcArr;
            if(type.equals("handCards") || (type.equals("putCards") && wcArr.length == playerCount-1)) {
                wc.setVisibility(true);
                wc.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (type.equals("putCards") && isCzar) {
                            runner.putCard(wc.getIndex());
                            setVisibility(false);
                        } else if (type.equals("handCards") && !isCzar) {
                            runner.playCard(wc.getIndex());
                            setVisibility(false);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        wc.setColor(new Color(0x9A9794));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        wc.setColor(Color.WHITE);
                    }
                });
            }else{
                wc.setVisibility(false);
            }
            this.add(wc);
            this.add(Box.createRigidArea(new Dimension(10, 0)));
        }
    }

    public int getCardLength() {
        return wcArr.length;
    }

    public void setCzar(boolean czar) {
        isCzar = czar;
    }
}
