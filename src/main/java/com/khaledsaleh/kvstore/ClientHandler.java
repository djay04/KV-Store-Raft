package main.java.com.khaledsaleh.kvstore;

import java.util.concurrent;
import java.net.ServerSocket;
import java.net.Socket;


// Per-Client handler logic

public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
    }
    
}
