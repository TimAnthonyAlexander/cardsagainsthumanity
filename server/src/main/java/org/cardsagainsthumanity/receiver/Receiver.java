package org.cardsagainsthumanity.receiver;

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
        ServerSocket serverSocket = new ServerSocket(8761);
        Socket socket = serverSocket.accept();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        Game game = new Game();

        String response;

        while (true) {
            String message = "";

            try {
                message = in.readLine();
                game.execute(message);
                response = game.response;

                if (response.equals("exit_code")) {
                    break;
                }

                response += "\n";

                socket.getOutputStream().write(response.getBytes());

                System.out.println(response);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}