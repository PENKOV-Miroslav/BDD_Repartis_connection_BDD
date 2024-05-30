package org.example;


import org.example.Serveur.Serveur;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> new Serveur(8030, 1, 10, 1).start()).start();
        new Thread(() -> new Serveur(8000, 11, 20, 2).start()).start();
        new Thread(() -> new Serveur(8001, 21, 30, 3).start()).start();
    }
}