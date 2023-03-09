package org.cardsagainsthumanity.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONObject;

/**
 * 
 */
public class Logic {
    public Player[] players;
    public BlackCard blackCard = this.blackCard();
    public Integer round = 0;
    public WhiteCard[] putCards = new WhiteCard[100];
    public int updatePull = 0;
    public String[] chat = new String[10000];

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

    public void joinGame(final String playerName, final String ip) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new Player(playerName);
                players[i].ip = ip;
                break;
            }
        }
        System.out.println("Player " + playerName + " joined the game");
    }

    public Player getPlayer(final String name) {
        for (final Player player : players) {
            if (player != null && player.name.equals(name)) {
                return player;
            }
        }
        return null;
    }

    public String getUpdate(final String clientip) throws UnknownHostException {
        // Loop through all players and if the ip is the same as the client ip, set
        // player to that player
        Player player = null;
        for (final Player value : players) {
            if (value != null) {
                final String ip = value.ip;
                if (ip.equals(clientip)) {
                    player = value;
                }
            }
        }

        // if player is not set, return an error
        if (player == null) {
            return "error";
        }

        updatePull++;
        // Return a json object containing the blackcard, and all whitecards of the
        // player that is requesting the update
        final BlackCard blackCard = this.blackCard;
        final int round = this.round;
        final int score = player.score;
        // If the host ip is the same as the players ip, role is host, otherwise role is
        // player
        final String role = player.getRole();
        final JSONObject json = new JSONObject();
        json.put("blackCard", blackCard.content);
        json.put("isCzar", player.isCzar);
        json.put("round", round);
        json.put("score", score);
        json.put("role", role);
        final String[] playerNames = new String[100];
        final String[] whiteCards = new String[10];
        final String[] putCards = new String[100];
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].name.equals(player.name)) {
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
        json.put("chat", chat);

        if (updatePull % 100 == 0) {
            System.out.println("Update pulled: " + updatePull);
            updatePull = 0;
        }

        return json.toString();
    }

    public void putCard(final String player, final int card) {
        final Player p = getPlayer(player);
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

    public void kickPlayer(final String player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].name.equals(player)) {
                players[i] = null;
                break;
            }
        }
    }

    public void sendChat(final String ip, final String message) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].ip.equals(ip)) {
                for (int j = 0; j < chat.length; j++) {
                    if (chat[j] == null) {
                        chat[j] = players[i].name + ": " + message;
                        break;
                    }
                }
                break;
            }
        }
    }

    private BlackCard blackCard() {
        // column called text, take a random row
        // from the file and return it as a BlackCard object.
        final String csv = "black.csv";
        String text;
        // Read the file and get a random line
        final List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csv))) {
            String line;
            while ((line = br.readLine()) != null) {
                final String[] values = line.split("\n");
                records.add(Arrays.asList(values));
            }
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        final int random = (int) (Math.random() * records.size());
        text = records.get(random).get(0);
        return new BlackCard(text);
    }
}
