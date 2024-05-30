package org.example.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 4000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Sending a sample INSERT command
            out.println("11:INSERT INTO table_name (id, data) VALUES (11, 'sample data')");
            System.out.println("Response from middleware: " + in.readLine());

            // Sending a sample SELECT command
            out.println("5:SELECT * FROM table_name WHERE id = 5");
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println("Response from middleware: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

