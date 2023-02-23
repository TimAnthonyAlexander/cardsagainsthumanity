package org.cardsagainsthumanity.game;

public class Player {
  public String name;
  public int score;
  public WhiteCard[] whitecards;

  public Player(String name) {
    this.name = name;
    this.score = 0;
    this.whitecards = new WhiteCard[10];
  }
}
