package org.cardsagainsthumanity.game;

import org.json.JSONObject;

public class Logic {
    public Player[] players;
    public BlackCard blackCard;
    public Integer round;

    public Logic() {
        players = new Player[100];
    }

    public void startGame() {
        round = 1;
        blackCard = this.blackCard();
    }

    private BlackCard blackCard() {
        // The black cards are in ../../../../../../../black.csv and it is a single
        // column called text, take a random row
        // from the file and return it as a BlackCard object.
        String text = "";
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null) {
                text = players[i].name;
                break;
            }
        }
        return new BlackCard(text);
    }

    public void joinGame(String playerName) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new Player(playerName);
                break;
            }
        }
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
        // Return a json object containing the blackcard, and all whitecards of the
        // player that is requesting the update
        BlackCard blackCard = this.blackCard();
        Player player = getPlayer(playerName);
        JSONObject json = new JSONObject();
        json.put("blackCard", blackCard);
        json.put("round", round);
        for (int i = 0; i < players.length; i++) {
            if (players[i] != null && players[i].name.equals(playerName)) {
                json.put("whitecards", players[i].whitecards);
            }
        }
        return json.toString();
    }
}
