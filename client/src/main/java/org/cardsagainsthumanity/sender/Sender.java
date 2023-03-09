package org.cardsagainsthumanity.sender;

import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.Socket;

public class Sender {

    private PrintWriter out;
    private BufferedReader in;
    private Socket clientSocket;

    public Sender() throws IOException{
        clientSocket = new Socket("172.20.10.3", 8761);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg){
        try{
            out.println(msg);
            String resp = in.readLine();
            return resp;
        }catch(IOException e){
            e.printStackTrace();
            return "Critical failure in sending message";
        }
    }

    public String closeConnection(){
        try{
            in.close();
            out.close();
            clientSocket.close();
            return "Closed Succesfully";
        }catch(IOException e){
            e.printStackTrace();
            return "Critical failure in closing connection";
        }

    }

}
