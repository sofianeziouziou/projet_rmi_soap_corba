package com.immobilier.rest;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.FindIterable;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import static com.mongodb.client.model.Filters.*;

public class UserDAO {
    private final MongoCollection<Document> collection;

    public UserDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.collection = db.getCollection("utilisateurs");
    }

    /**
     * Trouve un utilisateur par email et mot de passe
     */
    public Utilisateur findByEmailAndPassword(String email, String password) {
        try {
            Document doc = collection.find(
                    and(eq("email", email), eq("password", password))
            ).first();

            return doc != null ? documentToUser(doc) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Trouve un utilisateur par email uniquement
     */
    public Utilisateur findByEmail(String email) {
        try {
            Document doc = collection.find(eq("email", email)).first();
            return doc != null ? documentToUser(doc) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Crée un nouvel utilisateur
     */
    public Utilisateur create(Utilisateur u) {
        try {
            // Générer un nouvel ID
            long newId = collection.countDocuments() + 1;

            Document doc = new Document()
                    .append("_id", newId)
                    .append("nom", u.getNom())
                    .append("email", u.getEmail())
                    .append("password", u.getPassword())
                    .append("role", u.getRole());

            collection.insertOne(doc);

            u.setId(newId);
            u.setPassword(null); // Ne pas renvoyer le mot de passe
            return u;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Liste tous les utilisateurs
     */
    public List<Utilisateur> findAll() {
        List<Utilisateur> users = new ArrayList<>();
        try {
            FindIterable<Document> docs = collection.find();
            for (Document doc : docs) {
                users.add(documentToUser(doc));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Convertit un Document MongoDB en objet Utilisateur
     */
    private Utilisateur documentToUser(Document doc) {
        Utilisateur u = new Utilisateur();
        u.setId(doc.getLong("_id"));
        u.setNom(doc.getString("nom"));
        u.setEmail(doc.getString("email"));
        u.setPassword(doc.getString("password"));
        u.setRole(doc.getString("role"));
        return u;
    }
}
