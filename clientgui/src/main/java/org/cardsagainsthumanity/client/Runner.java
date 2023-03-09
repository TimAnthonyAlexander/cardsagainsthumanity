package org.cardsagainsthumanity.client;

import org.cardsagainsthumanity.client.sender.Sender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class Runner {
    private Sender sender;
    private String name, conn;
    private int score;
    private int round;
    private boolean connected;

    private WhiteCard[] pcarr;


    private WhiteCard[] wcarr;

    private BlackCard bc;

    private String gamestate;
    private boolean czar;

    public Runner() throws IOException, InterruptedException {
        GUI gui = new GUI(this);
        name = "";
        conn = "";
        connected = false;
        round = 0;
        czar = false;

        while(!connected){
            while(name == "" && conn == ""){
                Thread.sleep(1000);
            }
            try {
                sender = new Sender(conn);
                if(sender.getClientSocket().isConnected()){
                    connected = true;
                }
            }catch(IOException e){
                System.out.println("Can't connect to host: "+ conn + " Please try again or check your connection parameters");
                name="";
                conn="";
            }
        }
        gui.hide_login();
        sender.sendMessage("join " + name);

        boolean run = true;

        while(run){
            update();
            JSONDecoder();
            Thread.sleep(16);
        }
    }
    public void update(){
        try{
            gamestate = sender.sendMessage("update "+ name);
        }catch (Exception e){
            System.out.println("Server Error");
            sender.closeConnection();
        }

    }

    public void putCard(int index){
        sender.sendMessage("putCard "+ name + " "+ index);
    }

    public Sender getSender(){
        return sender;
    }

    public boolean isTzar(){
        return czar;
    }

    public void playCard(WhiteCard c){
        sender.sendMessage("play "+ name + " " + c.getIndex());
    }

    public void JSONDecoder(){
        JSONObject jobj = new JSONObject(gamestate);
        score = jobj.getInt("score");
        bc = new BlackCard(jobj.getString("blackCard"));
        round = jobj.getInt("round");
        JSONArray temp = jobj.getJSONArray("whiteCards");
        czar = jobj.getBoolean("isCzar");
        wcarr = new WhiteCard[temp.length()];
        for(int i=0;i< wcarr.length;i++){
            wcarr[i] = new WhiteCard(temp.getString(i), i);
        }
        temp = jobj.getJSONArray("putCards");
        pcarr = new WhiteCard[temp.length()];
        for(int i=0;i< pcarr.length;i++){
            if(!temp.isNull(i)) {
                pcarr[i] = new WhiteCard(temp.getString(i), i);
            }else{
                continue;
            }
        }
    }

    public void setName(String s){
        this.name = s;
    }

    public void setConn(String s){
        this.conn = s;
    }
}
