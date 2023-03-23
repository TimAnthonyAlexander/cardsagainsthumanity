package org.cardsagainsthumanity.game;

public class WhiteCard extends Card {
    public Player player;
    public WhiteCard(String content, Player player) {
        this.content = content;
        this.player = player;
    }
}
