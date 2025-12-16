package org.example;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        try (Connection conn = DbConnection.getConnection()) {
            System.out.println("✅ Connexion réussie à la base !");
        } catch (Exception e) {
            System.out.println("❌ Échec de la connexion");
            e.printStackTrace();
        }
    }
}
