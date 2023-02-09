package org.cardsagainsthumanity;

import org.cardsagainsthumanity.sender.Sender;

public class Main {

    public static void main(String[] args){
        try {
            Sender s = new Sender();
            System.out.println(s.sendMessage("start"));
            s.sendMessage("stop");
            s.sendMessage("exit");
            s.closeConnection();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
