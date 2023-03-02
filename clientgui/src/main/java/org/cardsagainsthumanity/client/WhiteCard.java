package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class WhiteCard extends JButton {

    private String text;
    private int index;
    public WhiteCard(String content, int index){
        this.text = content;
        this.index = index;
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

    public int getIndex(){
        return this.index;
    }

    public void setPassedText(String text) {
        this.text = text;
    }
}
