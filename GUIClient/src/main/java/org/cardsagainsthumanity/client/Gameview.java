package org.cardsagainsthumanity.client;

import javax.swing.*;

public class Gameview extends JPanel {

    private JPanel hand;
    public Gameview(){

    }

    public void setContent(WhiteCard[] hc, BlackCard bc){
        for(WhiteCard c : hc){
            hand.add(c);
        }
    }

}
