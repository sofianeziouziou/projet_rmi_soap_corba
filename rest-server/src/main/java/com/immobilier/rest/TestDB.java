package com.immobilier.rest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDB {
    public static void main(String[] args) {
        try (Connection conn = DBConnexion.getConnection()) {
            System.out.println("✅ Connexion à la BD réussie !");

            // Test simple : lire les utilisateurs
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Utilisateur");
            if (rs.next()) {
                System.out.println("ℹ️ Nombre d'utilisateurs dans la BD : " + rs.getInt("total"));
            }

            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println("❌ Impossible de se connecter à la BD : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
