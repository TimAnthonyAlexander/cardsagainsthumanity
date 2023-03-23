package org.cardsagainsthumanity.client;

import org.cardsagainsthumanity.client.sender.Sender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class Runner {
    private Sender sender;
    private String name, conn;
    private boolean connected;

    private GUI gui;
    private String gamestate;
    private boolean czar, waiting;

    private DataModel model;

    public Runner() throws IOException, InterruptedException {
        model = new DataModel();
        name = "";
        conn = "";
        connected = false;

        czar = false;
        gui = new GUI(this, model);
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
                        if (gameStarted()){
                            waiting = false;
                            gui.init_area(model.getHandCards(), model.getPutCards(), model.getBlackCard());
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

    public boolean gameStarted(){
        if(model.getRound() == 1 && model.getPreviousRound() == 0){
            return true;
        }else {
            return false;
        }
    }
    public void startGame(){
        sender.sendMessage("start");
    }

    public boolean isHost(){
        if(model.getRole().equals("host")){
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
        sender.sendMessage("chooseCard "+ index);
    }

    public Sender getSender(){
        return sender;
    }

    public boolean isTzar(){
        return czar;
    }

    public void playCard(int index){
        sender.sendMessage("purCard " + index);
    }

    public void JSONDecoder(){
        if(!(gamestate.isEmpty() || gamestate.equals("error") || gamestate.equals(""))) {
            JSONObject jobj = new JSONObject(gamestate);
            int score = jobj.getInt("score");
            BlackCard bc = new BlackCard(jobj.getString("blackCard"));
            int previousRound = model.getRound();
            int round = jobj.getInt("round");
            boolean czar = jobj.getBoolean("isCzar");
            String role = jobj.getString("role");
            JSONArray temp = jobj.getJSONArray("whiteCards");
            WhiteCard[] wcarr = new WhiteCard[temp.length()];
            for (int i = 0; i < wcarr.length; i++) {
                if (!temp.isNull(i)) {
                    wcarr[i] = new WhiteCard(temp.getString(i), i);
                }
            }
            temp = jobj.getJSONArray("putCards");
            WhiteCard[] pcarr = new WhiteCard[temp.length()];
            for (int i = 0; i < pcarr.length; i++) {
                if (!temp.isNull(i)) {
                    pcarr[i] = new WhiteCard(temp.getString(i), i);
                } else {
                    continue;
                }
            }
            model.setScore(score);
            model.setCzar(czar);
            model.setRound(round);
            model.setPreviousRound(previousRound);
            model.setBlackCard(bc);
            model.setHandCards(wcarr);
            model.setPutCards(pcarr);
            model.setRole(role);
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
