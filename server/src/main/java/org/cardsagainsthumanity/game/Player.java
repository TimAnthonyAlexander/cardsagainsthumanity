package org.cardsagainsthumanity.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    public String name;
    public int score;
    public WhiteCard[] whitecards;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.whitecards = new WhiteCard[10];
        for (int i = 0; i < whitecards.length; i++) {
            whitecards[i] = this.whiteCard();
        }
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
