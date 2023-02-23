package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame{


    public GUI(){
        this.setPreferredSize(new Dimension(1920,1080));

        this.setSize(this.getPreferredSize());
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        init_login();

    }

    public void init_login(){
        JPanel loginscreen = new JPanel();
        loginscreen.setLayout(new GridBagLayout());
        loginscreen.setBackground(Color.GRAY);

        JPanel formfield = new JPanel();
        formfield.setBackground(Color.WHITE);

        formfield.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 0;
        formfield.add(new JLabel("Your Name:"), c);

        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 0;
        c.gridy = 1;
        formfield.add(new JTextField(), c);

        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        formfield.add(new JLabel("Gamecode:"), c);

        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 3;
        formfield.add(new JTextField(), c);

        c.weightx = 0.1;
        c.gridx = 2;
        c.gridy = 3;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        JButton submit = new JButton("Join");

        submit.addActionListener(e -> System.out.println("Clicked"));

        formfield.add(submit, c);


        loginscreen.add(formfield);

        this.add(loginscreen);

        this.setVisible(true);
    }
}