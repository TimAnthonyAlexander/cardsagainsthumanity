package org.cardsagainsthumanity.client;

public class BlackCard {

    private String text;
    public BlackCard(String content){
        text = content;
    }

    public String getText(){
        return text;
    }

    public void setText(String s){
        text = s;
    }
}
