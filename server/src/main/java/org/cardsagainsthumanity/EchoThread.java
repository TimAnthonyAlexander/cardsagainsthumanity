package org.cardsagainsthumanity;

import org.cardsagainsthumanity.game.Logic;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class EchoThread extends Thread {
    protected Socket socket;
    public Logic logic;
    protected ServerSocket serverSocket;

    public EchoThread(Socket clientSocket, Logic logic, ServerSocket serverSocket) {
        this.socket = clientSocket;
        this.logic = logic;
        this.serverSocket = serverSocket;
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
        game.logic = this.logic;

        String response;

        System.out.println("Client connected on " + socket.getInetAddress().getHostAddress());

        while (true) {
            String message = "";

            try {
                message = in.readLine();
                game.execute(message, socket.getInetAddress().getHostAddress(), socket, serverSocket);
                response = game.response;

                if (response.contains("Exiting game")) {
                    System.out.println("Client disconnected on " + socket.getInetAddress().getHostAddress());
                    game.logic.sendServerMessage("Client disconnected on " + socket.getInetAddress().getHostAddress());
                    socket.close();
                    return;
                }

                response += "\r\n";

                out.writeBytes(response);
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
