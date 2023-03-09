package org.cardsagainsthumanity.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    public String name;
    public int score;
    public WhiteCard[] whitecards;
    public boolean isCzar = false;
    public String ip = "";

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.whitecards = new WhiteCard[10];
        for (int i = 0; i < whitecards.length; i++) {
            whitecards[i] = this.whiteCard();
        }
    }

    public boolean isHost() throws UnknownHostException {
        String hostIp = InetAddress.getLocalHost().getHostAddress();
        System.out.println("Host IP: " + hostIp);
        System.out.println("Player IP: " + ip);
        return ip.equals(hostIp);
    }

    public String getRole() throws UnknownHostException {
        return isHost() ? "host" : "player";
    }

    private WhiteCard whiteCard() {
        String csv = "white.csv";
        String text;
        // Read the file and get a random line
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\n");
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int random = (int) (Math.random() * records.size());
        text = records.get(random).get(0);
        return new WhiteCard(text);
    }
}
