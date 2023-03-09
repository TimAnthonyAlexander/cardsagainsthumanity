package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class GUI extends JFrame{
    private Runner runner;
    private JPanel loginscreen;
    private JPanel gamearea;

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
        init_login();

    }

    public void init_area(){
        this.gamearea = new JPanel();
        gamearea.setLayout(new GridBagLayout());
    }

    public void rebuild_area(WhiteCard[] hcs, WhiteCard[] pcs, BlackCard bc){
        gamearea.removeAll();
        GridBagConstraints c = new GridBagConstraints();
        if(runner.isTzar()){

        }else{
            JPanel handcardarea = new JPanel();
            for(WhiteCard w : hcs){
                handcardarea.add(w);
            }
        }
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

        this.add(loginscreen);

        this.setVisible(true);
    }

    public void hide_login(){
        this.loginscreen.setVisible(false);
    }
}