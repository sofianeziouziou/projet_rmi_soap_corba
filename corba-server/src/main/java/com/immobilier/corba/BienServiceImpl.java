package com.immobilier.corba;

import Immobilier.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BienServiceImpl extends BienServicePOA {

    @Override
    public int addBien(Immobilier.Bien b) {
        try (Connection c = DBConnexion.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "INSERT INTO Biens(titre, description, prix, disponible, agentId) VALUES(?,?,?,?,?)",
                     Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, b.titre);
            ps.setString(2, b.description);
            ps.setDouble(3, b.prix);
            ps.setBoolean(4, b.disponible);
            ps.setInt(5, (int) b.agentId); // converti en int
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                return keys.getInt(1); // return int, pas long
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Immobilier.Bien[] listBiens() {
        List<Immobilier.Bien> list = new ArrayList<>();
        try (Connection c = DBConnexion.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT id,titre,description,prix,disponible,agentId FROM Biens")) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Immobilier.Bien b = new Immobilier.Bien();
                b.id = rs.getInt("id");           // int pour correspondre à IDL
                b.titre = rs.getString("titre");
                b.description = rs.getString("description");
                b.prix = rs.getDouble("prix");
                b.disponible = rs.getBoolean("disponible");
                b.agentId = rs.getInt("agentId"); // int
                list.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.toArray(new Immobilier.Bien[0]);
    }

    @Override
    public Immobilier.Bien[] listBiensAgent(int agentId) {
        List<Immobilier.Bien> list = new ArrayList<>();
        try (Connection c = DBConnexion.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT id,titre,description,prix,disponible,agentId FROM Biens WHERE agentId=?")) {

            ps.setInt(1, agentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Immobilier.Bien b = new Immobilier.Bien();
                b.id = rs.getInt("id");
                b.titre = rs.getString("titre");
                b.description = rs.getString("description");
                b.prix = rs.getDouble("prix");
                b.disponible = rs.getBoolean("disponible");
                b.agentId = rs.getInt("agentId");
                list.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list.toArray(new Immobilier.Bien[0]);
    }

    @Override
    public boolean checkDisponibilite(int bienId) {
        try (Connection c = DBConnexion.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT disponible FROM Biens WHERE id=?")) {

            ps.setInt(1, bienId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBoolean("disponible");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
