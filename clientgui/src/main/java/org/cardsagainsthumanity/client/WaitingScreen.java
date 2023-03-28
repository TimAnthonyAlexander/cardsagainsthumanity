package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class WaitingScreen extends JPanel implements DataModelListener{
    private final Runner runner;
    private final DataModel dataModel;
    private final PlayerPanel playerList;
    private final JPanel center;
    private final ChatPanel chat;
    public WaitingScreen(Runner r, ChatPanel chat, DataModel dm){
        this.runner = r;
        center = new JPanel();
        this.dataModel = dm;
        playerList = new PlayerPanel(dm.getPlayers());
        this.chat = chat;
        chat.updateChat(dm.getChat());
        this.dataModel.addListener(this);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();
                center.setPreferredSize(new Dimension(width * 3 / 5, height * 2/3));
                chat.setPreferredSize(new Dimension(width / 5, height * 2/3));
                playerList.setPreferredSize(new Dimension(width / 5, height * 2 / 3));
                revalidate();
            }
        });

        this.setLayout(new BorderLayout());

        JLabel waitingText = new JLabel("Waiting for other Players");
        JButton hostStart = new JButton("Start Game");
        if (!runner.isHost()) {
            hostStart.setVisible(false);
        }
        hostStart.addActionListener(e -> runner.startGame());

        center.add(waitingText);
        center.add(hostStart);

        this.add(center, BorderLayout.CENTER);
        this.add(chat, BorderLayout.LINE_END);
        this.add(playerList, BorderLayout.LINE_START);
    }

    public void updateChat(String [] chat){
        this.chat.updateChat(chat);
    }

    public void updatePlayers(String[] players){
        this.playerList.updatePlayers(players);
    }

    public void removeListener(){
        dataModel.removeListener(this);
    }

    @Override
    public void dataModelChanged(DataModel dataModel) {

    }

    @Override
    public void roundChanged(int round) {

    }

    @Override
    public void chatChanged(String[] chat) {
        updateChat(chat);
    }

    @Override
    public void playersChanged(String[] players) {
        updatePlayers(players);
    }
}
