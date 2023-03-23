package org.cardsagainsthumanity;

import org.cardsagainsthumanity.receiver.Receiver;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        // Start the receiver
        new Receiver(args);
    }
}
