package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class WhiteCard extends JButton {

    private String text;
    public WhiteCard(String content){
        this.text = content;
        this.setBackground(Color.WHITE);
    }

    public String getPassedText() {
        return text;
    }

    public void showText(Boolean b){
        if(b) {
            this.setText(text);
        }else{
            this.setText("");
        }
    }

    public void setPassedText(String text) {
        this.text = text;
    }
}
