package org.example.Middleware;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Middleware {

    private Map<Integer, String> serverMap;

    public Middleware() {
        serverMap = new HashMap<>();
        serverMap.put(1, "localhost:8030");
        serverMap.put(2, "localhost:8000");
        serverMap.put(3, "localhost:8001");
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(4000)) {
            System.out.println("Middleware started on port 4000");
            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    String clientMessage;
                    while ((clientMessage = in.readLine()) != null) {
                        System.out.println("Received message from client: " + clientMessage);
                        int id = Integer.parseInt(clientMessage.split(":")[0]);
                        String command = clientMessage.split(":", 2)[1];
                        forwardMessageToServer(id, command, out);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void forwardMessageToServer(int id, String command, PrintWriter clientOut) {
        String serverAddress = getServerAddressForId(id);
        if (serverAddress != null) {
            String[] addressParts = serverAddress.split(":");
            String host = addressParts[0];
            int port = Integer.parseInt(addressParts[1]);
            try (Socket serverSocket = new Socket(host, port);
                 PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()))) {

                out.println(id + ":" + command);
                String serverResponse;
                while ((serverResponse = in.readLine()) != null) {
                    clientOut.println(serverResponse);
                }
            } catch (IOException e) {
                e.printStackTrace();
                clientOut.println("Error communicating with server: " + e.getMessage());
            }
        } else {
            clientOut.println("Invalid ID: " + id);
        }
    }

    private String getServerAddressForId(int id) {
        if (id >= 1 && id <= 10) {
            return serverMap.get(1);
        } else if (id >= 11 && id <= 20) {
            return serverMap.get(2);
        } else if (id >= 21 && id <= 30) {
            return serverMap.get(3);
        }
        return null;
    }

    public static void main(String[] args) {
        Middleware middleware = new Middleware();
        middleware.start();
    }
}

