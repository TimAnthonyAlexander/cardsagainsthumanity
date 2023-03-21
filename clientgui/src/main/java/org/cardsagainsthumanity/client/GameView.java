package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameView extends JPanel {

    private CardArea handCards;
    private CardArea putCards;
    private CardArea blackCard;

    public GameView(){
        this.setLayout(new BorderLayout(8, 6));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();
                handCards.setPreferredSize(new Dimension(width, height / 3));
                blackCard.setPreferredSize(new Dimension(width / 10, height / 3));
                putCards.setPreferredSize(new Dimension(width * 3 / 5, height / 3));
                revalidate();
            }
        });
    }
    public void setHandCards(WhiteCard[] wc){
        this.handCards = new CardArea();
        this.handCards.setWcArr(wc);
        this.handCards.addCards();
        this.handCards.setPreferredSize(new Dimension(0, this.getHeight()/3));
        this.handCards.setBackground(Color.GREEN);
        this.add(handCards, BorderLayout.PAGE_END);
    }

    public void checkVisibility(boolean isTzar){
        if (isTzar){
            this.handCards.setVisible(false);
            this.putCards.setVisible(true);
        }else{
            this.handCards.setVisible(true);
            this.putCards.setVisible(false);
        }
    }

    public void setPutCards(WhiteCard[] wc){
        this.putCards = new CardArea();
        this.putCards.setWcArr(wc);
        this.putCards.addCards();
        this.putCards.setPreferredSize(new Dimension(this.getWidth() * 3 / 5, this.getHeight()/3));
        this.putCards.setBackground(Color.BLUE);
        this.add(putCards, BorderLayout.CENTER);
    }

    public void setBlackCard(BlackCard bc){
        this.blackCard = new CardArea();
        this.blackCard.setBC(bc);
        this.blackCard.addBlackCard();
        this.blackCard.setPreferredSize(new Dimension(this.getWidth()/10, this.getHeight()/3));
        this.blackCard.setBackground(Color.RED);
        this.add(blackCard, BorderLayout.LINE_START);
    }
}
