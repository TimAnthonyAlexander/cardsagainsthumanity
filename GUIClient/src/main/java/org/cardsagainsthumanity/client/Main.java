package org.cardsagainsthumanity.client;

public class Main {

    public static void main(String[] args){
        try {
            /*Sender s = new Sender();
            System.out.println(s.sendMessage("start"));
            s.sendMessage("stop");
            s.sendMessage("exit");
            s.closeConnection();*/
            new Runner("10.62.163.63","Peter");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
