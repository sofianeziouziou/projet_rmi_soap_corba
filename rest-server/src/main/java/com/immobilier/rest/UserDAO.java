package com.immobilier.rest;

import com.immobilier.core.MongoDBConnection;
import com.immobilier.core.Utilisateur;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.and;

public class UserDAO {
    private final MongoCollection<Document> coll;

    public UserDAO() {
        MongoDatabase db = MongoDBConnection.getDatabase();
        this.coll = db.getCollection("utilisateurs");
    }

    public Utilisateur findByEmailAndPassword(String email, String password) {
        try {
            Document doc = coll.find(and(eq("email", email), eq("password", password))).first();
            return doc == null ? null : documentToUser(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Utilisateur findByEmail(String email) {
        try {
            Document doc = coll.find(eq("email", email)).first();
            return doc == null ? null : documentToUser(doc);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Utilisateur create(Utilisateur u) {
        try {
            long newId = coll.countDocuments() + 1;
            Document d = new Document()
                    .append("_id", newId)
                    .append("nom", u.getNom())
                    .append("email", u.getEmail())
                    .append("password", u.getPassword())
                    .append("role", u.getRole());
            coll.insertOne(d);
            u.setId(newId);
            u.setPassword(null);
            return u;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Utilisateur> findAll() {
        try {
            List<Utilisateur> list = new ArrayList<>();
            for (Document d : coll.find()) list.add(documentToUser(d));
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

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
