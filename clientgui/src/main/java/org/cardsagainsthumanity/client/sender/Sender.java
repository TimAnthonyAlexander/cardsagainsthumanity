package org.cardsagainsthumanity.client.sender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.InetAddress;
import java.net.SocketException;

public class Sender {

    private PrintWriter out;
    private BufferedReader in;
    private Socket clientSocket;

    public Sender(String code) throws IOException{
        String host = code;
        int port = 8761;
                String[] codArr = code.split(":");
        if(codArr.length > 1) {
            host = codArr[0];
            try {
                port = Integer.parseInt(codArr[1]);
            }catch(Exception e){
                System.out.println(e);
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
            String resp = in.readLine();
            return resp;
        }catch(IOException e){
            //e.printStackTrace();
            return "Connection lost";
        }
    }

    public String closeConnection(){
        try{
            in.close();
            out.close();
            clientSocket.close();
            return "Closed Successfully";
        }catch(IOException e){
            e.printStackTrace();
            return "Critical failure in closing connection";
        }
    }
}
