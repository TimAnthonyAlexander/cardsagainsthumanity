package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class ChatPanel extends JPanel {
    private final JTextField inputField;
    private final JTextArea chatField;
    private final JButton send;

    public ChatPanel(Runner r){
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(5,5,5,5);

        // Create the chat text area and add it to the panel
        chatField = new JTextArea(20,50);
        chatField.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(chatField);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.fill = GridBagConstraints.BOTH;
        add(scrollPane, c);

        // Create the input text field and add it to the panel
        inputField = new JTextField(30);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.weightx = 0.9;
        c.weighty = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        add(inputField, c);

        // Create the send button and add it to the panel
        send = new JButton("Send");
        send.addActionListener(e -> {
            String text = inputField.getText();
            if(text.length()>1){
                r.sendMessage(inputField.getText());
                inputField.setText("");
            }
        });
        c.gridx = 1;
        c.gridy = 1;
        c.weightx = 0.1;
        add(send, c);
    }

    public void updateChat(String[] messages){
        chatField.setText("");
        for(String message : messages){
            chatField.append(message+"\n");
        }
    }

}
