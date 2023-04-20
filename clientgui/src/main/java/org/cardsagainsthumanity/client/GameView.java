package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameView extends JPanel implements DataModelListener {

    private CardArea handCards;
    private CardArea putCards;
    private CardArea blackCard;
    private final Runner runner;
    private final DataModel dataModel;
    private ScoreBoard scoreBoard;
    private ChatPanel chat;
    private boolean czar;

    public GameView(Runner r, DataModel dataModel){
        this.setLayout(new BorderLayout(8, 6));
        this.runner = r;
        this.dataModel = dataModel;
        this.dataModel.addListener(this);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();
                handCards.setPreferredSize(new Dimension(width, height / 3));
                blackCard.setPreferredSize(new Dimension(width / 5, height * 19 / 30));
                putCards.setPreferredSize(new Dimension(width * 3 / 5, height / 3));
                chat.setPreferredSize(new Dimension(width / 5, height * 19 / 30));
                scoreBoard.setPreferredSize(new Dimension(0, height / 30));
                revalidate();
            }
        });
    }

    public void checkVisibility(){
        if (czar){
            this.handCards.setVisible(false);
            this.putCards.setVisible(true);
        }else{
            this.handCards.setVisible(this.handCards.getVisibility());
            this.putCards.setVisible(false);
        }
    }

    public void setChat(ChatPanel c){
        this.chat = c;
        this.chat.setPreferredSize(new Dimension(this.getWidth() * 3 / 5, this.getHeight() * 19 / 30));
        this.add(c, BorderLayout.LINE_END);
    }

    public void setHandCards(WhiteCard[] wc){
        this.handCards = new CardArea(runner);
        this.handCards.setWc(wc, "handCards");
        this.handCards.setPreferredSize(new Dimension(0, this.getHeight()/3));
        //this.handCards.setBackground(Color.GREEN);
        this.add(handCards, BorderLayout.PAGE_END);
    }

    public void setPutCards(WhiteCard[] wc){
        this.putCards = new CardArea(runner);
        this.putCards.setWc(wc, "putCards");
        this.putCards.setPreferredSize(new Dimension(this.getWidth() * 3 / 5, this.getHeight() / 3));
        //this.putCards.setBackground(Color.BLUE);
        this.add(putCards, BorderLayout.CENTER);
    }

    public void setBlackCard(BlackCard bc){
        this.blackCard = new CardArea(runner);
        this.blackCard.setBC(bc);
        this.blackCard.setPreferredSize(new Dimension(this.getWidth() / 5, this.getHeight() * 19 / 30));
        //this.blackCard.setBackground(Color.RED);
        this.add(blackCard, BorderLayout.LINE_START);
    }

    public void setScoreBoard(){
        this.scoreBoard = new ScoreBoard();
        this.scoreBoard.setPreferredSize(new Dimension(0, this.getHeight() / 30));
        updateScoreBoard();
        this.add(scoreBoard, BorderLayout.PAGE_START);
    }

    public void updateScoreBoard(){
        this.scoreBoard.updateContent(dataModel.getScore(), dataModel.getRound(), dataModel.isCzar());
    }

    public void updateView(DataModel model){
        this.blackCard.setPlayerCount(model.getPlayers().length);
        this.blackCard.setBC(model.getBlackCard());
        this.putCards.setPlayerCount(model.getPlayers().length);
        this.putCards.setWc(model.getPutCards(),"putCards");
        this.handCards.setPlayerCount(model.getPlayers().length);
        this.handCards.setWc(model.getHandCards(), "handCards");
        this.czar = model.isCzar();
        checkVisibility();
        System.out.println("Updated View");
        revalidate();
        repaint();
    }



    public void updateChat(String[] chat){
        this.chat.updateChat(chat);
    }

    @Override
    public void dataModelChanged(DataModel dataModel) {
        updateView(dataModel);
    }

    @Override
    public void roundChanged(int round) {
        handCards.setVisibility(true);
        updateScoreBoard();
    }

    @Override
    public void chatChanged(String[] chat) {
        updateChat(chat);
    }

    @Override
    public void playersChanged(String[] players) {

    }
}
