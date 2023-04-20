package org.cardsagainsthumanity.client;

import javax.swing.*;
import java.awt.*;

public class ScoreBoard extends JPanel {
    private final JLabel score;
    private final JLabel round;
    private final JLabel role;

    public ScoreBoard(){
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.score = new JLabel();
        this.round = new JLabel();
        this.role = new JLabel();
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        this.add(score);
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        this.add(round);
        this.add(Box.createRigidArea(new Dimension(10, 0)));
        this.add(role);
    }

    public void updateContent(int score, int round, boolean czar){
        this.score.setText("Your Score: " + score);
        this.round.setText("Round: " + round +" / 10");
        if(czar){
            this.role.setText("Role: czar");
        }else{
            this.role.setText("Role: player");
        }
    }
}
