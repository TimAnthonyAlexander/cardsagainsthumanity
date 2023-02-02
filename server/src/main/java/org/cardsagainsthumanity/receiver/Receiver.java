package org.cardsagainsthumanity.receiver;

import org.cardsagainsthumanity.EchoThread;
import org.cardsagainsthumanity.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

// This class is the receiver, it loops and waits for any data on port 8761
// The message starts with a word, which is the command, and the rest of the message is the data
public class Receiver {
    public String message = "";

    public Receiver() throws IOException {
        // Listen on port 8761
        ServerSocket serverSocket = null;

        serverSocket = new ServerSocket(8761);


        while (true) {
            Socket socket = null;
            socket = serverSocket.accept();

            new EchoThread(socket).start();
        }
    }
}