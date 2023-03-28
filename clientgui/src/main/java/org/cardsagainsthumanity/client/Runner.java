package org.cardsagainsthumanity.client;

import org.cardsagainsthumanity.client.sender.Sender;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class Runner implements DataModelListener{
    private Sender sender;
    private String name, conn;

    private final GUI gui;
    private String gameState;

    private final DataModel model;

    public Runner() throws InterruptedException {
        model = new DataModel();
        name = "";
        conn = "";
        boolean connected = false;

        model.addListener(this);

        gui = new GUI(this, model);
        gui.setActiveScreen("login");
        while(!connected){
            while(name.equals("") && conn.equals("")){
                Thread.sleep(1000);
            }
            try {
                sender = new Sender(conn);
                if(sender.getClientSocket().isConnected()){
                    connected = true;
                    sender.sendMessage("join " + name);
                    UUID requestId = UUID.randomUUID();
                    update(requestId);
                    JSONDecoder(requestId);
                    gui.initWaitingScreen();
                    gui.setActiveScreen("waiting");
                }
            }catch(IOException e){
                System.out.println("Can't connect to host: "+ conn + " Please try again or check your connection parameters");
                name="";
                conn="";
            }
        }
        boolean run = true;
        while(run){
            try {
                UUID requestId = UUID.randomUUID();
                update(requestId);
                if(gameState.equals("Connection lost")){
                    System.out.println("Connection closed unexpectedly");
                    break;
                }
                Thread.sleep(100);
                JSONDecoder(requestId);
            }catch (JSONException e){
                System.out.println(gameState);
            }catch (Exception e){
                System.out.println("Communication Error");
                run = false;
            }
        }
    }
    public void startGame(){
        sender.sendMessage("start");
    }

    public boolean isHost(){
        return model.getRole().equals("host");
    }

    public void sendMessage(String message){
        sender.sendMessage("sendChat "+ message);
    }

    public void update(UUID requestId){
        try{
            gameState = sender.sendMessage("update");// "+ requestId);
        }catch (Exception e){
            System.out.println("Server Error");
            sender.closeConnection();
        }
    }

    public void putCard(int index){
        if(model.getPlayers().length-1 == model.getPutCards().length) {
            sender.sendMessage("chooseCard " + index);
        }
    }

    public Sender getSender(){
        return sender;
    }

    public void playCard(int index){
        sender.sendMessage("putCard " + index);
    }

    public void JSONDecoder(UUID requestId){
        if(!(gameState == null || gameState.equals("error") || gameState.equals(""))) {
            JSONObject jObj = new JSONObject(gameState);
            //if (jObj.has("messageId") && jObj.getString("messageId").equals(requestId.toString())) {
                int score = jObj.getInt("score");
                BlackCard bc = new BlackCard(jObj.getString("blackCard"));
                int previousRound = model.getRound();
                int round = jObj.getInt("round");
                boolean czar = jObj.getBoolean("isCzar");
                String role = jObj.getString("role");
                JSONArray temp = jObj.getJSONArray("whiteCards");
                WhiteCard[] wcArr = new WhiteCard[temp.length()];
                for (int i = 0; i < wcArr.length; i++) {
                    if (!temp.isNull(i)) {
                        wcArr[i] = new WhiteCard(temp.getString(i), i);
                    }
                }
                temp = jObj.getJSONArray("putCards");
                WhiteCard[] pcarr = new WhiteCard[temp.length()];
                for (int i = 0; i < pcarr.length; i++) {
                    if (!temp.isNull(i)) {
                        pcarr[i] = new WhiteCard(temp.getString(i), i);
                    } else {
                        continue;
                    }
                }
                temp = jObj.getJSONArray("chat");
                String[] chat = new String[temp.length()];
                for (int i = 0; i < chat.length; i++) {
                    if (!temp.isNull(i)) {
                        chat[i] = temp.getString(i);
                    } else {
                        continue;
                    }
                }
                temp = jObj.getJSONArray("playerNames");
                String[] playerNames = new String[temp.length()];
                for (int i = 0; i < playerNames.length; i++) {
                    if (!temp.isNull(i)) {
                        playerNames[i] = temp.getString(i);
                    } else {
                        continue;
                    }
                }
                model.setScore(score);
                model.setCzar(czar);
                model.setRound(round);
                model.setPreviousRound(previousRound);
                model.setBlackCard(bc);
                model.setHandCards(wcArr);
                model.setPutCards(pcarr);
                model.setRole(role);
                model.setChat(chat);
                model.setPlayers(playerNames);
            //}else{
                //System.out.println("Did not receive correct UUID");
            //}
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

    @Override
    public void dataModelChanged(DataModel dataModel) {

    }

    @Override
    public void roundChanged(int round) {
        System.out.println(round);
        this.model.removeListener(this);
        if(round == 1 && model.getPreviousRound() == 0){
            gui.init_area(model.getHandCards(), model.getPutCards(), model.getBlackCard());
            gui.setActiveScreen("game");
        }
    }

    @Override
    public void chatChanged(String[] chat) {

    }

    @Override
    public void playersChanged(String[] players) {

    }
}
