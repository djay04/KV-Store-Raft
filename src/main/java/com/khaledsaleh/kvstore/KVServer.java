package main.java.com.khaledsaleh.kvstore;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Main entrypoint for the KVStore server application - Accept loop, thread pool, etc.

public class KVServer {

    final static int port = 8080; // Default port
    static ExecutorService executorService = Executors.newFixedThreadPool(20);
    private static final Logger logger = LoggerFactory.getLogger(KVServer.class);
    public static void main(String[] args) {
        // Start the server and listen for client connections
        // For each connection, create a new ClientHandler thread

        try{
            
            ServerSocket serverSocket = new ServerSocket(port);

            while (true) {

                try {
                    Socket clientSocket = serverSocket.accept();

                    executorService.submit(new ClientHandler(clientSocket));

                } catch (IOException e){
                    logger.error("Error accepting client connection: " + e.getMessage());
                }
               
            }

        } catch (IOException e) {
            throw new RuntimeException("Error starting server: " + e.getMessage());
        }
        

        
    }
}