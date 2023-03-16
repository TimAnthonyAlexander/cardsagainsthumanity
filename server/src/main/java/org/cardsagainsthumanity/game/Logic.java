package org.cardsagainsthumanity.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.ArrayList;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * 
 */
public class Logic {
    public ArrayList<Player> players = new ArrayList<Player>();
    public BlackCard blackCard = this.blackCard();
    public Integer round = 0;
    public ArrayList<WhiteCard> putCards = new ArrayList<WhiteCard>();
    public int updatePull = 0;
    public ArrayList<String> chat = new ArrayList<String>();

    public Logic() {
    }

    /**
     *
     */
    public void startGame() {
        round = 1;
        blackCard = this.blackCard();

        // Take a random player and make him .isCzar=true
        Random rand = new Random();
        int randomNum = rand.nextInt(players.size());
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null) {
                players.get(i).isCzar = false;
            }
        }
        players.get(randomNum).isCzar = true;
    }

    public void stopGame() {

    }

    public void joinGame(final String playerName, final String ip) {
        if (round != 0) {
            return;
        }
        Player player = new Player(playerName);
        player.ip = ip;
        players.add(player);
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
        final ArrayList<String> playerNames = new ArrayList<String>();
        final String[] whiteCards = new String[10];
        final ArrayList<String> putCards = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null && players.get(i).name.equals(player.name)) {
                for (int j = 0; j < players.get(i).whitecards.length; j++) {
                    whiteCards[j] = players.get(i).whitecards[j].content;
                }
                break;
            }
        }
        // loop over putcards
        for (int i = 0; i < this.putCards.size(); i++) {
            if (this.putCards.get(i) != null) {
                putCards.set(i, this.putCards.get(i).content);
            }
        }
        // set the player names for each this.players
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null) {
                playerNames.add(players.get(i).name);
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

    public void putCard(final String ip, final int card) {
        Player player = null;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null && players.get(i).ip.equals(ip)) {
                player = players.get(i);
                break;
            }
        }

        if (player != null) {
            for (int i = 0; i < putCards.size(); i++) {
                if (putCards.get(i) == null && player.whitecards[card] != null) {
                    putCards.set(i, player.whitecards[card]);
                    player.whitecards[card] = null;
                    break;
                }
            }
        }
    }

    public void kickPlayer(final String player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null && players.get(i).name.equals(player)) {
                players.set(i, null);
                break;
            }
        }
    }

    public void sendChat(final String ip, final String message) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null && players.get(i).ip.equals(ip)) {
                chat.add(players.get(i).name + ": " + message);
                break;
            }
        }
    }

    private BlackCard blackCard() {
        // column called text, take a random row
        // from the file and return it as a BlackCard object.
        InputStream is = Logic.class.getClassLoader().getResourceAsStream("black.csv");
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
        return new BlackCard(line);
    }
}
