package org.cardsagainsthumanity.client;

import org.cardsagainsthumanity.client.sender.Sender;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class Runner {
    private Sender s;
    private String name;
    private int score;
    private int round;


    private WhiteCard[] wcarr;

    private BlackCard bc;

    private String gamestate;

    public Runner(String conn, String pname) throws IOException, InterruptedException {
        name = pname;
        Sender s = new Sender("localhost");

        boolean run = true;

        while(run){
            update();
            JSONDecoder();
            Thread.sleep(1000);
        }
    }
    public void update(){
        gamestate = s.sendMessage("update "+ name);
        if (gamestate == null){
            s.closeConnection();
        }
    }

    public void JSONDecoder(){
        JSONObject jobj = new JSONObject(gamestate);
        score = jobj.getInt("score");
        bc = new BlackCard(jobj.getString("blackCard"));
        round = jobj.getInt("round");
        JSONArray temp = jobj.getJSONArray("whiteCards");
        wcarr = new WhiteCard[temp.length()];
        for(int i=0;i< wcarr.length;i++){
            wcarr[i] = new WhiteCard(temp.getString(i));
        }
    }
}
