package org.cardsagainsthumanity.client;

import org.cardsagainsthumanity.client.sender.Sender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class Runner {
    private Sender sender;
    private String name, conn, role;
    private int score;
    private int round, previousround;
    private boolean connected;

    private GUI gui;

    private WhiteCard[] pcarr;


    private WhiteCard[] wcarr;

    private BlackCard bc;

    private String gamestate;
    private boolean czar, waiting;

    public Runner() throws IOException, InterruptedException {
        name = "";
        conn = "";
        connected = false;
        round = 0;
        previousround = 0;
        czar = false;
        gui = new GUI(this);
        gui.setActiveScreen("login");
        while(!connected){
            while(name == "" && conn == ""){
                Thread.sleep(1000);
            }
            try {
                sender = new Sender(conn);
                if(sender.getClientSocket().isConnected()){
                    connected = true;
                    sender.sendMessage("join " + name);
                    update();
                    JSONDecoder();
                    gui.init_waitingscreen();
                    gui.setActiveScreen("waiting");
                    waiting = true;
                    while(waiting){
                        update();
                        JSONDecoder();
                        if (gamestarted()){
                            waiting = false;
                            gui.init_area(wcarr, pcarr, bc);
                            gui.setActiveScreen("game");
                        }
                    }
                }
            }catch(IOException e){
                System.out.println("Can't connect to host: "+ conn + " Please try again or check your connection parameters");
                name="";
                conn="";
            }
        }
        boolean run = true;
        while(run){
            update();
            JSONDecoder();
            Thread.sleep(16);
        }
    }

    public boolean gamestarted(){
        if(round == 1 && previousround == 0){
            return true;
        }else {
            return false;
        }
    }
    public void startGame(){
        sender.sendMessage("start");
    }

    public boolean isHost(){
        if(role.equals("host")){
            return true;
        }
        return false;
    }

    public void update(){
        try{
            gamestate = sender.sendMessage("update");

        }catch (Exception e){
            System.out.println("Server Error");
            sender.closeConnection();
        }

    }

    public void putCard(int index){
        sender.sendMessage("putCard "+ index);
    }

    public Sender getSender(){
        return sender;
    }

    public boolean isTzar(){
        return czar;
    }

    public void playCard(WhiteCard c){
        sender.sendMessage("play " + c.getIndex());
    }

    public void JSONDecoder(){
        if(!(gamestate.equals("error") || gamestate.isEmpty() || gamestate.equals(""))) {
            JSONObject jobj = new JSONObject(gamestate);
            score = jobj.getInt("score");
            bc = new BlackCard(jobj.getString("blackCard"));
            previousround = round;
            round = jobj.getInt("round");
            JSONArray temp = jobj.getJSONArray("whiteCards");
            czar = jobj.getBoolean("isCzar");
            wcarr = new WhiteCard[temp.length()];
            role = jobj.getString("role");
            for (int i = 0; i < wcarr.length; i++) {
                if (!temp.isNull(i)) {
                    wcarr[i] = new WhiteCard(temp.getString(i), i);
                }
            }
            temp = jobj.getJSONArray("putCards");
            pcarr = new WhiteCard[temp.length()];
            for (int i = 0; i < pcarr.length; i++) {
                if (!temp.isNull(i)) {
                    pcarr[i] = new WhiteCard(temp.getString(i), i);
                } else {
                    continue;
                }
            }
        }else{
            System.out.println("Could not update");
        }
    }

    public void setName(String s){
        this.name = s;
    }

    public void setConn(String s){
        this.conn = s;
    }
}
