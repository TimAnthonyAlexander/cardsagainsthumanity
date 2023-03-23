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
    private static final int ROUNDS = 10;

    public ArrayList<Player> players = new ArrayList<Player>();
    public BlackCard blackCard = this.blackCard();
    public ArrayList<Player> putPlayers = new ArrayList<Player>();
    public Integer round = 0;
    public ArrayList<WhiteCard> putCards = new ArrayList<WhiteCard>();
    public int updatePull = 0;
    public ArrayList<String> chat = new ArrayList<String>();
    public Player winner = null;

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
        System.out.println("Player " + players.get(randomNum).name + " is the czar");
        sendServerMessage("Player " + players.get(randomNum).name + " is the czar");
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
        sendServerMessage("Player " + playerName + " joined the game");
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
        final ArrayList<String> whiteCards = new ArrayList<String>();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null && players.get(i).name.equals(player.name)) {
                for (int j = 0; j < players.get(i).whitecards.size(); j++) {
                    if (players.get(i).whitecards.get(j) != null) {
                        whiteCards.add(players.get(i).whitecards.get(j).content);
                    }
                }
                break;
            }
        }
        // set the player names for each this.players
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null) {
                playerNames.add(players.get(i).name);
            }
        }
        final ArrayList<String> putCards = new ArrayList<String>();
        // for each this.putCards, add the content to putCards
        for (int i = 0; i < this.putCards.size(); i++) {
            if (this.putCards.get(i) != null) {
                putCards.add(this.putCards.get(i).content);
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
        if (round == 0) {
            return;
        }

        Player player = null;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null && players.get(i).ip.equals(ip)) {
                player = players.get(i);
                break;
            }
        }

        if (player.isCzar) {
            return;
        }

        // If the player is in putPlayers, return
        for (int i = 0; i < putPlayers.size(); i++) {
            if (putPlayers.get(i) != null && putPlayers.get(i) == player) {
                return;
            }
        }

        System.out.println("Player " + player.name + " put card " + card);

        if (player != null) {

            if (player.whitecards.get(card) != null) {
                putCards.add(player.whitecards.get(card));
                player.whitecards.remove(card);
                putPlayers.add(player);
            }
        }
    }

    public void chooseCard(final String ip, final int card) {
        Player player = null;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null && players.get(i).ip.equals(ip)) {
                player = players.get(i);
                break;
            }
        }

        // print out all putcards
        for (int i = 0; i < putCards.size(); i++) {
            if (putCards.get(i) != null) {
                System.out.println("Putcard: " + putCards.get(i).content + " with index " + i);
            }
        }

        // Check if card exists in putCards
        if (putCards.get(card) == null) {
            return;
        }
        

        if (player != null && player.isCzar) {
            for (int i = 0; i < players.size(); i++) {
                System.out.println("Choose card: " + card);
                if (players.get(i) != null && players.get(i).name.equals(putCards.get(card).player.name)) {
                    players.get(i).score++;
                    break;
                }
            }
            putCards.clear();
            round++;
            blackCard = this.blackCard();
            // Get current czar
            int currentCzar = 0;
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i) != null && players.get(i).isCzar) {
                    currentCzar = i;
                    break;
                }
            }
            // set all czars false
            for (int i = 0; i < players.size(); i++) {
                if (players.get(i) != null) {
                    players.get(i).isCzar = false;
                }
            }
            // Calculate next in row
            int newCzar;
            if (currentCzar == players.size() - 1) {
                newCzar = 0;
            } else {
                newCzar = currentCzar + 1;
            }
            players.get(newCzar).isCzar = true;
            putPlayers.clear();
            System.out.println("Player " + newCzar + " is now czar");
            sendServerMessage("Player " + players.get(newCzar).name + " is now czar");
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

    public void sendServerMessage(final String message) {
        chat.add("Server: " + message);
    }

    public void setCzar(final String ip, int player) {
        // Set all players to not czar
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null) {
                players.get(i).isCzar = false;
            }
        }

        // Set the player to czar
        Player playerToBeCzar = players.get(player);
        playerToBeCzar.isCzar = true;
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
