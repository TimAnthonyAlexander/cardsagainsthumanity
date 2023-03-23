package org.cardsagainsthumanity.client;

import java.util.ArrayList;
import java.util.List;

public class DataModel {
    private int round;
    private int previousRound;
    private int score;

    private int removedHandCards;
    private int missingPutCards;

    private String role;

    private WhiteCard[] putCards, handCards;
    private BlackCard blackCard;

    private boolean czar;

    private List<DataModelListener> listeners = new ArrayList<DataModelListener>();

    public DataModel(){
        removedHandCards = 0;
        missingPutCards = 0;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        if(this.round != round) {
            this.round = round;
            notifyListeners();
        }
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if(this.score != score) {
            this.score = score;
            notifyListeners();
        }
    }

    public WhiteCard[] getPutCards() {
        return putCards;
    }

    public void setPutCards(WhiteCard[] putCards) {
        if(this.putCards != null) {
            int temp = 0;
            for (int i = 0; i < this.putCards.length; i++) {
                if (putCards[i] == null) {
                    temp++;
                }
            }
            if (temp != missingPutCards) {
                this.missingPutCards = temp;
                this.putCards = putCards;
                notifyListeners();
            }
        }else{
            this.putCards = putCards;
            notifyListeners();
        }
    }

    public WhiteCard[] getHandCards() {
        return handCards;
    }

    public void setHandCards(WhiteCard[] handCards) {
        if(this.handCards != null) {
            int temp = 0;
            for (int i = 0; i < this.handCards.length; i++) {
                if (handCards[i] == null) {
                    temp++;
                }
            }
            if (temp > removedHandCards) {
                removedHandCards = temp;
                this.handCards = handCards;
                notifyListeners();
            }
        }else{
            this.handCards = handCards;
            notifyListeners();
        }
    }

    public BlackCard getBlackCard() {
        return blackCard;
    }

    public void setBlackCard(BlackCard blackCard) {
        if(this.blackCard != null) {
            if (!(this.blackCard.getText().equals(blackCard.getText()))) {
                this.blackCard.setText(blackCard.getText());
                notifyListeners();
            }
        }else{
            this.blackCard = blackCard;
            notifyListeners();
        }

    }

    public boolean isCzar() {
        return czar;
    }

    public void setCzar(boolean czar) {
        if(this.czar != czar) {
            this.czar = czar;
            notifyListeners();
        }
    }

    public int getPreviousRound() {
        return previousRound;
    }

    public void setPreviousRound(int previousRound) {
        if(this.previousRound != previousRound) {
            this.previousRound = previousRound;
            notifyListeners();
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if(this.role != null) {
            if (!this.role.equals(role)) {
                this.role = role;
                notifyListeners();
            }
        }else{
            this.role = role;
            notifyListeners();
        }
    }

    public void addListener(DataModelListener listener) {
        listeners.add(listener);
    }

    public void removeListener(DataModelListener listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (DataModelListener listener : listeners) {
            listener.dataModelChanged(this);
        }
    }
}
