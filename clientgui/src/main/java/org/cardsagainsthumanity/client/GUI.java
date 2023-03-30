package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame{
    private final Runner runner;
    private JPanel loginScreen;

    private WaitingScreen waitingScreen;
    private GameView gameArea;

    private final Container contentPane;

    private final ChatPanel chat;
    private final DataModel dataModel;

    public GUI(Runner prunner, DataModel dm){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Insets scnMax = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        int taskbarSize = scnMax.bottom;


        this.setPreferredSize(new Dimension((int) screenSize.getWidth(),(int) screenSize.getHeight()-(int)taskbarSize));
        this.setResizable(false);
        this.dataModel = dm;
        this.runner = prunner;
        this.chat = new ChatPanel(runner);
        this.setSize(this.getPreferredSize());
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if(runner.getSender()!=null) {
                    runner.getSender().sendMessage("exit");
                }
                System.exit(0);
            }

        });

        contentPane = this.getContentPane();
        contentPane.setPreferredSize(this.getSize());
        contentPane.setBackground(Color.BLUE);
        init_login();
        this.setVisible(true);
    }

    public void initWaitingScreen(){
        this.waitingScreen = new WaitingScreen(runner, chat, dataModel);
    }

    public void init_area(WhiteCard[] hcs, WhiteCard[] pcs, BlackCard bc){
        this.gameArea = new GameView(runner, dataModel);
        gameArea.setPreferredSize(this.getSize());
        rebuild_area(hcs, pcs, bc);
    }

    public void rebuild_area(WhiteCard[] hcs, WhiteCard[] pcs, BlackCard bc){
        gameArea.removeAll();
        gameArea.setBackground(Color.GRAY);
        gameArea.setPutCards(pcs);
        gameArea.setHandCards(hcs);
        gameArea.setBlackCard(bc);
        gameArea.setScoreBoard();
        gameArea.setChat(chat);
    }

    public void init_login(){
        loginScreen = new JPanel();
        loginScreen.setLayout(new GridBagLayout());
        loginScreen.setBackground(Color.GRAY);

        JPanel formField = new JPanel();
        formField.setBackground(Color.WHITE);

        formField.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JTextField gameName = new JTextField();
        JTextField gameCode = new JTextField();

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        formField.add(new JLabel("Your Name:"), c);

        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 1;
        formField.add(gameName, c);

        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        formField.add(new JLabel("Game-code:"), c);

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 3;
        formField.add(gameCode, c);

        c.weightx = 0.1;
        c.gridx = 2;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        JButton submit = new JButton("Join");

        submit.addActionListener(e -> {
            if(gameCode.getText().equals("") || gameName.getText().equals("")){
                System.out.println("Please input data");
            }else {
                runner.setName(gameName.getText());
                runner.setConn(gameCode.getText());
            }
        }
        );

        formField.add(submit, c);


        loginScreen.add(formField);
    }

    public void setActiveScreen(String type){
        switch (type) {
            case "login":
                contentPane.removeAll();
                contentPane.add(loginScreen);
                loginScreen.setVisible(true);
                this.getContentPane().repaint();
                this.repaint();
                this.getContentPane().revalidate();
                this.revalidate();
                break;
            case "waiting":
                contentPane.removeAll();
                this.getContentPane().repaint();
                waitingScreen.setVisible(true);
                contentPane.add(waitingScreen);
                this.repaint();
                this.getContentPane().revalidate();
                this.revalidate();
                break;
            case "game":
                waitingScreen.removeListener();
                contentPane.removeAll();
                contentPane.add(gameArea);
                gameArea.setVisible(true);
                System.out.printf(gameArea.getPreferredSize().toString());
                this.getContentPane().repaint();
                this.repaint();
                this.getContentPane().revalidate();
                this.revalidate();
                break;
        }
    }
}