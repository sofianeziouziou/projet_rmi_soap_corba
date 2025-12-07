package com.immobilier.rest;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class TestMongoDB {
    public static void main(String[] args) {
        try {
            System.out.println("🔍 Test de connexion MongoDB...\n");

            MongoDatabase db = MongoDBConnection.getDatabase();
            System.out.println("✅ Connexion MongoDB réussie!");
            System.out.println("📊 Base de données: " + db.getName());

            // Lister les collections
            System.out.println("\n📋 Collections disponibles:");
            for (String name : db.listCollectionNames()) {
                long count = db.getCollection(name).countDocuments();
                System.out.println("  - " + name + " (" + count + " documents)");
            }

            // Test lecture utilisateurs
            System.out.println("\n👥 Utilisateurs dans la base:");
            for (Document doc : db.getCollection("utilisateurs").find()) {
                System.out.println("  ID: " + doc.get("_id") +
                        ", Nom: " + doc.getString("nom") +
                        ", Email: " + doc.getString("email") +
                        ", Role: " + doc.getString("role"));
            }

            // Test lecture biens
            System.out.println("\n🏠 Biens dans la base:");
            for (Document doc : db.getCollection("biens").find()) {
                System.out.println("  ID: " + doc.get("_id") +
                        ", Titre: " + doc.getString("titre") +
                        ", Prix: " + doc.getDouble("prix") +
                        ", Disponible: " + doc.getBoolean("disponible"));
            }

            System.out.println("\n✅ Test terminé avec succès!");

        } catch (Exception e) {
            System.err.println("❌ Erreur lors du test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}