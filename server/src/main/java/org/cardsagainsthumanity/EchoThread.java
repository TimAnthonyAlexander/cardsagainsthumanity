package org.cardsagainsthumanity;

import java.io.*;
import java.net.Socket;

public class EchoThread extends Thread {
    protected Socket socket;

    public EchoThread(Socket clientSocket) {
        this.socket = clientSocket;
    }

    public void run() {
        BufferedReader in = null;
        DataOutputStream out = null;
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Game game = new Game();

        String response;

        System.out.println("Client connected on " + socket.getInetAddress().getHostAddress());

        while (true) {
            String message = "";

            try {
                message = in.readLine();
                game.execute(message);
                response = game.response;

                if (response.equals("exit_code")) {
                    System.out.println("Client disconnected on " + socket.getInetAddress().getHostAddress());
                    socket.close();
                    return;
                }

                out.writeBytes(response);

                System.out.println(response);
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                return;
            }
        }


    }
}
