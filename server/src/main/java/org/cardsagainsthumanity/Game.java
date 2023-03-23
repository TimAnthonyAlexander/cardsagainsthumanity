package org.cardsagainsthumanity;

import org.cardsagainsthumanity.game.Logic;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Game {
    public String response;
    public Logic logic;

    public void execute(String message, String ip, Socket socket) throws UnknownHostException, IOException {
        // Split the message into command and data

        String command;
        String[] split;

        // If the message does not contain a space, the command is the whole message
        if (!message.contains(" ")) {
            command = message;
            split = new String[0];
        } else {
            split = message.split(" ", 2);
            command = split[0];
        }

        this.response = this.runCommand(command, split, ip, socket);
    }

    private String runCommand(String command, String[] data, String ip, Socket socket)
            throws UnknownHostException, IOException {
        String response;

        // Ip of the connecting client

        switch (command) {
            case "start":
                response = "Starting game";
                logic.startGame();
                break;
            case "stop":
                response = "Stopping game";
                logic.stopGame(socket);
                break;
            case "join":
                response = "Joining as " + data[1];
                logic.joinGame(data[1], ip);
                break;
            case "exit":
                response = "exit_code";
                break;
            case "kick":
                response = "Kicking " + data[1];
                logic.kickPlayer(data[1]);
                break;
            case "update":
                response = logic.getUpdate(ip);
                break;
            case "sendChat":
                response = "Sending chat";
                logic.sendChat(ip, data[1]);
                break;
            case "putCard":
                response = "Putting card";
                logic.putCard(ip, Integer.parseInt(data[1]));
                break;
            case "setCzar":
                response = "Setting czar";
                logic.setCzar(ip, Integer.parseInt(data[1]));
                break;
            case "chooseCard":
                response = "Choosing card as czar";
                logic.chooseCard(ip, Integer.parseInt(data[1]));
                break;
            default:
                response = "Invalid command: " + command;
                break;
        }

        return response;
    }

}
