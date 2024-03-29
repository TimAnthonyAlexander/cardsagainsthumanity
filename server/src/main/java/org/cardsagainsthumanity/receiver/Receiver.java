package org.cardsagainsthumanity.receiver;

import org.cardsagainsthumanity.EchoThread;
import org.cardsagainsthumanity.game.Logic;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

// This class is the receiver, it loops and waits for any data on port 8761
// The message starts with a word, which is the command, and the rest of the message is the data
public class Receiver {
    public String message = "";

    public Receiver(String[] args) throws IOException {
        // Listen on port 8761
        ServerSocket serverSocket = null;

        // Automatically grab the ip of the user (not the loopback address)
        // If args first argument is null
        InetAddress addr;
        if (args.length == 0 || args[0] == null) {
            addr = InetAddress.getLocalHost();
        } else {
            addr = InetAddress.getByName(args[0]);
        }

        int port;
        if (args.length < 2 || args[1] == null) {
            port = 8761;
        } else {
            port = Integer.parseInt(args[1]);
        }

        serverSocket = new ServerSocket(port, 50, addr);

        System.out.println("Server started on " + addr.getHostAddress() + " on port " + port);

        Logic logic = new Logic();

        int requests = 0;

        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
            } catch (SocketException e) {
                break;
            }

            requests++;

            new EchoThread(socket, logic, serverSocket).start();

            if (requests % 100 == 0) {
                System.out.println("Requests: " + requests);
            }
        }
    }
}
