package org.cardsagainsthumanity.client;

import javax.swing.*;

public class Gameview extends JPanel {

    private CardArea handcards;
    private CardArea putcards;

    public Gameview(){

    }
    public void setHandcards(WhiteCard[] wc){
        this.handcards = new CardArea();
        this.handcards.setWcArr(wc);
        this.handcards.addCards();
        this.add(handcards);
    }

    public void checkVisibility(boolean isTzar){
        if (isTzar){
            this.handcards.setVisible(false);
            this.putcards.setVisible(true);
        }else{
            this.handcards.setVisible(true);
            this.putcards.setVisible(false);
        }
    }

    public void setPutcards(WhiteCard[] wc){
        this.putcards = new CardArea();
        this.putcards.setWcArr(wc);
        this.putcards.addCards();
        this.add(putcards);
    }

}
