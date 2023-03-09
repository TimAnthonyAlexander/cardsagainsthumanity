package org.cardsagainsthumanity.game;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Logic {
    public Player[] players;
    public BlackCard blackCard = this.blackCard();
    public Integer round = 0;
    public WhiteCard[] putCards = new WhiteCard[100];
    public int updatePull = 0;

    public Logic() {
        players = new Player[100];
    }

    /**
     * 
     */
    public void startGame() {
        round = 1;
        blackCard = this.blackCard();
    }

    public void stopGame() {

    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public BlackCard getBlackCard() {
        return blackCard;
    }

    public void setBlackCard(BlackCard blackCard) {
        this.blackCard = blackCard;
    }

    public Integer getRound() {
        return round;
    }

    public void setRound(Integer round) {
        this.round = round;
    }

    public void joinGame(String playerName) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new Player(playerName);
                break;
            }
        }
        System.out.println("Player " + playerName + " joined the game");
    }

    public Player getPlayer(String name) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].name.equals(name)) {
                return players[i];
            }
        }
        return null;
    }

    public String getUpdate(String playerName) {
        updatePull++;
        // Return a json object containing the blackcard, and all whitecards of the
        // player that is requesting the update
        BlackCard blackCard = this.blackCard;
        Player player = getPlayer(playerName);
        int round = this.round;
        int score = player.score;
        JSONObject json = new JSONObject();
        json.put("blackCard", blackCard.content);
        json.put("isCzar", player.isCzar);
        json.put("round", round);
        json.put("score", score);
        String[] playerNames = new String[100];
        String[] whiteCards = new String[10];
        String[] putCards = new String[100];
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].name.equals(playerName)) {
                for (int j = 0; j < players[i].whitecards.length; j++) {
                    whiteCards[j] = players[i].whitecards[j].content;
                }
                break;
            }
        }
        for (int i = 0; i < this.putCards.length; i++) {
            if (this.putCards[i] != null) {
                putCards[i] = this.putCards[i].content;
            }
        }
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                playerNames[i] = players[i].name;
            }
        }
        json.put("putCards", putCards);
        json.put("playerNames", playerNames);
        json.put("whiteCards", whiteCards);

        if (updatePull % 100 == 0) {
            System.out.println("Update pulled: " + updatePull);
            updatePull = 0;
        }

        return json.toString();
    }

    public void putCard(String player, int card) {
        Player p = getPlayer(player);
        if (p != null) {
            for (int i = 0; i < putCards.length; i++) {
                if (putCards[i] == null && p.whitecards[card] != null) {
                    putCards[i] = p.whitecards[card];
                    p.whitecards[card] = null;
                    break;
                }
            }
        }
    }

    private BlackCard blackCard() {
        // column called text, take a random row
        // from the file and return it as a BlackCard object.
        String csv = "black.csv";
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
        return new BlackCard(text);
    }
}
