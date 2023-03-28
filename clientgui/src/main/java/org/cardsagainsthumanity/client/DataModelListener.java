package org.cardsagainsthumanity.client;

public interface DataModelListener {
    void dataModelChanged(DataModel dataModel);
    void roundChanged(int round);
    void chatChanged(String[] chat);

    void playersChanged(String[] players);
}