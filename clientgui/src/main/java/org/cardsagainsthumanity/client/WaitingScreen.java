package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class WaitingScreen extends JPanel implements DataModelListener{
    private Runner runner;
    private DataModel dataModel;
    private ChatPanel chat;
    public WaitingScreen(Runner r, ChatPanel chat, DataModel dm){
        this.runner = r;
        this.dataModel = dm;
        this.chat = chat;
        this.dataModel.addListener(this);

        this.setLayout(new BorderLayout());
        JPanel center = new JPanel();

        JLabel waitingText = new JLabel("Waiting for other Players");
        JButton hostStart = new JButton("Start Game");
        if (!runner.isHost()) {
            hostStart.setVisible(false);
        }
        hostStart.addActionListener(e -> {
            runner.startGame();
        });

        center.add(waitingText);
        center.add(hostStart);
        chat.setPreferredSize(new Dimension(this.getWidth() * 1/5, this.getHeight() * 2/3));

        this.add(chat, BorderLayout.LINE_END);
        this.add(center, BorderLayout.CENTER);
    }

    public void updateView(DataModel d){
        chat.updateChat(d.getChat());
    }

    public void removeListener(){
        dataModel.removeListener(this);
    }

    @Override
    public void dataModelChanged(DataModel dataModel) {
        updateView(dataModel);
    }
}
