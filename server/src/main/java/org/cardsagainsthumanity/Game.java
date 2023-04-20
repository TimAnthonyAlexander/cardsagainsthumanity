package org.cardsagainsthumanity;

import org.cardsagainsthumanity.game.Logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Game {
    public String response;
    public Logic logic;

    public void execute(String message, String ip, Socket socket, ServerSocket serverSocket)
            throws UnknownHostException, IOException {
        // Split the message into command and data

        String command;
        String[] split;

        // If the message does not contain a space, the command is the whole message
        if (!message.contains(" ")) {
            command = message;
            split = new String[0];
        } else {
            split = message.split(" ", 4);
            command = split[0];
        }

        this.response = this.runCommand(command, split, ip, socket, serverSocket);
    }

    private String runCommand(String command, String[] data, String ip, Socket socket, ServerSocket serverSocket)
            throws UnknownHostException, IOException {
        String response;

        // Ip of the connecting client

        switch (command) {
            case "start":
                response = "{\"Message\": \"Starting game\", id: \"" + data[1] + "\"}";
                logic.startGame();
                break;
            case "stop":
                response = "{\"Message\": \"Stopping game\", id: \"" + data[1] + "\"}";
                logic.stopGame(socket, serverSocket);
                break;
            case "join":
                response = "{\"Message\": \"Joining game as " + data[2] + "\", id: \"" + data[1] + "\"}";
                logic.joinGame(data[2], ip);
                break;
            case "exit":
                response = "{\"Message\": \"Exiting game\", id: \"EXIT\"}";
                break;
            case "kick":
                logic.kickPlayer(data[2]);
                response = "{\"Message\": \"Kicking player\", id: \"" + data[1] + "\"}";
                break;
            case "update":
                response = logic.getUpdate(ip, data[1]);
                break;
            case "sendChat":
                String id = data[1];
                // Everything after data 1
                String chat = "";
                for (int i = 2; i < data.length; i++) {
                    chat += data[i] + " ";
                }
                response = "{\"Message\": \"Sending chat\", id: \"" + id + "\"}";
                logic.sendChat(ip, chat);
                break;
            case "sendServerMessage":
                String message = "";
                for (int i = 2; i < data.length; i++) {
                    message += data[i] + " ";
                }
                logic.sendServerMessage(message);
                response = "{\"Message\": \"Sending server message\", id: \"" + data[1] + "\"}";
                break;
            case "putCard":
                logic.putCard(ip, Integer.parseInt(data[2]));
                response = "{\"Message\": \"Putting card\", id: \"" + data[1] + "\"}";
                break;
            case "setCzar":
                logic.setCzar(ip, Integer.parseInt(data[2]));
                response = "{\"Message\": \"Setting czar\", id: \"" + data[1] + "\"}";
                break;
            case "chooseCard":
                logic.chooseCard(ip, Integer.parseInt(data[2]));
                response = "{\"Message\": \"Choosing card\", id: \"" + data[1] + "\"}";
                break;
            default:
                response = "{\"Message\": \"Invalid command\", id: \"invalid\"}";
                break;
        }

        return response;
    }

}
