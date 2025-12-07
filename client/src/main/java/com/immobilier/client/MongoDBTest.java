package com.immobilier.client;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDBTest {
    public static void main(String[] args) {
        try {
            System.out.println("🔍 Test de connexion MongoDB depuis le client...\n");

            MongoClient client = MongoClients.create("mongodb://localhost:27017");
            MongoDatabase db = client.getDatabase("ImmobilierDB");

            System.out.println("✅ Connexion réussie!");
            System.out.println("📊 Base: " + db.getName());

            System.out.println("\n📋 Collections:");
            for (String name : db.listCollectionNames()) {
                long count = db.getCollection(name).countDocuments();
                System.out.println("  - " + name + " (" + count + " documents)");
            }

            System.out.println("\n👥 Utilisateurs:");
            for (Document doc : db.getCollection("utilisateurs").find().limit(5)) {
                System.out.println("  • " + doc.getString("nom") +
                        " (" + doc.getString("email") + ") - " +
                        doc.getString("role"));
            }

            System.out.println("\n🏠 Biens:");
            for (Document doc : db.getCollection("biens").find().limit(5)) {
                System.out.println("  • " + doc.getString("titre") +
                        " - " + doc.getDouble("prix") + " € - " +
                        (doc.getBoolean("disponible") ? "Disponible" : "Vendu"));
            }

            client.close();
            System.out.println("\n✅ Test terminé!");

        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
