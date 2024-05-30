package org.example.Serveur;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBDD {
    private static ConnexionBDD instance1;
    private static ConnexionBDD instance2;
    private static ConnexionBDD instance3;

    private Connection connection;
    private String url;
    private String username;
    private String password;

    public ConnexionBDD(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
        try {
            this.connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnexionBDD getInstance(int serverId) {
        switch (serverId) {
            case 1:
                if (instance1 == null) {
                    instance1 = new ConnexionBDD("jdbc:mysql://localhost:3306/db1", "root", "root");
                }
                return instance1;
            case 2:
                if (instance2 == null) {
                    instance2 = new ConnexionBDD("jdbc:mysql://localhost:3306/db2", "root", "root");
                }
                return instance2;
            case 3:
                if (instance3 == null) {
                    instance3 = new ConnexionBDD("jdbc:mysql://localhost:3306/db3", "root", "root");
                }
                return instance3;
            default:
                throw new IllegalArgumentException("Invalid server ID");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
