package org.cardsagainsthumanity.receiver;

import org.cardsagainsthumanity.EchoThread;
import org.cardsagainsthumanity.game.Logic;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

// This class is the receiver, it loops and waits for any data on port 8761
// The message starts with a word, which is the command, and the rest of the message is the data
public class Receiver {
    public String message = "";

    public Receiver() throws IOException {
        // Listen on port 8761
        ServerSocket serverSocket = null;

        // Automatically grab the ip of the user (not the loopback address)
        InetAddress addr = InetAddress.getLocalHost();

        serverSocket = new ServerSocket(8761, 50, addr);

        System.out.println("Server started on " + addr.getHostAddress() + " on port 8761");

        Logic logic = new Logic();

        while (true) {
            Socket socket = null;
            socket = serverSocket.accept();

            new EchoThread(socket, logic).start();
        }
    }
}
