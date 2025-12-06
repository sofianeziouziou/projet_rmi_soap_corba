package com.immobilier.rest;

import java.sql.*;

public class UserDAO {

    public Utilisateur findByEmailAndPassword(String email, String password) throws Exception {
        try (Connection c = DBConnexion.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT id, nom, email, password, role FROM Utilisateurs WHERE email=? AND password=?")) {
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                return u;
            }
            return null;
        }
    }

    public Utilisateur findByEmail(String email) throws Exception {
        try (Connection c = DBConnexion.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT id, nom, email, password, role FROM Utilisateurs WHERE email=?")) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setRole(rs.getString("role"));
                return u;
            }
            return null;
        }
    }

    public Utilisateur create(Utilisateur u) throws Exception {
        try (Connection c = DBConnexion.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO Utilisateurs(nom, email, password, role) VALUES(?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getNom());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPassword());
            ps.setString(4, u.getRole());
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                u.setId(keys.getInt(1));
                u.setPassword(null); // pour ne pas renvoyer le mot de passe
                return u;
            }
            return null;
        }
    }
}
