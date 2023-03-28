package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame{
    private Runner runner;
    private JPanel loginscreen;

    private JButton start;

    private WaitingScreen waitingScreen;
    private GameView gameArea;

    private Container contentPane;

    private ChatPanel chat;
    private DataModel dataModel;

    public GUI(Runner prunner, DataModel dm){
        this.setPreferredSize(new Dimension(1920,1080));
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
        gameArea.setBackground(Color.YELLOW);
        gameArea.setPutCards(pcs);
        gameArea.setHandCards(hcs);
        gameArea.setBlackCard(bc);
        gameArea.setScoreBoard();
        gameArea.setChat(chat);
    }

    public GameView getGameArea(){
        return gameArea;
    }

    public void init_login(){
        loginscreen = new JPanel();
        loginscreen.setLayout(new GridBagLayout());
        loginscreen.setBackground(Color.GRAY);

        JPanel formfield = new JPanel();
        formfield.setBackground(Color.WHITE);

        formfield.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JTextField gamename = new JTextField();
        JTextField gamecode = new JTextField();

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        formfield.add(new JLabel("Your Name:"), c);

        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 1;
        formfield.add(gamename, c);

        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        formfield.add(new JLabel("Gamecode:"), c);

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 3;
        formfield.add(gamecode, c);

        c.weightx = 0.1;
        c.gridx = 2;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        JButton submit = new JButton("Join");

        submit.addActionListener(e -> {if(gamecode.getText() == "" || gamename.getText() == ""){

                }else {
                    runner.setName(gamename.getText());
                    runner.setConn(gamecode.getText());
                }
        }
        );

        formfield.add(submit, c);


        loginscreen.add(formfield);
    }

    public void setActiveScreen(String type){
        switch(type){
            case "login":
                contentPane.removeAll();
                contentPane.add(loginscreen);
                loginscreen.setVisible(true);
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
            default:
                System.out.println("LELEL");
                break;
        }
    }
}