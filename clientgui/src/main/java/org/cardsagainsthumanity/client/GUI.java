package org.cardsagainsthumanity.client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GUI extends JFrame{
    private Runner runner;
    private JPanel loginscreen;

    private JButton start;

    private JPanel waitingscreen;
    private GameView gamearea;

    private Container contentPane;

    public GUI(Runner prunner){
        this.setPreferredSize(new Dimension(1920,1080));

        this.runner = prunner;
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

    public void init_waitingscreen(){
        this.waitingscreen = new JPanel();
        waitingscreen.setLayout(new GridBagLayout());
        JPanel center = new JPanel();

        JLabel waitingtext = new JLabel("Waiting for other Players");
        JButton hoststart = new JButton("Start Game");
        if (!runner.isHost()) {
            hoststart.setVisible(false);
        }
        hoststart.addActionListener(e -> {
            runner.startGame();
        });

        center.add(waitingtext);
        center.add(hoststart);

        waitingscreen.add(center);
    }

    public void init_area(WhiteCard[] hcs, WhiteCard[] pcs, BlackCard bc){
        this.gamearea = new GameView();
        gamearea.setPreferredSize(this.getSize());
        rebuild_area(hcs, pcs, bc);
    }

    public void rebuild_area(WhiteCard[] hcs, WhiteCard[] pcs, BlackCard bc){
        gamearea.removeAll();
        gamearea.setBackground(Color.YELLOW);
        gamearea.setPutCards(pcs);
        gamearea.setHandCards(hcs);
        gamearea.setBlackCard(bc);
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
                waitingscreen.setVisible(true);
                contentPane.add(waitingscreen);
                this.repaint();
                this.getContentPane().revalidate();
                this.revalidate();
                break;
            case "game":
                contentPane.removeAll();
                contentPane.add(gamearea);
                gamearea.setVisible(true);
                System.out.printf(gamearea.getPreferredSize().toString());
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