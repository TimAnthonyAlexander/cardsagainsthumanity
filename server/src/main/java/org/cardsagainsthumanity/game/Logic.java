package org.cardsagainsthumanity.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;
import java.sql.*;

import org.json.JSONObject;

/**
 * 
 */
public class Logic {
    private int ROUNDS = 10;

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
    public void startGame(int rounds) {
        round = 1;
        this.ROUNDS = rounds;
        blackCard = this.blackCard();

        final Random rand = new Random();
        final int randomNum = rand.nextInt(players.size());
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null) {
                players.get(i).isCzar = false;
            }
        }
        players.get(randomNum).isCzar = true;
        System.out.println("Player " + players.get(randomNum).name + " is the czar");
        sendServerMessage("Player " + players.get(randomNum).name + " is the czar");
    }

    public void stopGame(final Socket socket, final ServerSocket serverSocket) throws IOException {
        sendServerMessage("Game stopped");
        System.out.println("Game stopped");
        socket.close();
        serverSocket.close();
    }

    public void joinGame(final String playerName, final String ip) {
        if (round != 0) {
            return;
        }
        final Player player = new Player(playerName);
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

    public String getUpdate(final String clientip, final String messageId) throws UnknownHostException {
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
        json.put("round", round);
        json.put("score", score);
        json.put("role", role);
        json.put("blackCard", blackCard.content);
        json.put("isCzar", player.isCzar);
        json.put("id", messageId);
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

        // If index is out of bounds, return
        if (card >= putCards.size()) {
            return;
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
                    sendServerMessage("Player " + players.get(i).name + " won the round!");
                    break;
                }
            }
            putCards.clear();
            round++;
            if (round > ROUNDS) {
                round = 0;
                // Calculate winner
                int winner = 0;
                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i) != null && players.get(i).score > players.get(winner).score) {
                        winner = i;
                    }
                }
                try {
                    Class.forName("org.sqlite.JDBC");
                    Connection conn = DriverManager.getConnection("jdbc:sqlite:winner.db");
                    Statement stat = conn.createStatement();
                    stat.executeUpdate("create table if not exists winner (name, score);");
                    PreparedStatement prep = conn.prepareStatement("insert into winner values (?, ?);");
                    prep.setString(1, players.get(winner).name);
                    prep.setInt(2, players.get(winner).score);
                    prep.addBatch();
                    conn.setAutoCommit(false);
                    prep.executeBatch();
                    conn.setAutoCommit(true);
                    ResultSet rs = stat.executeQuery("select * from winner;");
                    while (rs.next()) {
                        System.out.println("name = " + rs.getString("name"));
                        System.out.println("score = " + rs.getInt("score"));
                    }
                    rs.close();
                    conn.close();
                    // Send server message score
                    sendServerMessage("Player " + players.get(winner).name + " won the game with "
                            + players.get(winner).score + " points!");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                chat.add("Player " + players.get(winner).name + " won the game!");
                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i) != null) {
                        players.get(i).score = 0;
                    }
                }
                round = 0;
                // reset all values
                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i) != null) {
                        players.get(i).whitecards.clear();
                        players.get(i).isCzar = false;
                    }
                }
                startGame(ROUNDS);
                for (int i = 0; i < players.size(); i++) {
                    if (players.get(i) != null) {
                        for (int j = 0; j < 10; j++) {
                            players.get(i).whitecards.add(players.get(i).whiteCard());
                        }
                    }
                }
                return;
            }

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
            sendServerMessage("Player " + players.get(newCzar).name + " is now czar");
            System.out.println("Player " + players.get(newCzar).name + " is now czar");
            // Write into local sqlite file the winner, the table is called "winner"
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

    public void setCzar(final String ip, final int playerId) throws UnknownHostException {
        Player player = null;
        // Set all players to not czar
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i) != null) {
                players.get(i).isCzar = false;
                player = players.get(i);
            }
        }

        if (!player.isHost()) {
            return;
        }

        // Set the player to czar
        player.isCzar = true;
    }

    public void setPlayers(final ArrayList<Player> players) {
        this.players = players;
    }

    public void setBlackCard(final BlackCard blackCard) {
        this.blackCard = blackCard;
    }

    public void setPutPlayers(final ArrayList<Player> putPlayers) {
        this.putPlayers = putPlayers;
    }

    public void setRound(final Integer round) {
        this.round = round;
    }

    public void setPutCards(final ArrayList<WhiteCard> putCards) {
        this.putCards = putCards;
    }

    public void setUpdatePull(final int updatePull) {
        this.updatePull = updatePull;
    }

    public void setChat(final ArrayList<String> chat) {
        this.chat = chat;
    }

    public void setWinner(final Player winner) {
        this.winner = winner;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public BlackCard getBlackCard() {
        return blackCard;
    }

    public ArrayList<Player> getPutPlayers() {
        return putPlayers;
    }

    public Integer getRound() {
        return round;
    }

    public ArrayList<WhiteCard> getPutCards() {
        return putCards;
    }

    public int getUpdatePull() {
        return updatePull;
    }

    public ArrayList<String> getChat() {
        return chat;
    }

    public Player getWinner() {
        return winner;
    }

    public int getRounds() {
        return ROUNDS;
    }

    private BlackCard blackCard() {
        // column called text, take a random row
        // from the file and return it as a BlackCard object.
        final InputStream is = Logic.class.getClassLoader().getResourceAsStream("black.csv");
        assert is != null;
        final BufferedReader br = new BufferedReader(new InputStreamReader(is));
        final ArrayList<String> lines = new ArrayList<String>();
        try {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }

        // Now return a random line from the file
        final Random rand = new Random();
        final int randomNum = rand.nextInt(lines.size());

        // Replace "RP" with a random player name
        final int randomPlayer = rand.nextInt(players.size());
        final String playerName = players.get(randomPlayer).name;

        final String line = lines.get(randomNum).replace("RP", playerName);

        return new BlackCard(line);
    }
}
