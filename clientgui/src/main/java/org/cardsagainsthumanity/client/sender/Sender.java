package org.cardsagainsthumanity.client.sender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Sender {

    private final PrintWriter out;
    private final BufferedReader in;
    private final Socket clientSocket;

    public Sender(String code) throws IOException{
        String host = code;
        int port = 8761;
                String[] codArr = code.split(":");
        if(codArr.length > 1) {
            host = codArr[0];
            try {
                port = Integer.parseInt(codArr[1]);
            }catch(Exception e){
                System.out.println("Please input a valid port");
            }
        }

        clientSocket = new Socket(host, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public Socket getClientSocket(){
        return clientSocket;
    }

    public String sendMessage(String msg){
        try{
            out.println(msg);
            return in.readLine();
        }catch(IOException e){
            return "Connection lost";
        }
    }

    public void closeConnection(){
        try{
            in.close();
            out.close();
            clientSocket.close();
            System.out.println("Closed Successfully");
        }catch(IOException e){
            System.out.println("Critical failure in closing connection");
        }
    }
}
