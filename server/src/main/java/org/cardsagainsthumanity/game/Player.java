package org.cardsagainsthumanity.game;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

public class Player {
    public String name;
    public int score;
    public ArrayList<WhiteCard> whitecards = new ArrayList<WhiteCard>();
    public boolean isCzar = false;
    public String ip = "";

    public Player(String name) {
        this.name = name;
        this.score = 0;
        for (int i = 0; i < 10; i++) {
            whitecards.add(this.whiteCard());
        }
    }

    public boolean isHost() throws UnknownHostException {
        String hostIp = InetAddress.getLocalHost().getHostAddress();
        return ip.equals(hostIp);
    }

    public String getRole() throws UnknownHostException {
        return isHost() ? "host" : "player";
    }

    public WhiteCard whiteCard() {
        InputStream is = Logic.class.getClassLoader().getResourceAsStream("white.csv");
        assert is != null;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        ArrayList<String> lines = new ArrayList<String>();
        try {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Now return a random line from the file
        Random rand = new Random();
        int randomNum = rand.nextInt(lines.size());
        String line = lines.get(randomNum);
        return new WhiteCard(line, this);
    }
}
