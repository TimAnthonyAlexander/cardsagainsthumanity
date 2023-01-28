package org.cardsagainsthumanity;

public class Game {
    public String response;

    public void execute(String message) {
        // Split the message into command and data

        String command;
        String data;

        // If the message does not contain a space, the command is the whole message
        if (!message.contains(" ")) {
            command = message;
            data = "";
        } else {
            String[] split = message.split(" ", 2);
            command = split[0];
            data = split[1];
        }

        this.response = runCommand(command, data);
    }

    private static String runCommand(String command, String data) {
        String response;

        switch (command) {
            case "start":
                response = "Starting game";
                break;
            case "stop":
                response = "Stopping game";
                break;
            case "exit":
                response = "exit_code";
                break;
            default:
                response = "Invalid command";
                break;
        }

        return response;
    }
}
