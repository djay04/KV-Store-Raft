package com.khaledsaleh.kvstore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// Per-Client handler logic
public class ClientHandler implements Runnable {

    private final Socket clientSocket;
    private final KVStore kvStore;

    public ClientHandler(Socket clientSocket, KVStore kvStore) {
        this.clientSocket = clientSocket;
        this.kvStore = kvStore;
    }

    @Override
    public void run() {
        try {
            // Handle client requests here
            // Read from clientSocket, process requests using kvStore, and write responses back to clientSocket

            // Example: Read a request, process it, and send a response

            // Pseudocode:

            // BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            // PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            
            String request;
            while ((request = in.readLine()) != null) {

                String firstWord = request.split(" ")[0] != null ? request.split(" ")[0] : "";

                String formattedResponse = "";

                
                try {
                    switch (firstWord){
                        
                        case "SET":

                            String[] setParts = request.split(" ", 3);

                            kvStore.set(setParts[1], setParts[2]);

                            formattedResponse = "SET key: " + setParts[1] + " value: "
    + setParts[2];
                            break;

                        case "GET":

                            String[] getParts = request.split(" ", 2);

                            String value = kvStore.get(getParts[1]) != null ? kvStore.get(getParts[1]) : "Key not found";

                            formattedResponse = "GET key: " + getParts[1] + " value: " + value;

                            break;
                        
                        case "DEL":

                            String[] delParts = request.split(" ");

                            kvStore.del(delParts[1]);

                            formattedResponse = "DEL key: "
                                + delParts[1];
                            break;
                    default:
                            formattedResponse = "Invalid request";
                        }

                    
                    out.println(formattedResponse);

                } catch (RuntimeException e) {
                    out.println("Error processing request: " + e.getMessage());
                }
            }
        } catch (IOException e) {
        
        System.err.println("Error handling client request: " + e.getMessage());

        } finally {

        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }
        }
    }
}

            
        
