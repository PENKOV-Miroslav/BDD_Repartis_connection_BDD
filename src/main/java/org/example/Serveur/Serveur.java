package org.example.Serveur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Serveur extends AbstractServeurTCP{

    private int minIdRange;
    private int maxIdRange;
    private Connection connection;

    public Serveur(int port, int maxIdRange, int minIdRange,int serverId) {
        super(port);
        this.minIdRange = minIdRange;
        this.maxIdRange = maxIdRange;
        this.connection = ConnexionBDD.getInstance(serverId).getConnection();
    }

    @Override
    protected void handleClientConnection(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Received message: " + clientMessage);
                String[] parts = clientMessage.split(":", 2);
                int id = Integer.parseInt(parts[0]);
                String command = parts[1];

                if (command.startsWith("INSERT")) {
                    handleInsert(command, out);
                } else if (command.startsWith("SELECT")) {
                    handleSelect(command, out);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleInsert(String command, PrintWriter out) {
        try (PreparedStatement stmt = connection.prepareStatement(command)) {
            stmt.executeUpdate();
            out.println("Insert successful");
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Insert failed: " + e.getMessage());
        }
    }

    private void handleSelect(String command, PrintWriter out) {
        try (PreparedStatement stmt = connection.prepareStatement(command);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                out.println("Result: " + rs.getString(1)); // Adjust as necessary for your schema
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("Select failed: " + e.getMessage());
        }
    }
}
